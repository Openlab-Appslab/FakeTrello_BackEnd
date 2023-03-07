package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserDetailsDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserEditDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserRegistrationDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.VerificationToken;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.UserRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.BadRequest;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserAlreadyExists;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserDoesntExist;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.tokenException.TokenExpired;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.tokenException.TokenInvalid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void addNewUser(UserRegistrationDTO userRegistrationDTO) {

        if(userRepository.findByEmail(userRegistrationDTO.getEmail()).isEmpty()){
            String encodePassword = encoder.encode(userRegistrationDTO.getPassword());
            User user = new User(encodePassword, userRegistrationDTO.getEmail());

            Optional<User> saved = Optional.of(userRepository.save(user));
            saved.ifPresent(u -> {
                try{
                    String token = UUID.randomUUID().toString();
                    verificationTokenService.save(saved.get(),token);

                    emailService.sendVerificationEmail(u);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

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
                optionalUser.getProfileImage());
    }

    @Transactional
    public void editWithImage(String firstName,
                              String lastName,
                              String nickname,
                              Integer phoneNumber,
                              String image,
                              String email) {

        User user = checkIfUserExistAndSendBack(email);

        saveIfNotEmpty(firstName, user, User::setFirstName);
        saveIfNotEmpty(lastName, user, User::setLastName);
        saveIfNotEmpty(nickname, user, User::setNickname);
        saveIfNotEmpty(phoneNumber, user, User::setPhoneNumber);
        saveIfNotEmpty(image, user, User::setProfileImage);
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

    @Transactional
    public void activateUserEmail(String token) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);

        if(verificationToken == null){
            new TokenInvalid("Your verification token is invalid!");

        }else {
            User user = verificationToken.getUser();

            if (!user.isEnable()) {
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                if (verificationToken.getExpiryDate().before(currentTimestamp)) {
                    new TokenExpired("Your verification token has expired!");
                } else {
                    user.setEnable(true);
                    verificationTokenService.deleteToken(token);
                }
            }
        }
    }


    @Transactional
    public void sendResetEmail(String email) throws Exception{
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserDoesntExist("User was not found!"));

        String token = UUID.randomUUID().toString();
        verificationTokenService.save(user,token);

        emailService.sendResetPasswordEmail(user);
    }

    public void checkVerificationTokenIsValidOrThrow(String token, String password) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);

        if(verificationToken == null){
            new TokenInvalid("Your verification token is invalid!");
        }else {
            User user = verificationToken.getUser();

            if (user.isEnable()) {
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                if (verificationToken.getExpiryDate().before(currentTimestamp)) {
                    new TokenExpired("Your verification token has expired!");
                } else {
                    verificationTokenService.deleteToken(token);
                    resetPassword(user, password);
                }
            }
        }
    }

    @Transactional
    public void resetPassword(User user, String password){
        if(!Objects.equals(user.getPassword(), password)){

             user.setPassword(encoder.encode(password));
             userRepository.save(user);
            System.out.println(user.getPassword());
        }else {
            throw new BadRequest("Password is same or not valid!");
        }
    }

    @Transactional
    public void uploadProfilePicture(String image, String email) {
        User user = checkIfUserExistAndSendBack(email);

    }
}
