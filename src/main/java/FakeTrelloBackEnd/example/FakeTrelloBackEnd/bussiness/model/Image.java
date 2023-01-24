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
    private String image;

    @ManyToOne(cascade = CascadeType.ALL)
    private Task task;
}

