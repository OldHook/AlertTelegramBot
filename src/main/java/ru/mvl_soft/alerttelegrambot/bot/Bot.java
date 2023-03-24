package ru.mvl_soft.alerttelegrambot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public final class Bot extends TelegramLongPollingBot {
    private Long chatId;

    public Bot(final String token, final Long chatIdentifier) {
        super(token);
        if (token == null) {
            throw new RuntimeException("Bot token must be specified.");
        }
        this.chatId = chatIdentifier;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        System.out.println(update.getMessage());

        Message message = update.getMessage();
        User user = message.getFrom();
        String userName = user.getUserName();
        if (userName.equalsIgnoreCase("morozovvl")) {
            chatId = update.getMessage().getChatId();
            sendMessage("chatId registered for " + user);
        }
    }

    @Override
    public String getBotUsername() {
        return "MVL_AlertBot"; // TODO Вынести во внешние настройки
    }

    public void sendMessage(final String message) {
        if (chatId == null) {
            return;
        }
        // ToDo message не доолно превышать 4096 символов.

        SendMessage sm = SendMessage.builder()
                .chatId(chatId)
                .text(message).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.err.println("ERROR:" + e.getMessage());
        }

        //SendAnimation
        //SendAudio
        //SendContact
        //SendDice
        //SendChatAction
        //SendDocument
        //SendGame
        //SendLocation
        //SendMediaGroup
        //SendPhoto
        //SendSticker
        //SendVenue
        //SendVideo
        //SendVideoNote
        //SendVoice
        //SendInvoice
        //SendPoll
        // + builders
    }
}
