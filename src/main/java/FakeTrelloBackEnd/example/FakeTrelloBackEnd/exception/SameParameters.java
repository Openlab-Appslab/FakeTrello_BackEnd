package FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SameParameters extends RuntimeException{
    public SameParameters(String message){super(message);}
}
