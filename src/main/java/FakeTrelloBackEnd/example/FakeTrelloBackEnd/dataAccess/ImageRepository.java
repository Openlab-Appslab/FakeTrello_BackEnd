package FakeTrelloBackEnd.example.FakeTrelloBackEnd.dataAccess;

import FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
