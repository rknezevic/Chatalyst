package repository;

import com.example.Chatalyst.model.Demographic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemographicRepository extends JpaRepository<Demographic, Long> {
}
