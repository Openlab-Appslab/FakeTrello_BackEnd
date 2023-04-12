package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @Column(name = "mondaySubjects")
    private List<String> listOfSubjectsOnMonday;

    @ElementCollection
    @Column(name = "tuesdaySubjects")
    private List<String> listOfSubjectsOnTuesday;

    @ElementCollection
    @Column(name = "wednesdaySubjects")
    private List<String> listOfSubjectsOnWednesday;

    @ElementCollection
    @Column(name = "thurdaysSubjects")
    private List<String> listOfSubjectsOnThurdays;

    @ElementCollection
    @Column(name = "fridaySubjects")
    private List<String> listOfSubjectsOnFriday;

    @OneToOne(mappedBy = "schedule")
    User user;

    public Schedule(User user) {
        this.listOfSubjectsOnMonday = new ArrayList<>();
        this.listOfSubjectsOnTuesday = new ArrayList<>();
        this.listOfSubjectsOnWednesday = new ArrayList<>();
        this.listOfSubjectsOnThurdays = new ArrayList<>();
        this.listOfSubjectsOnFriday = new ArrayList<>();
        this.user = user;
    }
}
