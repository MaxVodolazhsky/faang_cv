package org.faang.cv.bot.sbertools.provider;

import lombok.RequiredArgsConstructor;
import org.faang.cv.bot.generated.tokenprovider.model.Token;
import org.faang.cv.bot.sbertools.gigachat.token.AccessTokenGigaChatClient;
import org.faang.cv.bot.sbertools.salutespeech.token.AccessTokenSaluteSpeechClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccessTokenProvider {

    private final AccessTokenGigaChatClient tokenGigaChatClient;
    private final AccessTokenSaluteSpeechClient tokenSaluteSpeechClient;

    public Token getTokenForGigaChat() {
        return tokenGigaChatClient.getToken(UUID.randomUUID().toString());
    }

    public Token getTokenForSaluteSpeech() {
        return tokenSaluteSpeechClient.getToken(UUID.randomUUID().toString());
    }
}
