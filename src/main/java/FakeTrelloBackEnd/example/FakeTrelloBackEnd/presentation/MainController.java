package FakeTrelloBackEnd.example.FakeTrelloBackEnd.presentation;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.CreateTaskDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.EditTaskDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.TaskInfoDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserDetailsDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserEditDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO.UserRegistrationDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.Task;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service.TaskService;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    //TASK CONTROLLER

    @PostMapping(value = "/createTask")
    public void createTask(@RequestBody CreateTaskDTO createTaskDTO, Authentication authentication) throws IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        taskService.createTask(createTaskDTO, userDetails.getUsername());
    }

    @GetMapping("/getAllUsersTasks")
    public List<TaskInfoDTO> getAllUsersTasks(Authentication authentication){
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

    @GetMapping("noAuth/resetPassword/{token}/{password}")
    public void resetPassword(@PathVariable("token") String token,
                              @PathVariable("password") String password){
        userService.checkVerificationTokenIsValidOrThrow(token, password);
    }

    @GetMapping("noAuth/sendResetEmail/{email}")
    public void sendResetEmail(@PathVariable String email) throws Exception{
        userService.sendResetEmail(email);
    }

    //PREPARING FOR IMAGE

    @PutMapping("/uploadProfilePicture")
    public void uploadProfilePicture(@RequestParam("image") MultipartFile image, Authentication authentication
    ){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userService.uploadProfilePicture(image, userDetails.getUsername());
    }

    @GetMapping("/getProfilePicture")
    public ResponseEntity<byte[]> getProfilePicture(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(userService.getProfilePicture(userDetails.getUsername()));
    }


    @PutMapping("/updateTaskState/{id}")
    public void updateTaskState(@PathVariable("id") Long id, @RequestBody EditTaskDTO editTaskDTO){
        taskService.editStateTask(id, editTaskDTO);
    }


    //Verification with email
    @GetMapping("/noAuth/verify/{token}")
    public void activationEmail(@PathVariable String token){
        userService.activateUserEmail(token);
    }

}
