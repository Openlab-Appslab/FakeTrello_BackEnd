package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "token_string")
    private String token;

    @Column(name = "expiry_date")
    private Timestamp expiryDate;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public VerificationToken(String token, Timestamp expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
