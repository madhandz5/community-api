package ryan.community.module.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryan.community.infra.util.CommonResponse;
import ryan.community.module.domain.Account;
import ryan.community.module.domain.Board;
import ryan.community.module.domain.BoardReply;
import ryan.community.module.repository.AccountRepository;
import ryan.community.module.repository.BoardReplyRepository;
import ryan.community.module.repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardReplyService {
    private final BoardReplyRepository boardReplyRepository;
    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;

    public BoardReply insertBoardReply(JsonNode replayData) {
        String content = replayData.findValue("content").asText();
        String email = replayData.findValue("email").asText();
        Account account = accountRepository.findByEmail(email);

        Long boardId = replayData.findValue("boardId").asLong();
        Optional<Board> board = boardRepository.findById(boardId);

        BoardReply boardReply = BoardReply.builder()
                .contents(content)
                .isActive(true)
                .account(account)
                .board(board.orElse(null))
                .createAt(LocalDateTime.now())
                .build();
        board.get().getReplyList().add(boardReply);
        account.getBoardReplyList().add(boardReply);
        return boardReplyRepository.save(boardReply);
    }

    public Map<String, Object> modifyBoardReply(JsonNode modifyData) {
        Map<String, Object> rData = new HashMap<>();
        Long replyId = modifyData.findValue("replyId").asLong();
        String content = modifyData.findValue("content").asText();

        Optional<BoardReply> boardReply = boardReplyRepository.findById(replyId);

        boardReply.ifPresentOrElse(modifiedReply -> {
            modifiedReply.setContents(content);
            modifiedReply.setModified(true);
            modifiedReply.setModifiedAt(LocalDateTime.now());
            rData.put("boardReply", modifiedReply);
        }, () -> rData.put("error", CommonResponse.Code.BAD_REQUEST));

        return rData;
    }

    public Map<String, Object> removeBoardReply(Long id) {
        Map<String, Object> rData = new HashMap<>();
        Optional<BoardReply> boardReply = boardReplyRepository.findById(id);
        boardReply.ifPresentOrElse(removeReply -> {
            removeReply.setActive(false);
            removeReply.setRemovedAt(LocalDateTime.now());
            rData.put("success", removeReply);
        }, () -> {
            rData.put("error", CommonResponse.Code.BAD_REQUEST);
        });
        return rData;
    }
}
