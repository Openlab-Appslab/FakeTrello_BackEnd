package FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.tokenException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TokenInvalid extends RuntimeException{
    public TokenInvalid(String message){super(message);}
}
