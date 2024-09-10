package com.aston.infoBoardRestService.util;

import com.aston.infoBoardRestService.entity.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class MessageGenerator {
    private static final List<String> MESSAGE_TEMPLATES = List.of(
            "Hello, this is a message from %s.",
            "Just wanted to say hi from %s!",
            "This is a random message from %s.",
            "Message from %s: Have a great day!",
            "Greetings from %s!",
            "Here's a message for you from %s.",
            "Just checking in with a message from %s.",
            "A friendly reminder from %s.",
            "This is a message to brighten your day from %s.",
            "Don't forget to smile! - %s",
            "%s, whats up?!?!111",
            "%s, whats good bro"
    );

    private static final Random RANDOM = new Random();

    public static Message generateMessage(Long authorId, String authorName) {
        String content = generateRandomMessage(authorName);
        LocalDateTime timestamp = LocalDateTime.now();
        return new Message(authorId, content, authorName, timestamp);
    }

    private static String generateRandomMessage(String authorName) {
        String template = MESSAGE_TEMPLATES.get(RANDOM.nextInt(MESSAGE_TEMPLATES.size()));
        return String.format(template, authorName);
    }
}