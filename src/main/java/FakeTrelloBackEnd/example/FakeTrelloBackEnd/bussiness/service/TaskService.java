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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Table;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private UserRepository userRepository;
    @Transactional
    public void createTask(CreateTaskDTO createTaskDTO, String email) throws IOException {
        User user = getUserOrThrow(email);

        Task task = new Task(
                createTaskDTO.getDeadline(),
                createTaskDTO.getText(),
                user
        );

        user.getListOfTasks().add(task); //saving automation, it's happen by annotation @Transactional
        taskRepository.save(task);
    }
/*

    private Set<byte[]> checkIfImagesAreValidOrThrow(Set<MultipartFile> listOfImages) throws IOException {
            listOfImages.forEach((item) ->{
                if (Objects.requireNonNull(item.getOriginalFilename()).contains(".."))
                    throw new BadRequest("Not supported");
            });

        return encodeBytesToStringWithTryCatch(listOfImages);
    }

    private Set<byte[]> encodeBytesToStringWithTryCatch(Set<MultipartFile> listOfImages) throws IOException {
        Set<byte[]> listOfBytesOfImages = new HashSet<>();

        for (MultipartFile image: listOfImages) {
            listOfBytesOfImages.add(image.getBytes());
        }
        return listOfBytesOfImages;
    }
*/


    public User getUserOrThrow(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UserDoesntExist("User was not found!"));
    }

    public Set<Task> getAllUsersTasks(String email) {
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

        saveIfNotEmpty(dto.getText(), task, Task::setText);
        saveIfNotEmpty(dto.getDeadline(), task, Task::setDeadline);

        return task;
    }

    @Transactional
    public void editStateTask(Long id, Task taskFromFE){
        Task task = getUsersTask(id);
        task.setTaskState(taskFromFE.getTaskState());
    }

    public <T> void saveIfNotEmpty(T toBeSet, Task task, BiConsumer<Task,T> setter){
        if (toBeSet == null)
            throw new BadRequest("Something went wrong!");

        setter.accept(task, toBeSet);
    }
}
