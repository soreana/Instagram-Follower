package user;

public class TempUser {
    private static User user;

    static {
        user = new User("samjjv@gmail.com", "A@a12345");
    }

    static User getUser() {
        return user;
    }
}
