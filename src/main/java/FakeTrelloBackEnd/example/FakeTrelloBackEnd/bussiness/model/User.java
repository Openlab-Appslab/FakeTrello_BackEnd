package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.exception.UserAlreadyExists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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


    //private Set<Task> listOfTask;

    public User(String password, String email) {
        this.password = password;
        this.email = email;
        this.firstName = "Nedefinované";
        this.lastName = "Nedefinované";
        this.nickname = "Nedefinované";
        this.phoneNumber = 0;
    }
}
