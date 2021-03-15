package ryan.community.module.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ryan.community.infra.util.CommonResponse;
import ryan.community.module.domain.Board;
import ryan.community.module.service.BoardService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertArticle(@RequestBody JsonNode articleData) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        Board newBoard = boardService.insertArticle(articleData);
        if (newBoard != null) {
            response.setCode(CommonResponse.Code.SUCCESS);
            rData.put("title", newBoard.getTitle());
            rData.put("content", newBoard.getContent());
            rData.put("author", newBoard.getAccount().getNickname());
            rData.put("createdAt", newBoard.getCreatedAt());
            response.setrData(rData);
        } else {
            response.setCode(CommonResponse.Code.EXCEPTION);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/find")
    public ResponseEntity<?> findArticleByAuthor(@RequestBody JsonNode params) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        List<Board> boardList = boardService.findArticleByAuthor(params);

        rData.put("boardList", boardList);
        response.setCode(CommonResponse.Code.SUCCESS);
        response.setrData(rData);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/{boardId}")
    public ResponseEntity<?> boardDetailView(@PathVariable Long boardId) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();

        Board board = boardService.boardDetailView(boardId);
        rData.put("board", board);
        response.setCode(CommonResponse.Code.SUCCESS);
        response.setrData(rData);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<?> getBoardList() {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        rData.put("boardList", boardService.getBoardList());
        response.setCode(CommonResponse.Code.SUCCESS);
        response.setrData(rData);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/modify")
    public ResponseEntity<?> modifyBoard(@RequestBody JsonNode modifyData) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        Map<String, Object> result = boardService.modifyBoard(modifyData);
        if (result.containsKey("error")) {
            response.setCode(CommonResponse.Code.BAD_REQUEST);
        } else if (result.containsKey("success")) {
            rData.put("success", result.get("success"));
            response.setCode(CommonResponse.Code.SUCCESS);
            response.setrData(rData);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/remove/{id}")
    public ResponseEntity<?> removeBoard(@PathVariable Long id) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        Map<String, Object> result = boardService.removeBoard(id);
        if (result.containsKey("error")) {
            response.setCode(CommonResponse.Code.BAD_REQUEST);
        } else if (result.containsKey("success")) {
            rData.put("success", result.get("success"));
            response.setCode(CommonResponse.Code.SUCCESS);
            response.setrData(rData);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
