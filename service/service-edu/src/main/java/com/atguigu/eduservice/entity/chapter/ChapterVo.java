package com.atguigu.eduservice.entity.chapter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
//章节
public class ChapterVo {
    private String id;
    private String title;
    private List<VideoVo> children= new ArrayList<>();//表示小节
}
