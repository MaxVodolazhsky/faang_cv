package org.faang.cv.bot.sbertools.gigachat.client;

import org.faang.cv.bot.generated.gigachatclient.api.GigaChatApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "gigachat-client",
        url = "${gigachat.client.url}",
        configuration = GigaChatClientConfig.class)
public interface GigaChatClient extends GigaChatApi {
}
