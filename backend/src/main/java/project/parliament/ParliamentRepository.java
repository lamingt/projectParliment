package project.parliament;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ParliamentRepository extends JpaRepository<ParliamentBill, Integer> {

}
