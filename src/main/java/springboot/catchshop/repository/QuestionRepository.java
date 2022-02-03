package springboot.catchshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.catchshop.domain.Question;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findById(Long id);
    List<Question> findByAnswered(String answered);
    List<Question> findAll();
}