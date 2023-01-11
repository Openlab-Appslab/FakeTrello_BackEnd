package FakeTrelloBackEnd.example.FakeTrelloBackEnd.presentation;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.CreateTaskDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.EditTaskDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserDetailsDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserEditDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserRegistrationDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.Task;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service.TaskService;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service.UserService;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.BadRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@AllArgsConstructor
public class MainController {

    private final UserService userService;
    private final TaskService taskService;

    @GetMapping("/login")
    public void login(){

    }

    @GetMapping("/getUserDetails")
    public UserDetailsDTO sendBasicInfo(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.getUserDetails(userDetails.getUsername());
    }

    @PostMapping("/noAuth/register")
    public void userRegistration(@RequestBody UserRegistrationDTO userRegistrationDTO){
        userService.addNewUser(userRegistrationDTO);
    }

    @DeleteMapping("/deleteUser")
    public void deleteUser(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userService.deleteUser(userDetails.getUsername());
    }

    @PutMapping("/editUser")
    public void editUser(@RequestBody UserEditDTO userEditDTO, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userService.editUser(userEditDTO, userDetails.getUsername());
    }

    @GetMapping("/getRawUser")
    public User getRawUser(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userService.getUserByEmail(userDetails.getUsername()).get();
    }

    //TASK CONTROLLER

    @PostMapping(value = "/createTask",
    consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createTask(@RequestBody CreateTaskDTO createTaskDTO, Authentication authentication) throws IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        taskService.createTask(createTaskDTO, userDetails.getUsername());
    }

    @GetMapping("/getAllUsersTasks")
    public Set<Task> getAllUsersTasks(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return taskService.getAllUsersTasks(userDetails.getUsername());
    }

    @GetMapping("/getUsersTask/{id}")
    public Task getUsersTask(@PathVariable Long id){
        return taskService.getUsersTask(id);
    }

    @DeleteMapping("/deleteTask/{id}")
    public void deleteTask(@PathVariable Long id, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        taskService.deleteTask(id, userDetails.getUsername());
    }

    @PutMapping("/editTask")
    public Task editTask(@RequestBody EditTaskDTO dto){
        return taskService.editTask(dto);
    }

    //PREPARING FOR IMAGE

    @PutMapping("/editWithImage")
    public void editUserWithImage(@RequestParam("firstName") String firstName,
                                  @RequestParam("lastName") String lastName,
                                  @RequestParam("nickname") String nickname,
                                  @RequestParam("phoneNumber") Integer phoneNumber,
                                  @RequestParam("image") MultipartFile image,
                                  Authentication authentication){

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userService.editWithImage(firstName, lastName, nickname, phoneNumber, image, userDetails.getUsername());

    }

    @PostMapping("/testImage")
    public ResponseEntity<byte[]> addImage(@RequestParam("image") MultipartFile file){

        try {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf(file.getContentType()))
                    .body(file.getBytes());
        } catch (IOException e) {
            throw new BadRequest("Something went wrong");
        }
    }

    /*@PutMapping("/editWithImageWithDTO")
    public void editUserWithImageWithDTO(UserEditDTO dto, Authentication authentication){

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userService.editWithImage(dto.getFirstName(), dto.getLastName(), dto.getNickname(), dto.getPhoneNumber(), dto.getImage(), userDetails.getUsername());

    }*/

}
