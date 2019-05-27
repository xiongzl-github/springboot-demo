package top.timebook.xdonlineeducationproject.intercept;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;
import top.timebook.xdonlineeducationproject.domain.JsonData;
import top.timebook.xdonlineeducationproject.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * The type Login intercepter.
 *
 * @author xiongzl
 */
public class LoginIntercepter implements HandlerInterceptor {

    private static final Gson GSON = new Gson();

    /**
     * 进入controller之前进行拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter("token");
        if (null != token) {
            Claims claims = JwtUtils.checkJWT(token);
            if (null != claims) {
                Integer userId = (Integer) claims.get("id");
                String name = (String) claims.get("name");
                request.setAttribute("user_id", userId);
                request.setAttribute("name", name);
                return true;
            }
        }
        sendJsonMessage(response, JsonData.buildError("please login"));
        return false;
    }

    /**
     * 发送json格式的信息给前端
     *
     * @param response
     * @param obj
     * @throws IOException
     */
    private static void sendJsonMessage(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (null != writer) {
            writer.print(GSON.toJson(obj));
            response.flushBuffer();
            writer.close();
        }
    }


}
