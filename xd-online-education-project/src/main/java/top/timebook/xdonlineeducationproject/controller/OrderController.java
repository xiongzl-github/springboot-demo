package top.timebook.xdonlineeducationproject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.timebook.xdonlineeducationproject.dto.VideoOrderDto;
import top.timebook.xdonlineeducationproject.service.VideoOrderService;
import top.timebook.xdonlineeducationproject.utils.IpUtils;
import top.timebook.xdonlineeducationproject.utils.QRCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName OrderController
 * @Description 订单控制类
 * @Author xiongzl
 * @Date 2019/5/8 19:11
 * @Version 1.0
 **/

@RestController
@RequestMapping("/user/api/v1/order")
public class OrderController {

    private final VideoOrderService videoOrderService;

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    /**
     * logger
     */
    private static final Logger DATALOGGER = LoggerFactory.getLogger("dataLogger");

    @Autowired
    public OrderController(VideoOrderService videoOrderService) {
        this.videoOrderService = videoOrderService;
    }


    /**
     * @Author xiongzl
     * @Description 添加一个订单
     * @Date 2019/5/16 2:49
     * @Param [videoId, request, response]
     * @Return void
     **/
    @GetMapping("/add")
    public void saveOrder(@RequestParam(value = "video_id") int videoId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取ip地址
        String ip = IpUtils.getIpAddr(request);
        int userId = (int) request.getAttribute("user_id");
        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setUserId(userId);
        videoOrderDto.setVideoId(videoId);
        videoOrderDto.setIp(ip);
        String codeUrl = videoOrderService.save(videoOrderDto);
        if (codeUrl == null) {
            throw new Exception();
        }
        // 生成二维码
        QRCodeUtil.generateQRCode(codeUrl, response);
    }


}
