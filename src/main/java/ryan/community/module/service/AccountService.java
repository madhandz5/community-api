package ryan.community.module.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryan.community.module.domain.Account;
import ryan.community.module.repository.AccountRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account signUp(JsonNode signUpData) {
        Account account = Account.builder()
                .email(signUpData.findValue("email").asText())
                .password(passwordEncoder.encode(signUpData.findValue("password").asText()))
                .nickname(signUpData.findValue("nickname").asText())
                .username(signUpData.findValue("username").asText())
                .build();
        account.generateEmailToken();
        return accountRepository.save(account);
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
}
