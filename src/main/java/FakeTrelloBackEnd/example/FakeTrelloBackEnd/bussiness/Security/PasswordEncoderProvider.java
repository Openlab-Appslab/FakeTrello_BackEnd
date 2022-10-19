package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderProvider {
    @Bean
    public BCryptPasswordEncoder getEncoder(){ return new BCryptPasswordEncoder(10);
    }
}
