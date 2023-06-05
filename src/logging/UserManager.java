package logging;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class UserManager {
    private static final String USER_FILE = "users.txt";
    private static final Logger LOGGER = Logger.getLogger(UserManager.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("app.log");
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create log file", e);
        }
    }

    public static void registerUser(String email, String password) {
        if (!isValidEmail(email)) {
            LOGGER.log(Level.WARNING, "Invalid email address: {0}", email);
            return;
        }

        if (isUserRegistered(email)) {
            LOGGER.log(Level.WARNING, "User with email {0} is already registered", email);
            return;
        }

        LocalDateTime registrationTime = LocalDateTime.now();

        try (FileWriter writer = new FileWriter(USER_FILE, true)) {
            writer.write(formatUserData(email, password, registrationTime) + "\n");
            LOGGER.log(Level.INFO, "User with email {0} has been registered", email);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write user data to file", e);
        }
    }

    public static User login(String email, String password) {
        if (!isValidEmail(email)) {
            LOGGER.log(Level.WARNING, "Invalid email address: {0}", email);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(email)) {
                    if (userData[1].equals(password)) {
                        LocalDateTime registrationTime = LocalDateTime.parse(userData[2], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        LOGGER.log(Level.INFO, "User with email {0} has logged in", email);
                        return new User(email, password, registrationTime);
                    } else {
                        LOGGER.log(Level.WARNING, "Incorrect password for user with email {0}", email);
                        return null;
                    }
                }
            }
            LOGGER.log(Level.WARNING, "User with email {0} is not registered", email);
            return null;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read user data from file", e);
            return null;
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private static boolean isUserRegistered(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(email)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read user data from file", e);
            return false;
        }
    }

    private static String formatUserData(String email, String password, LocalDateTime registrationTime) {
        String formattedRegistrationTime = registrationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return email + "," + password + "," + formattedRegistrationTime;
    }
}