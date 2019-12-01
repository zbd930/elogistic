package com.duogesi.controller;

import com.duogesi.entities.pic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RequestMapping("/upload/")
@Controller
public class uploadservlet {
    

    @RequestMapping(value = "upload.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView uploadPicture(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
        String order=request.getParameter("id");
        //裁剪用户id
        String originalFirstName = multipartFile.getOriginalFilename();
        String picFirstName = originalFirstName.substring(0, originalFirstName.indexOf("."));

        //取得图片的格式后缀
//        String originalLastName = multipartFile.getOriginalFilename();
//        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));

        //拼接：名字+时间戳+后缀
        String picName = order+".jpg";
        copy(multipartFile, realPath, picName);
        return null;
    }
    @RequestMapping(value = "upload1.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView uploadPicture1(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
        String order=request.getParameter("id");
        //裁剪用户id
        String originalFirstName = multipartFile.getOriginalFilename();
        String picFirstName = originalFirstName.substring(0, originalFirstName.indexOf("."));

        //取得图片的格式后缀
//        String originalLastName = multipartFile.getOriginalFilename();
//        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));

        //拼接：名字+时间戳+后缀
        String picName = order+".jpg";
        copy(multipartFile, realPath, picName);
        return null;
    }
    @RequestMapping(value = "upload2.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView uploadPicture2(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
        String order=request.getParameter("id");
        //裁剪用户id
        String originalFirstName = multipartFile.getOriginalFilename();
        String picFirstName = originalFirstName.substring(0, originalFirstName.indexOf("."));

        //取得图片的格式后缀
//        String originalLastName = multipartFile.getOriginalFilename();
//        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));

        //拼接：名字+时间戳+后缀
        String picName = order+".jpg";
        copy(multipartFile, realPath, picName);
        return null;
    }

    @RequestMapping(value = "upload3.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView uploadPicture3(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
        String order=request.getParameter("id");
        //裁剪用户id
        String originalFirstName = multipartFile.getOriginalFilename();
        String picFirstName = originalFirstName.substring(0, originalFirstName.indexOf("."));

        //取得图片的格式后缀
//        String originalLastName = multipartFile.getOriginalFilename();
//        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));

        //拼接：名字+时间戳+后缀
        String picName = order+".jpg";
        copy(multipartFile, realPath, picName);
        return null;
    }
    private Boolean copy(MultipartFile multipartFile, String realPath, String picName) {
        try {
            File dir = new File(realPath);
            //如果文件目录不存在，创建文件目录
            if (!dir.exists()) {
                dir.mkdir();
                System.out.println("创建文件目录成功：" + realPath);
            }
            File file = new File(realPath, picName);
            multipartFile.transferTo(file);
            System.out.println("添加图片成功！");
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
