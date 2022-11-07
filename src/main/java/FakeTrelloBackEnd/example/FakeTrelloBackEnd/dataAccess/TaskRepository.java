package FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
