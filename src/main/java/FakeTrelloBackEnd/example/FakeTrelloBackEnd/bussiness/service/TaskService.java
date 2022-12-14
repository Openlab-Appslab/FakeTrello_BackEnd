package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.CreateTaskDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.Task;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.TaskRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.UserRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserDoesntExist;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.taskException.TaskDoesNotExist;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private UserRepository userRepository;
    @Transactional
    public void createTask(CreateTaskDTO createTaskDTO, String email) {
        User user = getUserOrThrow(email);

        Task task = new Task(
                createTaskDTO.getHeadline(),
                createTaskDTO.getText(),
                createTaskDTO.getDate(),
                user
        );

        user.getListOfTasks().add(task); //saving automation, it's happen by annotation @Transactional
        taskRepository.save(task);
    }

    public User getUserOrThrow(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UserDoesntExist("User was not found!"));
    }

    public Set<Task> getAllUsersTask(String email) {
        User user = getUserOrThrow(email);
        return user.getListOfTasks();
    }

    public Task getUsersTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskDoesNotExist("Task was not found!"));
    }
}
