package top.timebook.xdonlineeducationproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author xiongzl
 * @Description 微信配置类
 * @Date 2019/5/14 15:04
 **/
@Configuration
@PropertySource(value = "classpath:application.properties")
public class WeChatConfig {

    /**
     * 公众号appid
     */
    @Value("${wxpay.appid}")
    private String appId;

    /**
     * 公众号密钥
     */
    @Value("${wxpay.appsecret}")
    private String appSecret;

    /**
     * 微信开放平台id
     */
    @Value("${wxopen.appid}")
    private String openAppId;

    /**
     * 微信开放平台密钥
     */
    @Value("${wxopen.appsecret}")
    private String openAppsecret;

    /**
     * 微信开放平台回调url
     */
    @Value("${wxopen.redirect_url}")
    private String openRedirectUrl;

    /**
     * 商户号id
     **/
    @Value("${wxpay.mer_id}")
    private String mchId;

    /**
     * 微信支付的key
     **/
    @Value("${wxpay.key}")
    private String key;

    /**
     * 微信支付的回调url
     **/
    @Value("${wxpay.callback}")
    private String payCallBackurl;


    /**
     * 统一下单url
     **/
    public static final String UNIFIED_ORDER_URL = "http://api.xdclass.net/pay/unifiedorder";


    

    /**
     * 微信开放平台二维码连接
     */
    public final static String OPEN_QRCODE_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";

    /**
     * 微信开放平台获取用户access_token 地址
     */
    public final static String OPEN_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 微信开放平台获取用户基本信息地址
     */
    public final static String OPEN_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh-CN";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getOpenAppId() {
        return openAppId;
    }

    public void setOpenAppId(String openAppId) {
        this.openAppId = openAppId;
    }

    public String getOpenAppsecret() {
        return openAppsecret;
    }

    public void setOpenAppsecret(String openAppsecret) {
        this.openAppsecret = openAppsecret;
    }

    public String getOpenRedirectUrl() {
        return openRedirectUrl;
    }

    public void setOpenRedirectUrl(String openRedirectUrl) {
        this.openRedirectUrl = openRedirectUrl;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPayCallBackurl() {
        return payCallBackurl;
    }

    public void setPayCallBackurl(String payCallBackurl) {
        this.payCallBackurl = payCallBackurl;
    }
}
