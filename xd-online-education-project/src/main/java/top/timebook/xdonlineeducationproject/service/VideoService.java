package top.timebook.xdonlineeducationproject.service;

import top.timebook.xdonlineeducationproject.domain.Video;

import java.util.List;

public interface VideoService {

    List<Video> findAll();

    Video findById(int id);

    int update(Video video);

    int delete(int id);

    int save(Video video);




}
