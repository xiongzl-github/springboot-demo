package top.timebook.xdonlineeducationproject.exception;

/**
 * The type Custom exception.
 *
 * @ClassName CustomException
 * @Description 自定义异常
 * @Author xiongzl
 * @Date 2019 /5/16 12:23
 * @Version 1.0
 */
public class CustomException extends RuntimeException {

    /**
     * 状态码
     **/
    private Integer code;
    /**
     * 异常消息
     **/
    private String msg;

    public CustomException(){}

    public CustomException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
