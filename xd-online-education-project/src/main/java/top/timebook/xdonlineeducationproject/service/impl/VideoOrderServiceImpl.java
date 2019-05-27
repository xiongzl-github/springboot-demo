package top.timebook.xdonlineeducationproject.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.timebook.xdonlineeducationproject.config.WeChatConfig;
import top.timebook.xdonlineeducationproject.domain.User;
import top.timebook.xdonlineeducationproject.domain.Video;
import top.timebook.xdonlineeducationproject.domain.VideoOrder;
import top.timebook.xdonlineeducationproject.dto.VideoOrderDto;
import top.timebook.xdonlineeducationproject.mapper.UserMapper;
import top.timebook.xdonlineeducationproject.mapper.VideoMapper;
import top.timebook.xdonlineeducationproject.mapper.VideoOrderMapper;
import top.timebook.xdonlineeducationproject.service.VideoOrderService;
import top.timebook.xdonlineeducationproject.utils.CommonUtils;
import top.timebook.xdonlineeducationproject.utils.HttpUtils;
import top.timebook.xdonlineeducationproject.utils.WXPayUtil;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @ClassName VideoOrderServiceImpl
 * @Description 订单实现类
 * @Author xiongzl
 * @Date 2019/5/10 18:34
 * @Version 1.0
 **/
@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    private final VideoOrderMapper videoOrderMapper;

    private final VideoMapper videoMapper;

    private final UserMapper userMapper;

    private final WeChatConfig weChatConfig;

    private static final int TIME_OUT = 5000;

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoOrderServiceImpl.class);

    /**
     * logger
     */
    private static final Logger DATALOGGER = LoggerFactory.getLogger("dataLogger");

    @Autowired
    public VideoOrderServiceImpl(VideoOrderMapper videoOrderMapper, VideoMapper videoMapper, UserMapper userMapper, WeChatConfig weChatConfig) {
        this.videoOrderMapper = videoOrderMapper;
        this.videoMapper = videoMapper;
        this.userMapper = userMapper;
        this.weChatConfig = weChatConfig;
    }

    /**
     * @param videoOrderDto
     * @Author xiongzl
     * @Description 下单接口
     * @Date 2019/5/10 18:33
     * @Param [videoOrderDto]
     * @Return top.timebook.xdonlineeducationproject.domain.VideoOrder
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public String save(VideoOrderDto videoOrderDto) throws Exception {
//        DATALOGGER.info("module=video_order`api=save`user_id={}`video_id={}", videoOrderDto.getUserId(), videoOrderDto.getVideoId());
//        DATALOGGER.error("module=video_order`api=save`user_id={}", videoOrderDto.getUserId());
//        LOGGER.error("module=video_order`api=save`user_id={}", videoOrderDto.getUserId());

        // 查找视频信息
        Video video = videoMapper.findById(videoOrderDto.getVideoId());
        // 查找用户信息
        User user = userMapper.findById(videoOrderDto.getUserId());
        // 生成订单
        VideoOrder videoOrder = createOrder(videoOrderDto, video, user);
        // 生成签名
        String payXml = createSign(videoOrder);
        // 统一下单
        return unifiedOrder(payXml);
    }

    /**
     * @param outTradeNo
     * @Author xiongzl
     * @Description 根据流水号查询订单
     * @Date 2019/5/15 17:12
     * @Param [outTradeNo]
     * @Return top.timebook.xdonlineeducationproject.domain.VideoOrder
     */
    @Override
    public VideoOrder findByOutTradeNo(String outTradeNo) {
        return videoOrderMapper.findByOutTradeNo(outTradeNo);
    }

    /**
     * @param videoOrder
     * @Author xiongzl
     * @Description 根据流水号更新订单
     * @Date 2019/5/15 17:12
     * @Param [videoOrder]
     * @Return int
     */
    @Override
    public int updateVideoOrderByOutTradeNo(VideoOrder videoOrder) {
        return videoOrderMapper.updateVideoOrderByOutTradeNo(videoOrder);
    }

    /**
     * @Author xiongzl
     * @Description 生成订单
     * @Date 2019/5/14 15:00
     * @Param [videoOrderDto, video, user]
     * @Return top.timebook.xdonlineeducationproject.domain.VideoOrder
     **/
    private VideoOrder createOrder(VideoOrderDto videoOrderDto, Video video, User user) {
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setVideoId(video.getId());
        videoOrder.setTotalFee(video.getPrice());
        videoOrder.setVideoImg(video.getCoverImg());
        videoOrder.setVideoTitle(video.getTitle());

        videoOrder.setUserId(user.getId());
        videoOrder.setHeadImg(user.getHeadImg());
        videoOrder.setNickname(user.getName());

        videoOrder.setDel(0);
        videoOrder.setCreateTime(new Date());
        videoOrder.setState(0);

        videoOrder.setOutTradeNo(CommonUtils.generateUUID());
        videoOrder.setIp(videoOrderDto.getIp());

        videoOrderMapper.insert(videoOrder);
        return videoOrder;
    }


    /**
     * @Author xiongzl
     * @Description 生成签名
     * @Date 2019/5/13 15:12
     * @Param [videoOrder]
     * @Return void
     **/
    private String createSign(VideoOrder videoOrder) throws Exception {
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", weChatConfig.getAppId());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str", CommonUtils.generateUUID());
        params.put("body", videoOrder.getVideoTitle());
        params.put("out_trade_no", videoOrder.getOutTradeNo());
        params.put("total_fee", videoOrder.getTotalFee().toString());
        params.put("spbill_create_ip", videoOrder.getIp());
        params.put("notify_url", weChatConfig.getPayCallBackurl());
        params.put("trade_type", "NATIVE");
        String sign = WXPayUtil.createSign(params, weChatConfig.getKey());
        params.put("sign", sign);
        return WXPayUtil.mapToXml(params);
    }


    /**
     * @Author xiongzl
     * @Description 统一下单方法
     * @Date 2019/5/13 15:00
     * @Param [videoOrder, payXml]
     * @Return java.lang.String
     **/
    private String unifiedOrder(String payXml) throws Exception {
        String orderStr = HttpUtils.doPost(WeChatConfig.UNIFIED_ORDER_URL, payXml, TIME_OUT);
        if (null == orderStr) {
            return null;
        }
        Map<String, String> unifiedOrderMap = WXPayUtil.xmlToMap(orderStr);
        if (unifiedOrderMap != null) {
            return unifiedOrderMap.get("code_url");
        }
        return null;
    }


}
