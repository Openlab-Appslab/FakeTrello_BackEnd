package FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.IM_USED)
public class UserAlreadyExists extends RuntimeException{
    public UserAlreadyExists(String message){super(message);}
}
