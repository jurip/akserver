package com.company.aab.app;

import com.company.aab.entity.Invite;
import io.jmix.core.UnconstrainedDataManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final UnconstrainedDataManager unconstrainedDataManager;

    public TelegramBot(UnconstrainedDataManager unconstrainedDataManager) {
        telegramClient = new OkHttpTelegramClient(getBotToken());
        this.unconstrainedDataManager = unconstrainedDataManager;
    }

    @Override
    public String getBotToken() {
        //прод
       // return "7332996600:AAEysrKvQKQDMyu6zkOccJHvYOuEvv37ofo";
        return "7003594932:AAGmIWHcmVaHyvW12j2fS8FpBdj2hS8o4q4";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            Invite inv = unconstrainedDataManager.create(Invite.class);
            unconstrainedDataManager.save(inv);// Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            SendMessage message = SendMessage // Create a message object
                    .builder()
                    .chatId(chat_id)
                    .text(inv.getId().toString())
                    .build();
            try {
                telegramClient.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}