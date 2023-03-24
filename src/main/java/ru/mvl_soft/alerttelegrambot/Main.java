package ru.mvl_soft.alerttelegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.server.SMTPServer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.mvl_soft.alerttelegrambot.bot.Bot;
import ru.mvl_soft.alerttelegrambot.smtpserver.MyMessageHandlerFactory;

public final class Main {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(final String[] args) throws TelegramApiException {
        new Main().start();
    }

    public void start() throws TelegramApiException {
        logger.info("Starting.");

        final EnvVarProvider varProvider = new EnvVarProvider();

        logger.debug("Telegram bot initializing.");
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot(varProvider.getToken(), varProvider.getChatId());
        botsApi.registerBot(bot);
        logger.debug("Telegram bot initialized.");

        logger.debug("SMTP server initializing.");
        MyMessageHandlerFactory myFactory = new MyMessageHandlerFactory(bot);
        SMTPServer smtpServer = SMTPServer.port(varProvider.getSmtpPort()).messageHandlerFactory(myFactory).build();
        smtpServer.start();
        logger.debug("SMTP server initialized.");

        logger.info("Started.");
    }
}
