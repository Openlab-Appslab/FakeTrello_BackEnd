package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.dto.taskDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateTaskDTO {

    private String title;
    private String deadline;
    private String text;

    private List<MultipartFile> listOfFile;
    private List<MultipartFile> listOfImage;
}