package ryan.community.module.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryan.community.infra.util.CommonResponse;
import ryan.community.module.domain.Account;
import ryan.community.module.domain.Board;
import ryan.community.module.repository.AccountRepository;
import ryan.community.module.repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;

    public Board insertArticle(JsonNode articleData) {
        String email = articleData.findValue("email").asText();
        Account account = accountRepository.findByEmail(email);
        Board board = Board.builder()
                .title(articleData.findValue("title").asText())
                .content(articleData.findValue("content").asText())
                .createdAt(LocalDateTime.now())
                .account(account)
                .isModified(false)
                .modifiedAt(null)
                .isActive(true)
                .replyList(null)
                .build();
        account.getBoardList().add(board);
        return boardRepository.save(board);
    }

    public List<Board> findArticleByAuthor(JsonNode params) {
        String email = params.findValue("email").asText();
        Account account = accountRepository.findByEmail(email);

        return boardRepository.findAllByAccount(account);
    }

    public Board boardDetailView(Long boardId) {
        return boardRepository.findById(boardId).get();
    }

    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

    public Map<String, Object> modifyBoard(JsonNode modifyData) {
        Map<String, Object> rData = new HashMap<>();
        Long boardId = modifyData.findValue("boardId").asLong();
        String content = modifyData.findValue("content").asText();
        Optional<Board> board = boardRepository.findById(boardId);
        board.ifPresentOrElse(modify -> {
            modify.setModified(true);
            modify.setModifiedAt(LocalDateTime.now());
            modify.setContent(content);
            rData.put("success", modify);
        }, () -> rData.put("error", CommonResponse.Code.BAD_REQUEST));
        return rData;
    }

    public Map<String, Object> removeBoard(Long id) {
        Map<String, Object> rData = new HashMap<>();
        Optional<Board> board = boardRepository.findById(id);
        board.ifPresentOrElse(remove -> {
            remove.setActive(false);
            remove.setRemovedAt(LocalDateTime.now());
            rData.put("success", remove);
        }, () -> rData.put("error", CommonResponse.Code.BAD_REQUEST));
        return rData;
    }
}
