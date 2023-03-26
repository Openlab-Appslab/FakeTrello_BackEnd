package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name="file")
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] file;

    @ManyToOne(cascade = CascadeType.ALL)
    private Task task;


    public File(byte[] file, Task task) {
        this.file = file;
        this.task = task;
    }
}
