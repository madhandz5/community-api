package ryan.community.module.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @PostMapping(value = "/sign-up")
    public Map<String, Object> signUp() {
        Map<String, Object> resultMap = new HashMap<>();

        return resultMap;
    }


}
