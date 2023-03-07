package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EditTaskDTO {
    private Long id;
    private String deadline;
    private String text;
    private String date;
    private String taskState;
}
