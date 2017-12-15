package user;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static Map<String,User> users = new HashMap<>();
    private static UserManager userManager = new UserManager();

    // todo read users from db
    static {
        User user = TempUser.getUser();
        users.put(user.getUsername(),user);
    }

    public static UserManager getInstance() {
        return userManager;
    }

    public User getUser(String username){
        return users.get(username);
    }
}
