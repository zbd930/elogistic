package com.duogesi.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/upload/")
@Controller
public class uploadservlet {


    private static Logger logger = Logger.getLogger(uploadservlet.class);

    //打包上传
    @RequestMapping(value = "file.do", method = {RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    public ModelAndView upfile(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, String order) throws IOException {
        mv.setViewName("uploadfile");
        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        //对应前端的upload的name参数"file"
        MultipartFile multipartFile = req.getFile("file");
        //取得图片的格式后缀
        String originalLastName = multipartFile.getOriginalFilename();
//        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));
        //拼接：名字+时间戳+后缀
        String packageName = order + originalLastName;
        String realPath = request.getServletContext().getRealPath("/files/");
        if (copy(multipartFile, realPath, packageName)) {
            logger.info("上传成功");
            mv.addObject("upload_result", "上传成功");
        } else {
            logger.error("上传失败");
            mv.addObject("upload_result", "上传失败");
        }
        return mv;
    }

    @RequestMapping(value = "upload.do", method = {RequestMethod.POST, RequestMethod.GET}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String uploadPicture(HttpServletRequest request, HttpServletResponse response) throws IOException {

        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;

        //对应前端的upload的name参数"file"
        MultipartFile multipartFile = req.getFile("file");
        //realPath填写电脑文件夹路径
//        String realPath = "D:\\work\\Project\\Logistic\\upload";
        String realPath = request.getServletContext().getRealPath("/fapiao/");
        //格式化时间戳
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//        Calendar cal = Calendar.getInstance();
//        String nowTime = sdf.format(cal);
        //获取订单号
        String order = request.getParameter("number");
        //裁剪用户id
        String originalFirstName = multipartFile.getOriginalFilename();
        String picFirstName = originalFirstName.substring(0, originalFirstName.indexOf("."));

        //取得图片的格式后缀
//        String originalLastName = multipartFile.getOriginalFilename();
//        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));

        //拼接：名字+时间戳+后缀
        String picName = order + ".jpg";
        if (copy(multipartFile, realPath, picName)) {
            return "上传成功";
        } else return "上传失败";
    }

    @RequestMapping(value = "upload1.do", method = {RequestMethod.POST, RequestMethod.GET}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String uploadPicture1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;

        //对应前端的upload的name参数"file"
        MultipartFile multipartFile = req.getFile("file");
        //realPath填写电脑文件夹路径
//        String realPath = "D:\\work\\Project\\Logistic\\upload";
        String realPath = request.getServletContext().getRealPath("/xiangdan/");
        //格式化时间戳
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//        Calendar cal = Calendar.getInstance();
//        String nowTime = sdf.format(cal);
        //获取订单号
        String order = request.getParameter("number");
        //裁剪用户id
        String originalFirstName = multipartFile.getOriginalFilename();
        String picFirstName = originalFirstName.substring(0, originalFirstName.indexOf("."));

        //取得图片的格式后缀
//        String originalLastName = multipartFile.getOriginalFilename();
//        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));

        //拼接：名字+时间戳+后缀
        String picName = order + ".jpg";
        if (copy(multipartFile, realPath, picName)) {
            return "上传成功";
        } else return "上传失败";
    }

    @RequestMapping(value = "upload2.do", method = {RequestMethod.POST, RequestMethod.GET}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String uploadPicture2(HttpServletRequest request, HttpServletResponse response) throws IOException {

        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;

        //对应前端的upload的name参数"file"
        MultipartFile multipartFile = req.getFile("file");
        //realPath填写电脑文件夹路径
//        String realPath = "D:\\work\\Project\\Logistic\\upload";
        String realPath = request.getServletContext().getRealPath("/baoguan/");
        //格式化时间戳
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//        Calendar cal = Calendar.getInstance();
//        String nowTime = sdf.format(cal);
        //获取订单号
        String order = request.getParameter("number");
        //裁剪用户id
        String originalFirstName = multipartFile.getOriginalFilename();
        String picFirstName = originalFirstName.substring(0, originalFirstName.indexOf("."));

        //取得图片的格式后缀
//        String originalLastName = multipartFile.getOriginalFilename();
//        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));

        //拼接：名字+时间戳+后缀
        String picName = order + ".jpg";
        if (copy(multipartFile, realPath, picName)) {
            return "上传成功";
        } else return "上传失败";
    }

    @RequestMapping(value = "upload3.do", method = {RequestMethod.POST, RequestMethod.GET}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String uploadPicture3(HttpServletRequest request, HttpServletResponse response) throws IOException {

        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;

        //对应前端的upload的name参数"file"
        MultipartFile multipartFile = req.getFile("file");
        //realPath填写电脑文件夹路径
//        String realPath = "D:\\work\\Project\\Logistic\\upload";
        String realPath = request.getServletContext().getRealPath("/hetong/");
        //格式化时间戳
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//        Calendar cal = Calendar.getInstance();
//        String nowTime = sdf.format(cal);
        //获取订单号
        String order = request.getParameter("number");
        //裁剪用户id
        String originalFirstName = multipartFile.getOriginalFilename();
        String picFirstName = originalFirstName.substring(0, originalFirstName.indexOf("."));

        //取得图片的格式后缀
//        String originalLastName = multipartFile.getOriginalFilename();
//        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));

        //拼接：名字+时间戳+后缀
        String picName = order + ".jpg";
        if (copy(multipartFile, realPath, picName)) {
            return "上传成功";
        } else return "上传失败";
    }

    private Boolean copy(MultipartFile multipartFile, String realPath, String picName) {
        try {
            File dir = new File(realPath);
            //如果文件目录不存在，创建文件目录
            if (!dir.exists()) {
                dir.mkdir();
                logger.info("创建文件目录成功：" + realPath);
            }
            File file = new File(realPath, picName);
            if (file.exists()) {
                logger.error("文件已存在");
                return false;
            }
            multipartFile.transferTo(file);
            logger.info("添加图片成功！");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        }

    }

}
