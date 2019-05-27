package top.timebook.xdonlineeducationproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.timebook.xdonlineeducationproject.config.WeChatConfig;
import top.timebook.xdonlineeducationproject.domain.JsonData;
import top.timebook.xdonlineeducationproject.mapper.VideoMapper;

/**
 * @Author xiongzl
 * @Description 测试控制类
 * @Date 2019/5/16 2:51
 * @Param
 * @Return
 **/

@RestController
public class TestController {

    private final WeChatConfig weChatConfig;

    private final VideoMapper videoMapper;

    @Autowired
    public TestController(WeChatConfig weChatConfig, VideoMapper videoMapper) {
        this.weChatConfig = weChatConfig;
        this.videoMapper = videoMapper;
    }

    @GetMapping("/test")
    public Object test() {
        return "hello world!";
    }


    @GetMapping("/test_config")
    public String testConfig() {
        System.out.println(weChatConfig.getOpenAppsecret());
        System.out.println(weChatConfig.getOpenAppId());
        System.out.println(weChatConfig.getOpenRedirectUrl());
        return weChatConfig.getAppId() + ", " + weChatConfig.getAppSecret() + ".";
    }

    @GetMapping("/test_db")
    public Object testDB(){
        return videoMapper.findAll();
    }

    @GetMapping("/get_json_data")
    public JsonData testJsonData(){
        return JsonData.buildSuccess(weChatConfig.getAppId(), "测试JsonData成功!");
    }
}
