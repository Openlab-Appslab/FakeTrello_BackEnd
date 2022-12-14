package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.CreateTaskDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.EditTaskDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.Task;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.TaskRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.UserRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.BadRequest;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserDoesntExist;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.taskException.TaskDoesNotExist;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.function.BiConsumer;

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

    public void deleteTask(Long id, String email) {
        User user = getUserOrThrow(email);
        Task task = getUsersTask(id);

        user.getListOfTasks().remove(task);
        task.setUser(null);
        taskRepository.delete(task);
    }

    @Transactional
    public Task editTask(EditTaskDTO dto) {
        Task task = getUsersTask(dto.getId());

        saveIfNotEmpty(dto.getDate(), task, Task::setDate);
        saveIfNotEmpty(dto.getText(), task, Task::setText);
        saveIfNotEmpty(dto.getHeadline(), task, Task::setHeadline);

        return task;
    }

    public void saveIfNotEmpty(String toBeSet, Task task, BiConsumer<Task,String> setter){
        if (toBeSet.isEmpty())
            throw new BadRequest("Something went wrong!");

        setter.accept(task, toBeSet);
    }
}
