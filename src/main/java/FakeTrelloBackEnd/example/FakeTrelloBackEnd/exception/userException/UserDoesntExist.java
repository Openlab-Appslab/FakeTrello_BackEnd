package FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.userException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserDoesntExist extends RuntimeException{
    public UserDoesntExist(String message){super(message);}
}
