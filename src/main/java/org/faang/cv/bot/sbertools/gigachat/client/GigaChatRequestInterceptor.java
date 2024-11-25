package org.faang.cv.bot.sbertools.gigachat.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.faang.cv.bot.generated.tokenprovider.model.Token;
import org.faang.cv.bot.sbertools.provider.AccessTokenProvider;

import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class GigaChatRequestInterceptor implements RequestInterceptor {

    private static final Long VALUES_30_MIN_IN_MILLI = 1800000L;

    private final AccessTokenProvider tokenProvider;
    private final AtomicReference<Token> token = new AtomicReference<>();

    @Override
    public void apply(RequestTemplate template) {
        if (isExpired()) {
            Token t = tokenProvider.getTokenForGigaChat();
            token.set(t);
        }
        template.header("Authorization", "Bearer " + token.get().getAccessToken());
    }

    private boolean isExpired() {
        Token t = token.get();
        if (t == null) {
            return true;
        }
        return System.currentTimeMillis() - t.getExpiresAt() > VALUES_30_MIN_IN_MILLI;
    }
}
