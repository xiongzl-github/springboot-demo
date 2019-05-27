package top.timebook.xdonlineeducationproject.mapper;

import org.apache.ibatis.annotations.*;
import top.timebook.xdonlineeducationproject.domain.VideoOrder;

import java.util.List;

/**
 * @ClassName VideoOrderMapper
 * @Description 订单mapper
 * @Author xiongzl
 * @Date 2019/5/10 12:06
 * @Version 1.0
 **/
public interface VideoOrderMapper {


    /**
     * 保存订单
     *
     * @param videoOrder
     * @return
     */
    @Insert("INSERT INTO video_order (openid, out_trade_no, state, create_time, notify_time, total_fee, nickname, head_img, video_id, video_title, video_img, user_id, ip, del) VALUES (#{openid}, #{outTradeNo}, #{state}, #{createTime}, #{notifyTime}, #{totalFee}, #{nickname}, #{headImg}, #{videoId}, #{videoTitle}, #{videoImg}, #{userId}, #{ip}, #{del});")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insert(VideoOrder videoOrder);

    /**
     * 根据order_id查找订单
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM video_order WHERE id = #{order_id} AND del = 0")
    VideoOrder findById(@Param("order_id") int id);

    /**
     * 根据outTradeNo查找订单
     *
     * @param outTradeNo
     * @return
     */
    @Select("SELECT * FROM video_order WHERE out_trade_no = #{out_trade_no} AND del = 0")
    VideoOrder findByOutTradeNo(@Param("out_trade_no") String outTradeNo);


    /**
     * 删除订单
     *
     * @param id
     * @param userId
     * @return
     */
    @Update("UPDATE video_order SET del = 1 WHERE id = #{id} AND user_id = #{userId}")
    int del(@Param("id") int id, @Param("userId") int userId);

    /**
     * @Author xiongzl
     * @Description 查询用户所有订单
     * @Date 2019/5/13 14:35
     * @Param [userId]
     * @Return java.util.List<top.timebook.xdonlineeducationproject.domain.VideoOrder>
     **/
    @Select("SELECT * FROM video_order WHERE user_id = #{userId} AND del = 0")
    List<VideoOrder> findMyOrderList(@Param("userId") int userId);

    /**
     * 根据out_trade_no 更新订单
     *
     * @param videoOrder
     * @return
     */
    @Update("UPDATE video_order SET state = #{state}, notify_time = #{notifyTime}, openid = #{openid} WHERE out_trade_no = #{outTradeNo} AND state = 0 AND del = 0")
    int updateVideoOrderByOutTradeNo(VideoOrder videoOrder);


}
