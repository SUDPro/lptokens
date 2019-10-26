package ru.itis.longpollingtokens.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.longpollingtokens.dto.LoginDto;
import ru.itis.longpollingtokens.dto.TokenDto;
import ru.itis.longpollingtokens.models.Token;
import ru.itis.longpollingtokens.models.User;
import ru.itis.longpollingtokens.repositories.TokenRepository;
import ru.itis.longpollingtokens.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    PasswordEncoder encoder;

    @Value("${token.expired}")
    private Integer expiredSecondsForToken;

    public TokenDto loginByCredentials(LoginDto loginDto) {
        Optional<User> userCandidate = userRepository.findFirstByLoginIgnoreCase(loginDto.getLogin());

        if (userCandidate.isPresent()) {
            User user = userCandidate.get();
            if (encoder.matches(loginDto.getPassword(), user.getPasswordHash())) {
                String value = UUID.randomUUID().toString();
                Token token = Token.builder()
                        .createdAt(LocalDateTime.now())
                        .expiredDateTime(LocalDateTime.now().plusSeconds(expiredSecondsForToken))
                        .value(value)
                        .user(user)
                        .build();
                tokenRepository.save(token);
                return TokenDto.from(value);
            }
        } throw new BadCredentialsException("Incorrect login or password");
    }

}
