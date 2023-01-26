package FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.tokenException;

import antlr.Token;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class TokenExpired extends RuntimeException{
    public TokenExpired(String message){super(message);}
}
