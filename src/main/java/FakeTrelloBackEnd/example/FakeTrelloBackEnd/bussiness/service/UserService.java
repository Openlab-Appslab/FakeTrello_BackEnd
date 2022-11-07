package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.UserEditDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.UserRegistrationDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.UserRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.BadRequest;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.SameParameters;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserAlreadyExists;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserDoesntExist;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.DOMException;

import javax.management.BadAttributeValueExpException;
import java.util.Objects;
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

    @Transactional
    public String editUser(UserEditDTO userEditDTO, String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()){

            if(userEditDTO.getEmail().length() > 0  && userEditDTO.getEmail() != null){

                if(userRepository.findByEmail(userEditDTO.getEmail()).isEmpty()){

                    if(!Objects.equals(user.get().getEmail(), userEditDTO.getEmail())){
                        user.get().setEmail(userEditDTO.getEmail());
                    }else{
                        throw new SameParameters("You have same email! Change It!");
                    }

                }else {
                    throw new UserAlreadyExists("User Already Exists!");
                }
            }else{
                throw new BadRequest("Bad params!");
            }


            if(userEditDTO.getPassword().length() > 0  && userEditDTO.getPassword() != null){

                    if(!Objects.equals(user.get().getPassword(), userEditDTO.getPassword())){
                        String encodePassword = encoder.encode(userEditDTO.getPassword());
                        user.get().setPassword(encodePassword);
                    }else{
                        throw new SameParameters("You have same password! Change It!");
                    }
            }else{
                throw new BadRequest("Bad params!");
            }


            return user.get().getEmail();

        }else{
            throw new UserDoesntExist("User with this email doesn't exist: "+email);
        }


        //part for password




    }
}
