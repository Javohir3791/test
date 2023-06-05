package logging;

import java.time.LocalDateTime;

public class User {
    private String email;
    private String password;
    private LocalDateTime registrationTime;

    public User(String email, String password, LocalDateTime registrationTime) {
        this.email = email;
        this.password = password;
        this.registrationTime = registrationTime;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }
}

