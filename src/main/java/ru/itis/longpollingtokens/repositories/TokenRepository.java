package ru.itis.longpollingtokens.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.longpollingtokens.models.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findFirstByValue(String value);
}
