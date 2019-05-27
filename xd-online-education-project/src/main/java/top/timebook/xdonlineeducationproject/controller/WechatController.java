package top.timebook.xdonlineeducationproject.controller;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.timebook.xdonlineeducationproject.config.WeChatConfig;
import top.timebook.xdonlineeducationproject.domain.JsonData;
import top.timebook.xdonlineeducationproject.domain.User;
import top.timebook.xdonlineeducationproject.domain.VideoOrder;
import top.timebook.xdonlineeducationproject.service.UserService;
import top.timebook.xdonlineeducationproject.service.VideoOrderService;
import top.timebook.xdonlineeducationproject.utils.JwtUtils;
import top.timebook.xdonlineeducationproject.utils.WXPayUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

/**
 * @ClassName WechatController
 * @Description 微信控制器
 * @Author xiongzl
 * @Date 2019/5/6 11:36
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1/wechat")
public class WechatController {

    private WeChatConfig weChatConfig;

    private UserService userService;

    private VideoOrderService videoOrderService;

    private static final String SUCCESS = "SUCCESS";

    private static final String RESULT_CODE = "result_code";

    @Autowired
    public WechatController(WeChatConfig weChatConfig, UserService userService, VideoOrderService videoOrderService) {
        this.weChatConfig = weChatConfig;
        this.userService = userService;
        this.videoOrderService = videoOrderService;
    }

    /**
     * @return top.timebook.xdonlineeducationproject.domain.JsonData
     * @Author xiongzl
     * @Description 拼装微信扫一扫登陆url
     * @Date 2019/5/6 11:53
     * @Param [accessPage]
     **/
    @GetMapping("/login_url")
    public JsonData loginUrl(@RequestParam(value = "access_page") String accessPage) throws UnsupportedEncodingException {
        // 获取微信开放平台重定向地址
        String redirectUrl = weChatConfig.getOpenRedirectUrl();
        // 对重定向地址进行编码
        String callbackUrl = URLEncoder.encode(redirectUrl, "GBK");
        // 拼装url
        String qrcodeUrl = String.format(WeChatConfig.OPEN_QRCODE_URL, weChatConfig.getOpenAppId(), callbackUrl, accessPage);
        return JsonData.buildSuccess(qrcodeUrl);
    }

    /**
     * @Author xiongzl
     * @Description 微信回调接口
     * @Date 2019/5/10 15:09
     * @Param [code, state, response]
     * @Return void
     **/
    @GetMapping("/user/callback")
    public void wechatUserCallback(@RequestParam(value = "code") String code, @RequestParam(value = "state") String state, HttpServletResponse response) throws IOException {
        User user = userService.saveWechatUser(code);
        System.out.println("user: ");
        System.out.println(user);
        if (null != user) {
            // 生成jwt
            String token = JwtUtils.geneJsonWebToken(user);
            // state: 表示的是用户的页面地址, 需要拼接http://, 不然会误以为是站内地址
            String redirectUrl = state + "?token=" + token +
                    "&head_img=" + user.getHeadImg() +
                    "&name=" + URLEncoder.encode(user.getName(),"UTF-8");
            response.sendRedirect(redirectUrl);
        }
    }

    @PostMapping("/order/callback")
    public void orderCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 接受微信传递过来的数据, xml格式
        String returnMsg = WXPayUtil.getMessage(request);
        Map<String, String> callbackMap = WXPayUtil.xmlToMap(returnMsg);
        // 校验签名是否一致
        if (checkSign(callbackMap)) {
            // 更新数据库支付信息, 并通知微信处理结果
            updateVideoOrderPayInfo(callbackMap, response);
        } else {
            WXPayUtil.responseResultInfo(response, 0);
        }
    }

    /**
     * @Author xiongzl
     * @Description 更新数据库支付信息, 并通知微信处理结果
     * @Date 2019/5/15 18:16 
     * @Param [callbackMap, response]
     * @Return void
     **/
    private void updateVideoOrderPayInfo(Map<String, String> callbackMap, HttpServletResponse response) throws IOException {
        if (SUCCESS.equals(callbackMap.get(RESULT_CODE))) {
            String outTradeNo = callbackMap.get("out_trade_no");
            VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(outTradeNo);
            if (null != dbVideoOrder && dbVideoOrder.getState() == 0) {
                VideoOrder videoOrder = new VideoOrder();
                videoOrder.setOpenid(callbackMap.get("appid"));
                videoOrder.setOutTradeNo(outTradeNo);
                videoOrder.setNotifyTime(new Date());
                videoOrder.setState(1);
                int rows = videoOrderService.updateVideoOrderByOutTradeNo(videoOrder);
                if (rows == 1) {
                    WXPayUtil.responseResultInfo(response, 1);
                    return;
                }
            }
        }
        WXPayUtil.responseResultInfo(response, 0);
    }

    private boolean checkSign(Map<String, String> callbackMap) {
        SortedMap<String, String> sortedMap = WXPayUtil.tranSortedMap(callbackMap);
        return WXPayUtil.isCorrectSign(sortedMap, weChatConfig.getKey());
    }


}
