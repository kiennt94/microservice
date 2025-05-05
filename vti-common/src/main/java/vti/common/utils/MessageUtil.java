package vti.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtil {

    private final MessageSource messageSource;

    @Autowired
    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    public String getMessage(String key, Object[] args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}
