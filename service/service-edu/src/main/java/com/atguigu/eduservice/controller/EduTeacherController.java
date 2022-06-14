package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-04-14
 */

@Api(tags="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {
    //注入service层
    @Autowired
    private EduTeacherService eduTeacherService;

    /*查询所有数据
    * rest风格
    * */
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAll(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    /*
    * 逻辑删除
    * */
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){
        boolean b = eduTeacherService.removeById(id);
        if(b){
           return R.ok();
        }else {
           return R.error();
        }
    }


    /*
    * 分页查询讲师方法
    * current  当前页
    * limit    每页记录数
    * */
    @ApiOperation(value = "分页查询讲师")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(
          @PathVariable  long current,
          @PathVariable  long limit
    ){
        //创建page对象
        Page<EduTeacher> page = new Page<>(current,limit);

      /*  try {
            int i=10/0;
        }catch (Exception e){
            throw new GuliException(2001,"执行了自定义异常");
        }*/

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        eduTeacherService.page(page,null);
        long total = page.getTotal();//获取总记录数
        List<EduTeacher> records = page.getRecords();//数据list集合

        Map map = new HashMap<>();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);
        /* return R.ok().data("total",total).data("rows",records);*/
    }


    /*
    * 分页条件查询
    * */
    @ApiOperation(value = "分页条件查询讲师")
    //PostMapping   才能用j格式提交
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R TeacherQuery(
            @PathVariable  long current,
            @PathVariable  long limit,
        @RequestBody(required = false)  TeacherQuery teacherQuery){
        //创建page对象
        Page<EduTeacher> teacherPage = new Page<>(current,limit);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        //判断条件是否为空
        if(!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
       if(!StringUtils.isEmpty(level)){
           wrapper.eq("level",level);
       }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_modified",end);
        }
        //排序
        wrapper.orderByDesc("gmt_create");

        //调用查询方法
        eduTeacherService.page(teacherPage,wrapper);

        long total = teacherPage.getTotal();//获取总记录数
        List<EduTeacher> records = teacherPage.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);

    }

    /*
    * 添加讲师
    * */
    @ApiOperation(value = "添加讲师")
    //添加讲师接口的方法
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if(save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /*
    * 根据id查询讲师
    * */
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable("id") String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    /*
    * 根据id修改讲师
    * */
    @ApiOperation(value = "修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean b = eduTeacherService.updateById(eduTeacher);
        if(b){
            return R.ok();
        }else {
            return R.error();
        }

    }
}

