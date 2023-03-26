package FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long > {
}
