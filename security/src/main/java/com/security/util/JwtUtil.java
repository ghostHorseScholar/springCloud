package com.security.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	
    /**
     * 加密密文
     */
	private static final String JWT_SECRET = "SecurityJWT";
	public static final long EXPIRATION_DATE = 15; // token超时时间 
	public static final long ALIPAY_APPLET_FORBID_REFRES_HTIME = 60; // token允许刷新时间

    /**
     * 创建jwt
     * @param id	
     * @param issuer
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, List<String> permissions, List<String> permissionUser, String userName, String subject, Object user) throws ExpiredJwtException {

        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        LocalDateTime nowTime = LocalDateTime.now();
        
        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<>();
        claims.put(AttributeConfig.TOKEN_CLAIMS_PERMISSION_USER, permissionUser);
        claims.put(AttributeConfig.TOKEN_CLAIMS_PERMISSION, permissions);
        claims.put(AttributeConfig.LOGIN_USER, JSON.toJSONString(user));

        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // 下面就是在为payload添加各种标准声明和私有声明了
        return Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(id)                  // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(localDateTimeToDate(nowTime))           // iat: jwt的签发时间
                .setExpiration(getExpirationDate(nowTime, EXPIRATION_DATE))
                .setIssuer(userName)          // issuer：jwt签发人
                .setSubject(subject)        // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, JWT_SECRET) // 设置签名使用的签名算法和签名使用的秘钥
                .compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws ExpiredJwtException {
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(JWT_SECRET)                 //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();     //设置需要解析的jwt
        return claims;
    }

    /**
     * token 刷新：
     * 1.没有过期直接通过；
     * 2.过期时间小于ALIPAY_APPLET_FORBID_REFRES_HTIME需要刷新；
     * 3.超过ALIPAY_APPLET_FORBID_REFRES_HTIME直接返回null；
     *
     * @param oldToken
     * @return
     */
    @SuppressWarnings("unchecked")
	public static String refresh(String token) throws ExpiredJwtException {

        	try {
				parseJWT(token);
				return token;
			} catch (ExpiredJwtException e) {
				Claims claims = e.getClaims();
				long expirationTime = TimeUnit.MINUTES.convert(claims.getExpiration().toInstant().getEpochSecond(), TimeUnit.SECONDS);
				long nowTime = TimeUnit.MINUTES.convert(Instant.now().getEpochSecond(), TimeUnit.SECONDS);
				long tokenTimeout = nowTime-expirationTime;

				if (tokenTimeout <= ALIPAY_APPLET_FORBID_REFRES_HTIME) { // 2.大于ALIPAY_APPLET_FORBID_REFRES_HTIME 小于EXPIRATION_DATE需要刷新
				    return createJWT(claims.getId(), claims.get(AttributeConfig.TOKEN_CLAIMS_PERMISSION, List.class), claims.get(AttributeConfig.TOKEN_CLAIMS_PERMISSION_USER, List.class), claims.getIssuer(), claims.getSubject(), JSON.parse(claims.get(AttributeConfig.LOGIN_USER, String.class)));
				}
				return null;
			}
    }
    


    /**
     * 将LocalDateTime转换为Date
     *
     * @param localDateTime
     * @return Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 生成token过期的时间
     *
     * @param createTime       token创建时间
     * @param calendarInterval token有效时间间隔
     * @return
     */
    public static Date getExpirationDate(LocalDateTime createTime, long calendarInterval) {
        LocalDateTime expirationDate = createTime.plus(calendarInterval, ChronoUnit.MINUTES);
        return localDateTimeToDate(expirationDate);
    }
    
    /**
     * 
     * @param request
     * @return
     */
    public static String getUserName(HttpServletRequest request) throws ExpiredJwtException {
    	JSONObject entity = getUser(request);
			if (entity == null) {
				return null;
			} else {
				return entity.get("userName").toString();
			}
    }
    
    /**
     * 
     * @param request
     * @return
     */
    public static JSONObject getUser(HttpServletRequest request) throws ExpiredJwtException {
		String token = refresh(request.getHeader(AttributeConfig.TOKEN_NAME));
		if (token == null) {
			return null;
		}
		Object obj = JSONObject.parse(parseJWT(token).get(AttributeConfig.LOGIN_USER, String.class));
		if (obj instanceof JSONObject) {
			return (JSONObject)obj;
		} else {
			return null;
		}
    }
    
    public static void main(String[] args) {
    	
    	try {
//    		String token = JwtUtil.createJWT("aa", "bb", "cc");
    		String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2b3VjaGVyIiwiaXNzIjoiW0FETUlOXSIsImV4cCI6MTU1NzcxODc1NSwiaWF0IjoxNTU3NzE3ODU1LCJqdGkiOiJhZG1pbiIsInRva2VuIjoiVk9VQ0hFUiJ9.7do0ku60n_Mr7hLfTJ9VtK7srWJqPOK5FtCaDNzmkNU";
//			token = JwtUtil.refresh(token);
    		System.out.println(token);
			Claims claims = JwtUtil.parseJWT(token);
			System.out.println(claims.getId());
			System.out.println(claims.getIssuer());
			System.out.println(claims.getSubject());
			System.out.println(claims.get("uid", String.class));
			System.out.println(claims.get("user_name", String.class));
			System.out.println(claims.get("nick_name", String.class));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(sdf.format(claims.getExpiration()));
//			System.out.println(JwtUtil.refresh(token));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
    }

}
