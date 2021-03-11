package ryan.community.module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ryan.community.module.domain.Account;
import ryan.community.module.domain.Board;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByAccount(Account account);
}
