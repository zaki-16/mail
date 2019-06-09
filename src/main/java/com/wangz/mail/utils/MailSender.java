package com.wangz.mail.utils;

import org.apache.commons.lang3.StringUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

/**
 * @ClassName MailSender
 * @Auther: wz1016_vip@163.com
 * @Date: 2019/6/9 10:31
 * @Description: TODO 使用163.com 提供邮件发送服务 --- 需要开启 POP3/IMAP/SMTP服务！
 */
public class MailSender {
    //发件人地址
    private static String SENDER_ADDRESS = "";
    //收件人地址
    private static String RECIPIENT_ADDRESS = "";
    //发件人账户名
    private final static String SENDER_ACCOUNT = "";
    //发件人账户密码
    private final static String SENDER_PASSWORD = "";


    /**
     * 获取邮件实例，需自定义
     *
     * @return 一封邮件实例
     */
    public static MimeMessage getMimeMessageInstance(){
        return new MimeMessage(getSession());
    }

    /**
     * @return 获取 session 实例
     */
    private static Session getSession(){
        /* 1.配置连接邮件服务器的参数 **/
        Properties properties = new Properties();

        // 用户认证方式
        properties.setProperty("mail.smtp.auth","true");
        // 传输协议
        properties.setProperty("mail.transport.protocol", "smtp");
        // 发件人的SMTP服务器地址
        properties.setProperty("mail.smtp.host", "smtp.163.com");

        /* 2.创建 邮件Session 对象 **/
        Session session = Session.getInstance(properties);
        // 控制台打印调试信息
        session.setDebug(true);

        return session;
    }
    /**
     * 发送邮件
     *
     * @throws MessagingException
     */
    public static void send(Message message) throws MessagingException {
        /* 获取session对象 **/
        Session session = getSession();

        /* 3.使用 session 创建邮件传输对象 **/
        Transport transporter = session.getTransport();

        // 连接 发件人服务邮箱
        transporter.connect(SENDER_ACCOUNT,SENDER_PASSWORD);

        // 给所有收件人发送
        transporter.sendMessage(message,message.getAllRecipients());
        // 只给 抄送类型的收件人发送
//      transporter.sendMessage(message,message.getRecipients(MimeMessage.RecipientType.CC));
        // 给指定邮箱发送
//      transporter.sendMessage(message,new Address[]{new InternetAddress(RECIPIENT_ADDRESS)});

        /* 4.关闭邮件连接 **/
        transporter.close();
    }


//    public static void send(String senderAddr,String recipientAddr,String subject,String content,String imagePath,String attachmentPath) throws MessagingException, UnsupportedEncodingException {
//        MimeMessage msg = getMimeMessageInstance();
//        // 设置发件人地址
//        msg.setFrom(new InternetAddress(senderAddr));
//        /*
//         * 设置收件人地址 -- 可发多个
//         * MimeMessage.RecipientType.TO:发送
//         * MimeMessage.RecipientType.CC：抄送
//         * MimeMessage.RecipientType.BCC：密送
//         */
//        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recipientAddr));
//
//        //设置邮件主题
//        msg.setSubject(subject, StandardCharsets.UTF_8.name());
//        //设置邮件正文
//        msg.setContent(content, "text/html;charset=UTF-8");
//
//        MimeBodyPart text_image = null;
//
//        if(StringUtils.isNotBlank(imagePath)){
//            // 5. 创建图片"节点"
//            MimeBodyPart image = new MimeBodyPart();
//            // 读取本地文件
//            DataHandler imageHandler = new DataHandler(new FileDataSource(imagePath));
//            // 将图片数据添加到"节点"
//            image.setDataHandler(imageHandler);
//            // 为"节点"设置一个唯一编号（在文本"节点"中将引用该ID）
//            image.setContentID("imgCid");
//
//            // 6. 创建文本"节点"
//            MimeBodyPart text = new MimeBodyPart();
//            // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
//            text.setContent(content+"<br/><a href='#'><img src='cid:imgCid'/></a>", "text/html;charset=UTF-8");
//
//            // 7. （文本+图片）设置 文本 和 图片"节点"的关系（将 文本 和 图片"节点"合成一个混合"节点"）
//            MimeMultipart mm_text_image = new MimeMultipart();
//            mm_text_image.addBodyPart(text);
//            mm_text_image.addBodyPart(image);
//            mm_text_image.setSubType("related");    // 关联关系
//
//            // 8. 将 文本+图片 的混合"节点"封装成一个普通"节点"
//            // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
//            // 上面的 imgCid 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
//            text_image = new MimeBodyPart();
//            text_image.setContent(mm_text_image);
//        }
//
//
//        if(StringUtils.isNotBlank(attachmentPath)){
//            // 9. 创建附件"节点"
//            MimeBodyPart attachment = new MimeBodyPart();
//            // 读取本地文件
//            DataHandler attachmentHandler = new DataHandler(new FileDataSource(attachmentPath));
//            // 将附件数据添加到"节点"
//            attachment.setDataHandler(attachmentHandler);
//            // 设置附件的文件名（需要编码）
//            attachment.setFileName(MimeUtility.encodeText(attachmentHandler.getName()));
//
//            // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
//            MimeMultipart mm = new MimeMultipart();
//            mm.addBodyPart(text_image);
//            mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
//            mm.setSubType("mixed");         // 混合关系
//
//            // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
//            msg.setContent(mm);
//        }
//
//
//
//
//        //设置邮件的发送时间,默认立即发送
//        msg.setSentDate(new Date());
//
//        send(msg);
//    }

    /**
     *发送不带附件的邮件
     *
     * @param senderAddr 发件人邮箱
     * @param recipientAddr 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件正文
     */
    public static void send(String senderAddr,String recipientAddr,String subject,String content) throws UnsupportedEncodingException, MessagingException {
        send(senderAddr,recipientAddr,subject,content,"");
    }
    /**
     *发送带附件的邮件
     *
     * @param senderAddr 发件人邮箱
     * @param recipientAddr 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件正文
     * @param attachmentPath 附件路径
     */
    public static void send(String senderAddr,String recipientAddr,String subject,String content,String attachmentPath) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = getMimeMessageInstance();
        // 设置发件人地址
        msg.setFrom(new InternetAddress(senderAddr));
        /*
         * 设置收件人地址 -- 可发多个
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recipientAddr));

        //设置邮件主题
        msg.setSubject(subject, StandardCharsets.UTF_8.name());
        //设置邮件正文
        msg.setContent(content, "text/html;charset=UTF-8");

        if(StringUtils.isNotBlank(attachmentPath)){
            // 若附件不为空 则重新创建文本结点
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(content,"text/html;charset=UTF-8");

            // 创建附件"节点"
            MimeBodyPart attachment = new MimeBodyPart();
            // 读取本地文件
            DataHandler attachmentHandler = new DataHandler(new FileDataSource(attachmentPath));
            // 将附件数据添加到"节点"
            attachment.setDataHandler(attachmentHandler);
            // 设置附件的文件名（需要编码）
            attachment.setFileName(MimeUtility.encodeText(attachmentHandler.getName()));

            // 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(text);
            mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
            mm.setSubType("mixed");         // 混合关系

            // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
            msg.setContent(mm);
        }

        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());

        send(msg);
    }




    public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        send(SENDER_ADDRESS,RECIPIENT_ADDRESS,"【天府邮差】XXX来信！","hello world~~");
    }
}
