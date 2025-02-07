package project.threads;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, UUID> {
    @Query("select t from Thread t order by t.date desc")
    List<Thread> getThreadsByDate(Pageable pageable);
}
