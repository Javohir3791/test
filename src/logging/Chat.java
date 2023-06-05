package logging;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chat {
    private static final String CHAT_LOG_FILE = "chat.log";
    private static final Logger LOGGER = Logger.getLogger(Chat.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("app.log");
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create log file", e);
        }
    }

    public static void sendMessage(User user, String message) {
        LocalDateTime sentTime = LocalDateTime.now();
        String formattedSentTime = sentTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try (FileWriter writer = new FileWriter(CHAT_LOG_FILE, true)) {
            writer.write(formatMessageData(user.getEmail(), formattedSentTime, message) + "\n");
            LOGGER.log(Level.INFO, "Message sent: {0}", message);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write chat log to file", e);
        }
    }

    private static String formatMessageData(String email, String formattedSentTime, String message) {
        return email + "," + formattedSentTime + "," + message;
    }
}