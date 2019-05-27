package top.timebook.xdonlineeducationproject.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.timebook.xdonlineeducationproject.domain.JsonData;

/**
 * @ClassName CustomExceptionHandler
 * @Description 异常处理控制器
 * @Author xiongzl
 * @Date 2019/5/16 12:33
 * @Version 1.0
 **/

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public JsonData handler(Exception e) {
        if (e instanceof CustomException) {
            CustomException customException = (CustomException) e;
            return JsonData.buildError(customException.getMsg(), customException.getCode());
        } else {
            return JsonData.buildError("全局异常, 未知错误");
        }
    }

}
