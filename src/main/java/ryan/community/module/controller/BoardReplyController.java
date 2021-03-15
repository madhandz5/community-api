package ryan.community.module.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ryan.community.infra.util.CommonResponse;
import ryan.community.module.domain.BoardReply;
import ryan.community.module.service.BoardReplyService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/board-reply")
public class BoardReplyController {
    private final BoardReplyService boardReplyService;

    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertBoardReply(@RequestBody JsonNode replayData) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();

        BoardReply boardReply = boardReplyService.insertBoardReply(replayData);
        rData.put("boardReply", boardReply);

        response.setCode(CommonResponse.Code.SUCCESS);
        response.setrData(rData);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/modify")
    public ResponseEntity<?> modifyBoardReply(@RequestBody JsonNode modifyData) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        Map<String, Object> result = boardReplyService.modifyBoardReply(modifyData);
        if (result.containsKey("error")) {
            response.setCode(CommonResponse.Code.BAD_REQUEST);
        } else {
            rData.put("boardReply", result.get("boardReply"));
            response.setCode(CommonResponse.Code.SUCCESS);
            response.setrData(rData);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/remove/{id}")
    public ResponseEntity<?> removeBoardReply(@PathVariable Long id) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        Map<String, Object> result = boardReplyService.removeBoardReply(id);
        if (result.containsKey("success")) {
            response.setCode(CommonResponse.Code.SUCCESS);
            rData.put("success", result.get("success"));
            response.setrData(rData);
        } else if (result.containsKey("error")) {
            response.setCode(CommonResponse.Code.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
