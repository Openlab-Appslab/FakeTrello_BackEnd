package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "string_in_image", columnDefinition="TEXT")
    private byte[] image;

    @ManyToOne(cascade = CascadeType.ALL)
    private Task task;

    @OneToOne
    private User user;


    public Image(byte[] image, Task task) {
        this.image = image;
        this.task = task;
        this.user = null;
    }

    public Image(byte[] image, User user) {
        this.image = image;
        this.user = user;
        this.task = null;
    }
}

