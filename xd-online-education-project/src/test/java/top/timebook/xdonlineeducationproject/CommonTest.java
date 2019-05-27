package top.timebook.xdonlineeducationproject;

import io.jsonwebtoken.Claims;
import org.junit.Test;
import top.timebook.xdonlineeducationproject.domain.User;
import top.timebook.xdonlineeducationproject.utils.JwtUtils;

/**
 * @ClassName CommonTest
 * @Description 通用方法测试类
 * @Author xiongzl
 * @Date 2019/5/5 15:08
 * @Version 1.0
 **/
public class CommonTest {

    @Test
    public void testGeneJwt(){
        User user = new User();
        user.setId(999);
        user.setHeadImg("www.xdclass.net");
        user.setName("xd");
        String token = JwtUtils.geneJsonWebToken(user);
        System.out.println(token);
    }

    @Test
    public void testCheck(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZGNsYXNzIiwiaWQiOjk5OSwibmFtZSI6InhkIiwiaW1nIjoid3d3LnhkY2xhc3MubmV0IiwiaWF0IjoxNTU3MDQwNDY3LCJleHAiOjE1NTc2NDUyNjd9.4dHc88Jil4L_ei8Bduf2Ki9N2ZvcYaHad-fa-fDUiZM";
        Claims claims = JwtUtils.checkJWT(token);
        System.out.println(claims);
        if (claims == null) {
            System.out.println("token 非法");
        }
    }


}
