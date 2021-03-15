package ryan.community.module.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ryan.community.infra.util.CommonResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/")
public class MainController {
    @GetMapping
    public ResponseEntity<?> home() {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        rData.put("Status", "Server OK");
        response.setCode(CommonResponse.Code.SUCCESS);
        response.setrData(rData);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
