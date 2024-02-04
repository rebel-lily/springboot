package com.sqc.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqc.springboot.common.Result;
import com.sqc.springboot.entity.Files;
import com.sqc.springboot.entity.User;
import com.sqc.springboot.mapper.FilesMapper;
import com.sqc.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @ClassName FileController
 * @Description
 * @Author Administrator
 * @Date 2023/11/23 8:56
 * @Version V1.0
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FilesMapper filesMapper;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        String uuid = IdUtil.fastSimpleUUID();//定义一个文件唯一标识符
        String fileUuid = uuid + StrUtil.DOT + type;


        File uploadFile = new File(fileUploadPath + fileUuid);
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()){//判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
            parentFile.mkdir();
        }

        String url;
        String md5;
        file.transferTo(uploadFile);//上传文件到磁盘
        md5 = SecureUtil.md5(uploadFile); //获取文件md5
        Files dbFiles = getFileMd5(md5);//从数据库查询是否存在相同的记录
        if (dbFiles != null){
            url = dbFiles.getUrl();

            uploadFile.delete(); //由于文件已存在，所以删除刚才上传的重复文件并不上传到数据库
        }else {

            url = "http://localhost:9090/file/" + fileUuid;//数据库若不存在重复文件，则不删除刚才上传的文件
        }

        Files saveFile = new Files();//存储数据库
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        filesMapper.insert(saveFile);

        return url;
    }

    @GetMapping("/{fileUuid}")
    public void download(@PathVariable String fileUuid, HttpServletResponse response) throws IOException{
        File uploadFile = new File(fileUploadPath + fileUuid);//根据文件的唯一标识获取文件
        ServletOutputStream os = response.getOutputStream();//设置输出流格式
        response.addHeader("Content-Disposition","attachment:filename=" + URLEncoder.encode(fileUuid,"UTF-8"));
        response.setContentType("application/octet-stream");

        os.write(FileUtil.readBytes(uploadFile));//读取文件字节流
        os.flush();
        os.close();
    }

    private Files getFileMd5(String md5){//文件的md5查询文件
        //查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<Files> filesList = filesMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    @GetMapping("/page")//分页查询
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",false);
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)){
            queryWrapper.like("name",name);
        }

        User currentUser = TokenUtils.getCurrentUser();
        System.out.println("获取用户信息=============" + currentUser.getNickname());
        return Result.success(filesMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files = filesMapper.selectById(id);
        files.setIsDelete(true);
        return Result.success(filesMapper.updateById(files));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        //select * from sys_file where id in (id,id,id……)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<Files> files = filesMapper.selectList(queryWrapper);
        for (Files file : files){
            file.setIsDelete(true);
            filesMapper.updateById(file);
        }
        return Result.success();
    }

    //新增和修改
    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.success(filesMapper.updateById(files));
    }
}
