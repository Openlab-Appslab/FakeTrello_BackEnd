package FakeTrelloBackEnd.example.FakeTrelloBackEnd.presentation;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.UserEditDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.UserRegistrationDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/showBSI")
    public String sendBasicInfo(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getUsername();
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
    public String editUser(@RequestBody UserEditDTO userEditDTO, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.editUser(userEditDTO, userDetails.getUsername());
    }

}
