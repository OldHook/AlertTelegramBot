package ru.mvl_soft.alerttelegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class EnvVarProvider {

    private final static String TOKEN_VAR_NAME = "BOT_TOKEN";
    private final static String CHAT_ID_VAR_NAME = "CHAT_ID";
    private final static String SMTP_PORT_VAR_NAME = "SMTP_PORT";
    private final static int DEFAULT_SMTP_PORT = 25000;
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, String> variables;

    public EnvVarProvider() {
        logger.debug("EnvVarProvider creating.");
        variables = new HashMap<>();
        System.getenv().forEach((key, value) -> variables.put(key.toUpperCase(), value));
        if (getVariable(TOKEN_VAR_NAME).isEmpty()) {
            throw new RuntimeException("Не передан или передан пустым обязательный параметр " + TOKEN_VAR_NAME);
        }
        if (getVariable(CHAT_ID_VAR_NAME).isEmpty()) {
            throw new RuntimeException("Не передан или передан пустым обязательный параметр " + CHAT_ID_VAR_NAME);
        }
        if (getVariable(SMTP_PORT_VAR_NAME).isEmpty()) {
            logger.warn("Не передан параметр {}. Будет установлен порт по-умолчанию {}.", SMTP_PORT_VAR_NAME, DEFAULT_SMTP_PORT);
            variables.remove(SMTP_PORT_VAR_NAME);
            variables.put(SMTP_PORT_VAR_NAME, Integer.toString(DEFAULT_SMTP_PORT));
        }
        logger.debug("EnvVarProvider created.");
    }

    public String getVariable(final String varName) {
        return variables.getOrDefault(varName.toUpperCase(), "");
    }

    public String getToken() {
        return getVariable(TOKEN_VAR_NAME);
    }

    public Long getChatId() {
        return Long.parseLong(getVariable(CHAT_ID_VAR_NAME));
    }

    public int getSmtpPort() {
        return Integer.parseInt(getVariable(SMTP_PORT_VAR_NAME));
    }
}
