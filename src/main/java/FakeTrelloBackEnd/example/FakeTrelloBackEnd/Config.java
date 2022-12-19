package FakeTrelloBackEnd.example.FakeTrelloBackEnd;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class Config {

    @Bean
    CommandLineRunner commandLineRunner (UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String encodePass = passwordEncoder.encode("1234");
            User user = new User(encodePass,"mato@gmail.com" );
            userRepository.save(user);
        };
    }
}
