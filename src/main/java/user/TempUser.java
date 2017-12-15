package user;

public class TempUser {

    private static User user;

    static {
        user = new User("samjjv@gmail.com", "");
    }

    static User getUser() {
        return user;
    }
}
