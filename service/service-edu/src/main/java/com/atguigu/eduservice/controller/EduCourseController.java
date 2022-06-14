package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-02
 */
@RestController
@Api(tags = "课程")
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;



    //条件分页查询课程信息
    @PostMapping("pageCourse/{current}/{limit}")
    public R TeacherQuery(
            @PathVariable  long current,
            @PathVariable  long limit,
            @RequestBody(required = false) EduCourse eduCourse){
        //创建page对象
        Page<EduCourse> Coursepage = new Page<>(current,limit);

        //构建条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        String title = eduCourse.getTitle();
        String status = eduCourse.getStatus();

        //判断条件是否为空
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }

        //调用查询方法
        eduCourseService.page(Coursepage,wrapper);

        long total = Coursepage.getTotal();//获取总记录数
        List<EduCourse> records = Coursepage.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);

    }

    //查询
    @GetMapping
    public R getCourseList() {
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list",list);
    }

    //添加课程信息
    @PostMapping("addCourseInfo")
    public R addeduCourse(@RequestBody CourseInfoVo courseInfoVo){
        //返回添加之后课程id 为了后面添加大纲使用
       String id =  eduCourseService.savaCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    //根据id查询课程信息
    @GetMapping("getCourseInfo/{courseId}")
        public R getCourseInfo(@PathVariable String courseId){
            CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
            return R.ok().data("courseInfoVo",courseInfoVo);
        }

        //修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }
    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = eduCourseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }
    //课程最终发布
    @PostMapping("pageCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }
    //删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId) {
        eduCourseService.removeCourse(courseId);
        return R.ok();
    }
}

