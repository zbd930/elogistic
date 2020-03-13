package com.duogesi.Mail;

import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

@Component
public class Mymail {
    // 发送邮件的账号
    public static String ownEmailAccount = "jemmy_ywt@163.com";
    // 发送邮件的密码------》授权码
    public static String ownEmailPassword = "Zbd7895123";
    // 发送邮件的smtp 服务器 地址
    public static String myEmailSMTPHost = "smtp.163.com";
    //防止附件名称过长，变成.bin
    static {
        System.setProperty("mail.mime.splitlongparameters", "false");
        System.setProperty("mail.mime.charset", "UTF-8");
    }

    public  void send(String receiveMailAccount,String context,String title) throws Exception {
        Properties prop = new Properties();
        // 设置邮件传输采用的协议smtp
        prop.setProperty("mail.transport.protocol", "smtp");
        // 设置发送人邮件服务器的smtp地址
        // 这里以网易的邮箱smtp服务器地址为例
        prop.setProperty("mail.smtp.host", myEmailSMTPHost);
        // 设置验证机制
        prop.setProperty("mail.smtp.auth", "true");

        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        // 需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        // QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)

        final String smtpPort = "465";
        prop.setProperty("mail.smtp.port", smtpPort);
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        prop.setProperty("mail.smtp.socketFactory.port", smtpPort);

        // 创建对象回话跟服务器交互
        Session session = Session.getInstance(prop);
        // 会话采用debug模式
        session.setDebug(true);
        // 创建邮件对象
        Message message = createSimpleMail(session,receiveMailAccount,context,title);

        Transport trans = session.getTransport();
        // 链接邮件服务器
        trans.connect(ownEmailAccount, ownEmailPassword);
        // 发送信息
        trans.sendMessage(message, message.getAllRecipients());
        // 关闭链接
        trans.close();
    }

    public  Message createSimpleMail(Session session, String receiveMailAccount, String context, String title) throws Exception {
        MimeMessage message = new MimeMessage(session);
        // 设置发送邮件地址,param1 代表发送地址 param2 代表发送的名称(任意的) param3 代表名称编码方式
        message.setFrom(new InternetAddress("jemmy_ywt@163.com", "Shenzhen Yiwutong Technology", "utf-8"));
        // 代表收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMailAccount, "发货人", "utf-8"));
        // To: 增加收件人（可选）
//        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("dd@receive.com", "USER_DD", "UTF-8"));
        // Cc: 抄送（可选）
//        if (ccemails.size()!=0) {
//            for (String s : ccemails) {
//                message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(s, "USER_EE", "UTF-8"));
//            }
//        }
        // Bcc: 密送（可选）
//        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("ff@receive.com", "USER_FF", "UTF-8"));

        // 设置邮件主题
        message.setSubject(title);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(context+"<br><br><br>Shenzhen Yiwutong Technology<br>" +
                "<br>" +
                "Shenzhen Yiwutong Technology Co., Ltd. <br>" +
                "<br>" +
                "Shenzhen Yiwutong Technology<br>" +
                "<br>" +
                "Tel:（86）18811876912");
        // 设置邮件内容
        message.setContent(String.valueOf(stringBuilder), "text/html;charset=utf-8");
        // 设置发送时间
        message.setSentDate(Calendar.getInstance().getTime());
        // 保存上面的编辑内容
        message.saveChanges();
        // 将上面创建的对象写入本地
        OutputStream out = new FileOutputStream("MyEmail.eml");
        message.writeTo(out);
        out.flush();
        out.close();
        return message;

    }


