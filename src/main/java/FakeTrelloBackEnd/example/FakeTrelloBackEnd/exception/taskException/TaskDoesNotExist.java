package FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.taskException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TaskDoesNotExist extends RuntimeException{
    public TaskDoesNotExist(String message){super(message);}
}
