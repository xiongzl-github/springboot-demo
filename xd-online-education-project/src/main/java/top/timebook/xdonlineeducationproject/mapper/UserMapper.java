package top.timebook.xdonlineeducationproject.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.timebook.xdonlineeducationproject.domain.User;

/**
 * @ClassName UserMapper
 * @Description 用户Mapper
 * @Author xiongzl
 * @Date 2019/5/8 11:04
 * @Version 1.0
 **/
public interface UserMapper {

    /**
     * @Author xiongzl
     * @Description 根据用户id查询User
     * @Date 2019/5/8 11:52
     * @Param [userId]
     * @Return top.timebook.xdonlineeducationproject.domain.User
     **/
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id")int userId);

    /**
     * @Author xiongzl
     * @Description 根据openid查找用户
     * @Date 2019/5/8 12:00
     * @Param [openid]
     * @Return top.timebook.xdonlineeducationproject.domain.User
     **/
    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User findByOpendid(@Param("openid")String openid);


    /**
     * @Author xiongzl
     * @Description 保存用户
     * @Date 2019/5/8 14:44
     * @Param [user]
     * @Return int
     **/
    @Insert("INSERT INTO user (openid, name, head_img, phone, sign, sex, city, create_time) VALUES (#{openid}, #{name}, #{headImg}, #{phone}, #{sign}, #{sex}, #{city}, #{createTime});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(User user);




}
