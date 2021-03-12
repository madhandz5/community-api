package ryan.community.module.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryan.community.infra.util.CommonResponse;
import ryan.community.module.domain.Account;
import ryan.community.module.repository.AccountRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> signUp(JsonNode signUpData) {
        Map<String, Object> rData = new HashMap<>();
        String email = signUpData.findValue("email").asText();
        String password = signUpData.findValue("password").asText();
        String nickname = signUpData.findValue("nickname").asText();
        String username = signUpData.findValue("username").asText();

        if (accountRepository.existsByEmail(email)) {
            rData.put("email", CommonResponse.Code.USED_EMAIL);
        } else if (accountRepository.existsByNickname(nickname)) {
            rData.put("nickname", CommonResponse.Code.USED_NICKNAME);
        } else {
            Account account = Account.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .username(username)
                    .build();
            account.generateEmailToken();
            rData.put("account", accountRepository.save(account));
        }
        return rData;
    }

    public Account verifyEmailCheckToken(JsonNode verifyData) {
        String email = verifyData.findValue("email").asText();
        String emailCheckToken = verifyData.findValue("emailCheckToken").asText();
        Account account = accountRepository.findByEmail(email);
        if (account.isEmailVerified()) {
            return null;
        }

        if (account.isValidToken(emailCheckToken)) {
            account.completeSignUp();
            accountRepository.save(account);
        }
        return account;
    }

    public Account login(JsonNode loginData) {
        String email = loginData.findValue("email").asText();
        String password = loginData.findValue("password").asText();
        Account account = accountRepository.findByEmail(email);
        if (passwordEncoder.matches(password, account.getPassword())) {
            return account;
        } else {
            return null;
        }
    }
}
