package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskInfoDTO {
    private Long id;

    private String title;
    private String deadline;
    private String text;
    private String state;
    private List<byte[]> listOfImage;
}
