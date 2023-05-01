package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.service;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.CreateTaskDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.EditTaskDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO.TaskInfoDTO;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.enums.TaskState;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.Image;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.Task;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.User;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.ImageRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.TaskRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess.UserRepository;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.generalException.BadRequest;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.taskException.TaskDoesNotExist;
import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.userException.UserDoesntExist;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public void createTask(CreateTaskDTO createTaskDTO, String email) throws IOException {
        User user = getUserOrThrow(email);

        Task task = new Task(
                createTaskDTO.getTitle(),
                createTaskDTO.getDeadline(),
                createTaskDTO.getText(),
                user
        );

        if(createTaskDTO.getListOfImage() != null){
            createTaskDTO.getListOfImage().forEach(e -> {

                String fileName = StringUtils.cleanPath(Objects.requireNonNull(e.getOriginalFilename()));
                if (fileName.contains(".."))
                    throw new BadRequest("Image is not a valid");
                try {
                    task.getListOfImages().add(imageRepository.save(new Image(StreamUtils.copyToByteArray(e.getInputStream()), task)));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }});

            }

            user.getListOfTasks().add(task); //saving automation, it's happen by annotation @Transactional
        taskRepository.save(task);
    }

    @Transactional
    public Task editTask(EditTaskDTO dto) {
        Task task = getUsersTask(dto.getId());

        saveIfNotEmpty(dto.getTitle(), task, Task::setTitle);
        saveIfNotEmpty(dto.getText(), task, Task::setText);
        saveIfNotEmpty(dto.getDeadline(), task, Task::setDeadline);

        return task;
    }

    @Transactional
    public void editStateTask(Long id, EditTaskDTO editTaskDTO){
        Task task = getUsersTask(id);

        if(editTaskDTO.getState().equals(TaskState.INPROGRESS.toString()))
            task.setState(TaskState.INPROGRESS);
        else if (editTaskDTO.getState().equals(TaskState.DONE.toString()))
            task.setState(TaskState.DONE);
        else
            task.setState(TaskState.TODO);

    }

    public List<TaskInfoDTO> getAllUsersTasks(String email) {

        User user = getUserOrThrow(email);

        List<TaskInfoDTO> returnList = new ArrayList<>();

        for (Task task : user.getListOfTasks()){
            returnList.add(convertToTaskInfoDTO(task));
        }

        return returnList;
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

    public <T> void saveIfNotEmpty(T toBeSet, Task task, BiConsumer<Task,T> setter){
        if (toBeSet == null)
            throw new BadRequest("Something went wrong!");

        setter.accept(task, toBeSet);
    }

    public User getUserOrThrow(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UserDoesntExist("User was not found!"));
    }

    public static TaskInfoDTO convertToTaskInfoDTO(Task task){
        return new TaskInfoDTO(
                task.getId(),
                task.getTitle(),
                task.getDeadline(),
                task.getText(),
                task.getState().toString());
    }
}
