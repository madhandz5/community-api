package ryan.community.module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ryan.community.module.domain.BoardReply;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {
}
