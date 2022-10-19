package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.UserRegistrationDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void addNewUser(UserRegistrationDTO userRegistrationDTO) {
        String encodePassword = encoder.encode(userRegistrationDTO.getPassword());

        User user = new User(userRegistrationDTO.getName(), encodePassword, userRegistrationDTO.getEmail());

        userRepository.save(user);
    }
}
