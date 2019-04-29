package top.timebook.xdonlineeducationproject.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.timebook.xdonlineeducationproject.domain.Video;
import top.timebook.xdonlineeducationproject.service.VideoService;

@RestController
@RequestMapping("/admin/api/v1/video")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;

    /**
     * 更新视频
     *
     * @param video 视频实体类
     * @return
     */
    @PutMapping("/update_by_id")
    int update(@RequestBody Video video) {
        return videoService.update(video);
    }

    /**
     * 根据id删除视频
     *
     * @param videoId 视频id
     * @return
     */
    @DeleteMapping("/del_by_id")
    int delete(@RequestParam(value = "video_id", required = true) int videoId) {
        return videoService.delete(videoId);
    }

    /**
     * 保存视频对象
     *
     * @param video 视频实体类
     * @return
     */
    @PostMapping("/add_video")
    int save(@RequestBody Video video) {
        return videoService.save(video);
    }


}
