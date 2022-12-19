package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

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

    @Column(name = "headline")
    private String headline;

    @Column(name = "text",
            columnDefinition = "TEXT")
    private String text;

    @Column(name = "date")
    private String date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Lob
    @Column(name = "images")
    @Type(type = "org.hibernate.type.ImageType")
    private String listOfImages;

    public Task(String headline, String text, String date, User user) {
        this.headline = headline;
        this.text = text;
        this.date = date;
        this.user = user;
        this.listOfImages = null;
    }

    public Task(String headline, String text, String date, User user, String listOfImages) {
        this.headline = headline;
        this.text = text;
        this.date = date;
        this.user = user;
        this.listOfImages = listOfImages;
    }
}
