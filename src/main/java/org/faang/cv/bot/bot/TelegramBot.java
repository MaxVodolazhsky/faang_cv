package org.faang.cv.bot.bot;

import org.faang.cv.bot.config.BotProps;
import org.faang.cv.bot.jpa.model.ChatEntity;
import org.faang.cv.bot.jpa.model.states.ChatState;
import org.faang.cv.bot.jpa.repository.ChatRepository;
import org.faang.cv.bot.sbertools.gigachat.client.GigaChatClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotProps botConfig;
    private final ChatRepository chatRepository;
    private final GigaChatClient gigaChatClient;

    public TelegramBot(BotProps botConfig, ChatRepository chatRepository, GigaChatClient gigaChatClient) {
        this.botConfig = botConfig;
        this.chatRepository = chatRepository;
        this.gigaChatClient = gigaChatClient;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    @Transactional
    public void onUpdateReceived(Update update) {
    }

    private void init(Long chatId) {
        ChatEntity entity = new ChatEntity();
        entity.setChatId(chatId);
        entity.setState(ChatState.WELCOME);
        chatRepository.save(entity);
        sendMessage(chatId, "Добро пожаловать в генератор CV.");
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
