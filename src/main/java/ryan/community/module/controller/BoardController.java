package ryan.community.module.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ryan.community.infra.util.CommonResponse;
import ryan.community.module.domain.Board;
import ryan.community.module.service.BoardService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/insert")
    public CommonResponse insertArticle(@RequestBody JsonNode articleData) {
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
        return response;
    }

    @PostMapping(value = "/find")
    public CommonResponse findArticleByAuthor(@RequestBody JsonNode params) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        List<Board> boardList = boardService.findArticleByAuthor(params);
        List<Long> boardIdList = new ArrayList<>();
        for (Board board : boardList) {
            boardIdList.add(board.getId());
        }
        rData.put("boardList", boardIdList);
        response.setCode(CommonResponse.Code.SUCCESS);
        response.setrData(rData);
        return response;
    }
}
