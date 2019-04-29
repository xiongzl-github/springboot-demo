package top.timebook.xdonlineeducationproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.timebook.xdonlineeducationproject.config.WeChatConfig;
import top.timebook.xdonlineeducationproject.mapper.VideoMapper;

@RestController
public class TestController {

    @Autowired
    private WeChatConfig weChatConfig;

    @GetMapping("/test")
    public Object test() {
        return "hello world!";
    }


    @GetMapping("/test_config")
    public String testConfig() {
        return weChatConfig.getAppId() + ", " + weChatConfig.getAppSecret() + ".";
    }

    @Autowired
    private VideoMapper videoMapper;

    @GetMapping("/test_db")
    public Object testDB(){
        return videoMapper.findAll();
    }
}
