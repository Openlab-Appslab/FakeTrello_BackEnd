package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.userDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEditDTO {
    private String firstName;
    private String lastName;
    private String nickname;
    private Integer phoneNumber;
    //private MultipartFile image;

}
