package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.VerificationToken;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService {
    private VerificationTokenService verificationTokenService;
    private JavaMailSender javaMailSender;

    public void sendVerificationEmail(User user) throws Exception{
        VerificationToken verificationToken = verificationTokenService.findByUser(user);

        if(verificationToken != null){
            String token = verificationToken.getToken();

            String body = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Verifing your email address</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\".container\">\n" +
                    "      <h3>Verication for your email adress</h3><br>\n" +
                    "      <p>Thank you for signing up. Please click on the button to verify your email address!</p><br>\n" +
                    "      <a href=\"[[URL]]\">Click to verify your email </a>\n" +
                    "      <h3>See you soon!</h3>\n" +
                    "      \n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";

            String verifyURL= "http://localhost:4200" +"/verifyEmail/"+token;

            body = body.replace("[[URL]]", verifyURL);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Email Address Verification!");
            helper.setText(body,true);
            javaMailSender.send(message);
        }

    }

    public void sendResetPasswordEmail(User user) throws Exception{
        VerificationToken verificationToken = verificationTokenService.findByUser(user);

        if(verificationToken != null){
            String token = verificationToken.getToken();

            String body = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Reset your password!</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\".container\">\n" +
                    "      <h3>Reset password</h3><br>\n" +
                    "      <p>Please click on the button to reset your password!</p><br>\n" +
                    "      <a href=\"[[URL]]\">Click to reset password</a>\n" +
                    "      <h3>See you soon!</h3>\n" +
                    "      \n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";

            body = body.replace("[[URL]]", "http://localhost:4200" +"/resetPassword/"+token);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Reset password!");
            helper.setText(body,true);
            javaMailSender.send(message);
        }

    }
}
