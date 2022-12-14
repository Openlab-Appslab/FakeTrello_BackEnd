package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserDetailsDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserEditDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserRegistrationDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.UserRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.BadRequest;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserAlreadyExists;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserDoesntExist;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

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
        userRepository.delete(checkIfUserExistAndSendBack(email));
    }

    @Transactional
    public void editUser(UserEditDTO userEditDTO, String email) {
        User optionalUser = checkIfUserExistAndSendBack(email);
        saveIfNotEmpty(userEditDTO.getFirstName(), optionalUser, User::setFirstName);
        saveIfNotEmpty(userEditDTO.getLastName(), optionalUser, User::setLastName);
        saveIfNotEmpty(userEditDTO.getNickname(), optionalUser, User::setNickname);
        saveIfNotEmpty(userEditDTO.getPhoneNumber(), optionalUser, User::setPhoneNumber);
    }

    public <T> void saveIfNotEmpty(T toBeSet, User user, BiConsumer<User, T> setter){

        if(toBeSet == null)
           throw new BadRequest("Something went wrong!");

        setter.accept(user, toBeSet);
    }

    public UserDetailsDTO getUserDetails(String email) {
        User userOptional = checkIfUserExistAndSendBack(email); //findUserOrThrow

        return convertUserToDTO(userOptional);
    }

    public User checkIfUserExistAndSendBack(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UserDoesntExist("User doesn't found!"));
    }

    public UserDetailsDTO convertUserToDTO(User optionalUser){
        return new UserDetailsDTO(
                optionalUser.getEmail(),
                optionalUser.getFirstName(),
                optionalUser.getLastName(),
                optionalUser.getNickname(),
                optionalUser.getPhoneNumber(),
                convertByteToResponseEntity(optionalUser.getProfileImage()));
    }

    @Transactional
    public void editWithImage(String firstName,
                              String lastName,
                              String nickname,
                              Integer phoneNumber,
                              MultipartFile image,
                              String email) {

        User user = checkIfUserExistAndSendBack(email);

        saveIfNotEmpty(firstName, user, User::setFirstName);
        saveIfNotEmpty(lastName, user, User::setLastName);
        saveIfNotEmpty(nickname, user, User::setNickname);
        saveIfNotEmpty(phoneNumber, user, User::setPhoneNumber);
        saveIfNotEmpty(convertImageToBytes(image), user, User::setProfileImage);
    }

    public byte[] convertImageToBytes(MultipartFile image){

        if(StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename())).contains(".."))
            throw new BadRequest("Something went wrong! Try again");

        try {
            return image.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<byte[]> convertByteToResponseEntity(byte[] bytesOfImage){
        return ResponseEntity
                .ok()
                .body(bytesOfImage);
    }
}
