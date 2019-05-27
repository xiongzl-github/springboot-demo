package top.timebook.xdonlineeducationproject.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import top.timebook.xdonlineeducationproject.domain.User;

import java.util.Date;

/**
 * jwt 工具类
 */
public class JwtUtils {


    /**
     * 设置发行方
     */
    private static final String SUBJECT;

    /**
     * 设置过期时间
     */
    private static final long EXPIRE;

    /**
     * 设置密钥
     */
    private static final String APPSECRET;

    static {
        EXPIRE = 1000 * 60 * 60 * 24 * 7;
        SUBJECT = "xdclass";
        APPSECRET = "xd666";
    }


    /**
     * @Author xiongzl
     * @Description 生成jwt token
     * @Date 2019/5/5 17:05 
     * @Param [user]
     * @return java.lang.String
     **/
    public static String geneJsonWebToken(User user) {
        if (user == null || user.getId() == null || user.getHeadImg() == null || user.getName() == null) {
            return null;
        }
        return Jwts.builder().setSubject(SUBJECT)
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("img", user.getHeadImg())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, APPSECRET).compact();
    }


    /**
     * @return io.jsonwebtoken.Claims
     * @Author xiongzl
     * @Description 校验token
     * @Date 2019/5/5 12:37
     * @Param [token]
     **/
    public static Claims checkJWT(String token) {
        try {
            return Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
        }
        return null;
    }


}
