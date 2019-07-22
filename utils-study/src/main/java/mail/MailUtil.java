package mail;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.io.File;

/**
 * 此类用于simplejavamail的学习
 */
public class MailUtil {
    @Test
    public void test() throws Exception {
        // almost everything is optional:
        Email email = EmailBuilder.startingBlank()
                .from("发短信的名称1", "发短信的帐号@163.com")
                .to("名称1", "接受短信帐号1@qq.com")
                .to("名称2", "接受短信帐号2@qq.com")
                .to("名称3", "接受短信帐号3@163.com")
                .withSubject("hey")
                .withHTMLText("<h4>HTML+附件+内嵌图片的邮件测试！！！</h4></br><a href=http://www.apache.org>" + "点击跳转</a>" +
                        "<h4>刘亦菲</h4></hr><img src=\"cid:wink1\">" +
                        "<h4>风景</h4></hr><img src=\"cid:wink2\">")
                .withPlainText("Please view this email in a modern email client!")
                .withEmbeddedImage("wink1", FileUtils.readFileToByteArray(new File("D:/imges/1.png")),"image/png")
                .withEmbeddedImage("wink2", FileUtils.readFileToByteArray(new File("D:/imges/2.png")),"image/png")
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.163.com", 25, "发短信的帐号", "发短信的帐号的授权码")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(10 * 1000)
                .clearEmailAddressCriteria() // turns off email validation
                .withDebugLogging(true)
                .buildMailer();

        mailer.sendMail(email);
    }

    @Test
    public void test2(){
        Email email = EmailBuilder.startingBlank()
                .from("发短信的名称1", "发短信的帐号@163.com")
                .to("名称1", "接受短信帐号1@qq.com")
                .to("名称2", "接受短信帐号2@qq.com")
                .to("名称3", "接受短信帐号3@163.com")
                .withSubject("My Bakery is finally open!")
                .withPlainText("Mom, Dad. We did the opening ceremony of our bakery!!!")
                .buildEmail();

        MailerBuilder
                .withSMTPServer("smtp.163.com", 25, "发短信的帐号", "发短信的帐号的授权码")
                .withSessionTimeout(10 * 1000)
                .withProperty("mail.smtp.sendpartial", "true")
                .buildMailer()
                .sendMail(email);
    }
}
