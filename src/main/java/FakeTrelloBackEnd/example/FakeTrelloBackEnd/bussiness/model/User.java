package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "password")
    private String password;

    @Column(name = "email",
            unique = true)
    private String email;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "nickname",
            unique = true)
    private String nickname;

    @Column(name = "phoneNumber",
            unique = true)
    private Integer phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Task> listOfTasks;

    @Lob
    @Column(name="profile_image")
    @Type(type = "org.hibernate.type.ImageType")
    @JsonIgnore
    private String profileImage;

    public User(String password, String email) {
        this.password = password;
        this.email = email;
        this.firstName = null;
        this.lastName = null;
        this.nickname = null;
        this.phoneNumber = null;
        this.profileImage = null;
        this.listOfTasks = new HashSet<>();
    }

}
