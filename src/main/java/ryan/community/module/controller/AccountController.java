package ryan.community.module.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ryan.community.infra.util.CommonResponse;
import ryan.community.module.domain.Account;
import ryan.community.module.service.AccountService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(@RequestBody JsonNode signUpData) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();

        Map<String, Object> result = accountService.signUp(signUpData);
        if (result.containsKey("account")) {
            rData.put("account", result.get("account"));
            response.setCode(CommonResponse.Code.SUCCESS);
            response.setrData(rData);
        }else if(result.containsKey("email")){
            response.setCode(CommonResponse.Code.USED_EMAIL);
        }else if(result.containsKey("nickname")){
            response.setCode(CommonResponse.Code.USED_NICKNAME);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/email-check-token")
    public ResponseEntity<?> verifyEmailCheckToken(@RequestBody JsonNode verifyData) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        Account newAccount = accountService.verifyEmailCheckToken(verifyData);

        if (newAccount == null) {
            response.setCode(CommonResponse.Code.ALREADY_VERIFIED_EMAIL);
        } else {
            if (newAccount.isEmailVerified()) {
                rData.put("account", newAccount);
                response.setCode(CommonResponse.Code.SUCCESS);
                response.setrData(rData);
            } else {
                response.setCode(CommonResponse.Code.CHECK_EMAIL_TOKEN_MISMATCH);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/log-in")
    public ResponseEntity<?> login(@RequestBody JsonNode loginData) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        Account account = accountService.login(loginData);
        if (account != null) {
            rData.put("account", account);
            response.setCode(CommonResponse.Code.SUCCESS);
            response.setrData(rData);
        } else {
            response.setCode(CommonResponse.Code.EMAIL_PASSWORD_MISMATCH);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody JsonNode jsonNode) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        response.setCode(CommonResponse.Code.SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