    //多个附件
    public void sendMail2(String[] realPath,String receiveMailAccount,String context,String subject)throws Exception{
        System.out.println("sendMailServlet-----start3");
        //1.创建邮件对象
        Properties prop = new Properties();
        // 设置邮件传输采用的协议smtp
        prop.setProperty("mail.transport.protocol", "smtp");
        // 设置发送人邮件服务器的smtp地址
        // 这里以网易的邮箱smtp服务器地址为例
        prop.setProperty("mail.smtp.host", myEmailSMTPHost);
        // 设置验证机制
        prop.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(prop);
        MimeMessage message =new MimeMessage(session);

        /*2.设置发件人
         * 其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
         * */
        message.setFrom(new InternetAddress("jemmy_ywt@163.com", "Shenzhen Yiwutong Technology", "utf-8"));
        // 代表收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMailAccount, "发货人", "utf-8"));

        /*4.设置标题*/
        message.setSubject(subject,"UTF-8");
        //message.setContent("Test Content:这是一封测试邮件...","text/html;charset=UTF-8");

        /*5.设置邮件正文*/

        //一个Multipart对象包含一个或多个bodypart对象，组成邮件正文
        MimeMultipart multipart = new MimeMultipart();
        //读取本地图片,将图片数据添加到"节点"

        //创建文本节点
        MimeBodyPart text = new MimeBodyPart();
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(context+"<br><br><br>Shenzhen Yiwutong Technology<br>" +
                "<br>" +
                "Shenzhen Yiwutong Technology Co., Ltd. <br>" +
                "<br>" +
                "Shenzhen Yiwutong Technology<br>" +
                "<br>" +
                "Tel:（86）18811876912");
        text.setContent(String.valueOf(stringBuilder),"text/html;charset=UTF-8");
        //将文本和图片添加到multipart
        multipart.addBodyPart(text);
        for (int i = 0; i < realPath.length; i++) {
            MimeBodyPart image = new MimeBodyPart();
            FileDataSource dataSource=new FileDataSource(realPath[i]);
            DataHandler dataHandler1 = new DataHandler(dataSource);
            image.setDataHandler(dataHandler1);
            multipart.addBodyPart(image);
        }
        multipart.setSubType("related");//关联关系
        message.setContent(multipart);
        message.setSentDate(Calendar.getInstance().getTime());
        message.saveChanges();
        Transport transport = session.getTransport("smtp");
        transport.connect(ownEmailAccount,ownEmailPassword);
        transport.sendMessage(message,message.getAllRecipients());
        transport.close();

        System.out.println("sendMailServlet-----end3");
    }

    //更新数据
    public void sendMail3(String realPath,String receiveMailAccount,String context,String subject)throws Exception{

        System.out.println("sendMailServlet-----start3");

        //1.创建邮件对象
        Properties prop = new Properties();
        // 设置邮件传输采用的协议smtp
        prop.setProperty("mail.transport.protocol", "smtp");
        // 设置发送人邮件服务器的smtp地址
        // 这里以网易的邮箱smtp服务器地址为例
        prop.setProperty("mail.smtp.host", myEmailSMTPHost);
        // 设置验证机制
        prop.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(prop);
        MimeMessage message =new MimeMessage(session);

        /*2.设置发件人
         * 其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
         * */
        message.setFrom(new InternetAddress("jemmy_ywt@163.com", "易物通科技有限公司", "utf-8"));
        // 代表收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMailAccount, "发货人", "utf-8"));

        /*4.设置标题*/
        message.setSubject(subject,"UTF-8");
        //message.setContent("Test Content:这是一封测试邮件...","text/html;charset=UTF-8");

        /*5.设置邮件正文*/

        //一个Multipart对象包含一个或多个bodypart对象，组成邮件正文
        MimeMultipart multipart = new MimeMultipart();
        //读取本地图片,将图片数据添加到"节点"
        MimeBodyPart image = new MimeBodyPart();
        System.out.println(realPath);
        System.out.println(realPath);
        System.out.println(realPath);

        DataHandler dataHandler1 = new DataHandler(new FileDataSource(realPath));
        image.setDataHandler(dataHandler1);

        //创建文本节点
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(context,"text/html;charset=UTF-8");

        //将文本和图片添加到multipart
        multipart.addBodyPart(text);
        multipart.addBodyPart(image);
        multipart.setSubType("related");//关联关系

        message.setContent(multipart);

        message.setSentDate(Calendar.getInstance().getTime());
        message.saveChanges();
        Transport transport = session.getTransport("smtp");
        transport.connect(ownEmailAccount,ownEmailPassword);
        transport.sendMessage(message,message.getAllRecipients());
        transport.close();

        System.out.println("sendMailServlet-----end3");
    }
}
