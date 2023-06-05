package logging;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static {
        try {
            var fileHandler = new FileHandler("app.log");
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create log file", e);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        LOGGER.log(Level.INFO, "Enter your email address:");
        String email = scanner.nextLine();

        LOGGER.log(Level.INFO, "Enter your password:");
        String password = scanner.nextLine();

        UserManager.registerUser(email, password);
        User user = UserManager.login(email, password);

        if (user != null) {
            LOGGER.log(Level.INFO, "Enter your message:");
            String message = scanner.nextLine();

            Chat.sendMessage(user, message);
        }

        scanner.close();
    }
}