package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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

//    @Column(name = "headline")
//    private String headline;

    @Column(name = "text",
            columnDefinition = "TEXT")
    private String text;

    @Column(name = "date")
    private String deadline;

    @Column(name="state")
    private String state;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @Column(name = "images")
    @OneToMany(mappedBy = "task")
    private Set<Image> listOfImages;

    public Task(String deadline, String text, User user) {
        this.deadline = deadline;
        this.text = text;
        this.user = user;
        this.state = "toDo";
    }

}
