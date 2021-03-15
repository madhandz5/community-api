package ryan.community.module.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryan.community.infra.util.CommonResponse;
import ryan.community.module.domain.Account;
import ryan.community.module.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
                    .isExit(false)
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

    public Map<String, Object> login(JsonNode loginData) {
        Map<String, Object> rData = new HashMap<>();
        String email = loginData.findValue("email").asText();
        String password = loginData.findValue("password").asText();
        if (!accountRepository.existsByEmail(email)) {
            rData.put("email", CommonResponse.Code.USER_NOT_EXIST);
        } else {
            Account account = accountRepository.findByEmail(email);
            if (passwordEncoder.matches(password, account.getPassword())) {
                rData.put("account", account);
            } else {
                rData.put("password", CommonResponse.Code.INVALID_PASSWORD);
            }
        }
        return rData;
    }

    public Map<String, Object> removeAccount(String email) {
        Map<String, Object> rData = new HashMap<>();
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            account.setExit(true);
            account.setExitAt(LocalDateTime.now());
            rData.put("success", account);
        } else {
            rData.put("error", CommonResponse.Code.BAD_REQUEST);
        }
        return rData;
    }

    public Map<String, Object> modifyAccount(JsonNode accountData) {
        Map<String, Object> rData = new HashMap<>();
        String email = accountData.findValue("email").asText();
        String username = accountData.findValue("username").asText();
        String nickname = accountData.findValue("nickname").asText();
        String password = accountData.findValue("password").asText();

        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            if (accountRepository.existsByNickname(nickname)) {
                rData.put("nickname", CommonResponse.Code.USED_NICKNAME);
            } else {
                account.setUsername(username);
                account.setNickname(nickname);
                account.setPassword(passwordEncoder.encode(password));
                rData.put("success", account);
            }
        } else {
            rData.put("error", CommonResponse.Code.BAD_REQUEST);
        }
        return rData;
    }
}
