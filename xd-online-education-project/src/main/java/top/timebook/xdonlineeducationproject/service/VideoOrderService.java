package top.timebook.xdonlineeducationproject.service;

import top.timebook.xdonlineeducationproject.domain.VideoOrder;
import top.timebook.xdonlineeducationproject.dto.VideoOrderDto;

/**
 * @InterfaceName VideoOrderService
 * @Description 订单接口
 * @Author xiongzl
 * @Date 2019/5/10 18:26
 * @Version 1.0
 **/
public interface VideoOrderService {

    /**
     * @Author xiongzl
     * @Description 下单接口
     * @Date 2019/5/10 18:33
     * @Param [videoOrderDto]
     * @Return top.timebook.xdonlineeducationproject.domain.VideoOrder
     **/
    String save(VideoOrderDto videoOrderDto) throws Exception;


    /**
     * @Author xiongzl
     * @Description 根据流水号查询订单
     * @Date 2019/5/15 17:12
     * @Param [outTradeNo]
     * @Return top.timebook.xdonlineeducationproject.domain.VideoOrder
     **/
    VideoOrder findByOutTradeNo(String outTradeNo);

    /**
     * @Author xiongzl
     * @Description 根据流水号更新订单
     * @Date 2019/5/15 17:12
     * @Param [videoOrder]
     * @Return int
     **/
    int updateVideoOrderByOutTradeNo(VideoOrder videoOrder);






}
