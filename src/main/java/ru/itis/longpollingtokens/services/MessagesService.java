package ru.itis.longpollingtokens.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.longpollingtokens.dto.MessageDto;
import ru.itis.longpollingtokens.models.Message;
import ru.itis.longpollingtokens.models.Token;
import ru.itis.longpollingtokens.repositories.MessageRepository;
import ru.itis.longpollingtokens.repositories.TokenRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessagesService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    TokenRepository tokenRepository;


    public void save(MessageDto messageDto) {
        Optional<Token> tokenCandidate = tokenRepository.findFirstByValue(messageDto.getToken());
        if (tokenCandidate.isPresent()) {
            Token token = tokenCandidate.get();
            Message message = Message.builder()
                    .text(messageDto.getText())
                    .user(token.getUser())
                    .build();
            messageRepository.save(message);
        } else {
            throw new IllegalArgumentException();
        }

    }

    public List<MessageDto> getAllMessages() {
        List<MessageDto> messageDtos = messageRepository.findAll().stream().map(message -> MessageDto.builder()
                .userName(message.getUser().getLogin())
                .text(message.getText())
                .build()).collect(Collectors.toList());
        return messageDtos;
    }
}
