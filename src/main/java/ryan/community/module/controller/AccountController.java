package ryan.community.module.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public CommonResponse signUp(@RequestBody JsonNode signUpData) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();

        Account newAccount = accountService.signUp(signUpData);
        rData.put("account", newAccount);

        response.setCode(CommonResponse.Code.SUCCESS);
        response.setrData(rData);

        return response;
    }

    @PostMapping(value = "/email-check-token")
    public CommonResponse verifyEmailCheckToken(@RequestBody JsonNode verifyData) {
        CommonResponse response = new CommonResponse();
        Map<String, Object> rData = new HashMap<>();
        Account newAccount = accountService.verifyEmailCheckToken(verifyData);

        if(newAccount == null) {
            response.setCode(CommonResponse.Code.ALREADY_VERIFIED_EMAIL);
        }else{
            if (newAccount.isEmailVerified()) {
                rData.put("account", newAccount);
                response.setCode(CommonResponse.Code.SUCCESS);
                response.setrData(rData);
            } else {
                response.setCode(CommonResponse.Code.CHECK_EMAIL_TOKEN_MISMATCH);
            }
        }
        return response;
    }


}
