package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailsDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String nickname;
    private Integer phoneNumber;
    private String image;
}
