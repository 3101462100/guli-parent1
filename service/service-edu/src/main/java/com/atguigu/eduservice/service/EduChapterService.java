package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-05-02
 */
public interface EduChapterService extends IService<EduChapter> {
    //根据课程id查询章节小节
    List<ChapterVo> getChapterByVideoId(String courseId);

    //删除章节
    boolean deleteChapter(String chapterId);
    //2 根据课程id删除章节
    void removeChapterByCourseId(String courseId);
}
