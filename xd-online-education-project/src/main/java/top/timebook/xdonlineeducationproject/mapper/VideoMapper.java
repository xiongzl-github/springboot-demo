package top.timebook.xdonlineeducationproject.mapper;

import org.apache.ibatis.annotations.*;
import top.timebook.xdonlineeducationproject.domain.Video;
import top.timebook.xdonlineeducationproject.provider.VideoProvider;

import java.util.List;

public interface VideoMapper {

    @Select("SELECT * FROM video")
//    @Results({
//            @Result(column = "cover_img", property = "coverImg"),
//            @Result(column = "view_num", property = "viewNum")
//    })
    List<Video> findAll();

    @Select("SELECT * FROM video WHERE id = #{id}")
    Video findById(int id);

//    @Update("UPDATE video SET title = #{title} WHERE id = #{id}")
    @UpdateProvider(type = VideoProvider.class, method = "updateVideo")
    int update(Video video);

    @Delete("DELETE FROM video WHERE id = #{id}")
    int delete(int id);

    @Insert("INSERT INTO video (`title`, `summary`, `cover_img`, `view_num`, `price`, `create_time`, `online`, `point`) VALUES (#{title}, #{summary}, #{coverImg}, #{viewNum}, #{price}, #{createTime}, #{online}, #{point});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(Video video);



}
