package ryan.community.module.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryan.community.module.domain.Account;
import ryan.community.module.domain.Board;
import ryan.community.module.repository.AccountRepository;
import ryan.community.module.repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                .build();
        account.getBoardList().add(board);
        return boardRepository.save(board);
    }

    public List<Board> findArticleByAuthor(JsonNode params) {
        String email = params.findValue("email").asText();
        Account account = accountRepository.findByEmail(email);

        return boardRepository.findAllByAccount(account);
    }
}
