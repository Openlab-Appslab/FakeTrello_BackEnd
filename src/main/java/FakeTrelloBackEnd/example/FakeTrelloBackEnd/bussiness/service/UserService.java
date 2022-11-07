package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.UserRegistrationDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.UserRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserAlreadyExists;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserDoesntExist;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;

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

        if(userRepository.findByEmail(userRegistrationDTO.getEmail()).isEmpty()){

            String encodePassword = encoder.encode(userRegistrationDTO.getPassword());
            User user = new User(encodePassword, userRegistrationDTO.getEmail());
            userRepository.save(user);
        }else{
            throw new UserAlreadyExists("User Already Exists!");
        }
    }

    public void deleteUser(String email) {

        if(userRepository.findByEmail(email).isPresent()){
            userRepository.delete(userRepository.findByEmail(email).get());
        }else{
            throw new UserDoesntExist("User with this email doesn't exist: "+email);
        }
    }
}
