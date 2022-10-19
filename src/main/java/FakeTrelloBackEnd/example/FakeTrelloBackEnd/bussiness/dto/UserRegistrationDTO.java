package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationDTO {
    private String name;
    private String password;
    private String email;
}
