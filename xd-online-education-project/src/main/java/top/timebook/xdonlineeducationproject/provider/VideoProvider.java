package top.timebook.xdonlineeducationproject.provider;

import org.apache.ibatis.jdbc.SQL;
import top.timebook.xdonlineeducationproject.domain.Video;

/**
 * video构建动态sql语句
 */
public class VideoProvider {

    /**
     * 更新video动态语句
     * @param video
     * @return
     */
    public String updateVideo(final Video video){
        return new SQL(){{
            UPDATE("video");
            // 条件写法
            if (video.getCoverImg() != null) {
                SET("cover_img = #{coverImg}");
            }
            if (video.getCreateTime() != null) {
                SET("createTime = #{createTime}");
            }
            if (video.getOnline() != null) {
                SET("online = #{online}");
            }
            if (video.getPoint() != null) {
                SET("point = #{point}");
            }
            if (video.getPrice() != null) {
                SET("price = #{price}");
            }
            if (video.getSummary() != null) {
                SET("summary = #{summary}");
            }
            if (video.getTitle() != null) {
                SET("title = #{title}");
            }
            if (video.getViewNum() != null) {
                SET("view_num = #{viewNum}");
            }
            WHERE("id = #{id}");
        }}.toString();
    }




}
