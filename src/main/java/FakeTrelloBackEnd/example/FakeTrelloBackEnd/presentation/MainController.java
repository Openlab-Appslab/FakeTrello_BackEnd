package FakeTrelloBackEnd.example.FakeTrelloBackEnd.presentation;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserDetailsDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserEditDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserRegistrationDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/getUserDetails")
    public UserDetailsDTO sendBasicInfo(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.getUserDetails(userDetails.getUsername());
    }

    @PostMapping("/noAuth/register")
    public void userRegistration(@RequestBody UserRegistrationDTO userRegistrationDTO){
        userService.addNewUser(userRegistrationDTO);
    }

    @DeleteMapping("/delete/user")
    public void deleteUser(@RequestBody UserRegistrationDTO userRegistrationDTO){
        userService.deleteUser(userRegistrationDTO.getEmail());
    }

    @PutMapping("/edit/user")
    public void editUser(@RequestBody UserEditDTO userEditDTO, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userService.editUser(userEditDTO, userDetails.getUsername());
    }

}
