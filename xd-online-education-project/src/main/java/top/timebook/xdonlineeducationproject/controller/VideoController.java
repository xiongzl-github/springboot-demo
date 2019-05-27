package top.timebook.xdonlineeducationproject.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.timebook.xdonlineeducationproject.domain.Video;
import top.timebook.xdonlineeducationproject.service.VideoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xiongzl
 * @Description 视频控制类
 * @Date 2019/5/16 2:52
 * @Param
 * @Return
 **/

@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }


    /**
     * 分页接口
     *
     * @param page 当前第几页
     * @param size 每页显示几条数据
     * @return
     */
    @GetMapping("/page")
    Object pageVideo(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        List<Video> videos = videoService.findAll();
        PageInfo<Video> pageInfo = new PageInfo<>(videos);
        Map<String, Object> returnMap = new HashMap<>(10);
        returnMap.put("total_size", pageInfo.getTotal());
        returnMap.put("total_page", pageInfo.getPages());
        returnMap.put("current_page", page);
        returnMap.put("data", pageInfo.getList());
        return returnMap;
    }

    /**
     * 根据id找视频
     *
     * @param videoId 视频id
     * @return
     */
    @GetMapping("/find_by_id")
    Video findById(@RequestParam(value = "video_id") int videoId) {
        return videoService.findById(videoId);
    }


}
