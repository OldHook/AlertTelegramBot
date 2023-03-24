package ru.mvl_soft.alerttelegrambot.smtpserver;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import ru.mvl_soft.alerttelegrambot.bot.Bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyMessageHandlerFactory implements MessageHandlerFactory {

    private final Bot bot;

    public MyMessageHandlerFactory(Bot bot) {
        this.bot = bot;
    }

    public MessageHandler create(MessageContext ctx) {
        return new Handler(ctx, bot);
    }

    class Handler implements MessageHandler {
        MessageContext ctx;
        Bot bot;
        String message;

        public Handler(MessageContext ctx, Bot bot) {
            this.ctx = ctx;
            this.bot = bot;
        }

        public void from(String from) {
            message = "Письмо от " + from;
        }

        public void recipient(String recipient) {
            message += " для " + recipient + "\n";
        }


        private String cleanupMailBody(final String msg) {
            StringBuilder b = new StringBuilder();
            for (String s : msg.split(System.lineSeparator())) {
                b.append(s).append(System.lineSeparator());
            }
            return b.toString();
        }

        public String data(InputStream data) {
            final String body = convertStreamToString(data) + "\n";
            message += cleanupMailBody(body);
            System.out.println(message);
            return "OK";
        }

        public void done() {
            bot.sendMessage(message.substring(0, Math.min(4096, message.length())));
        }

        public String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

    }
}
