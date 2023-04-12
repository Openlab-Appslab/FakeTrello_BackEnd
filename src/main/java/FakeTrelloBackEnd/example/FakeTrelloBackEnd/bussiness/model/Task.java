package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.enums.TaskState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text",
            columnDefinition = "TEXT")
    private String text;

    @Column(name = "date")
    private String deadline;

    @Column(name="state")
    private Enum state;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @Column(name = "images")
    @OneToMany(mappedBy = "task")
    private List<Image> listOfImages;

    @JsonIgnore
    @Column(name = "files")
    @OneToMany(mappedBy = "task")
    private List<File> listOfFiles;

    public Task(String title ,String deadline, String text, User user) {
        this.title = title;
        this.deadline = deadline;
        this.text = text;
        this.user = user;
        this.state = TaskState.TODO;
        this.listOfImages = new ArrayList<>();
        this.listOfFiles = new ArrayList<>();
    }

}
