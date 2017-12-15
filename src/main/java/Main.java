import instagram.InstagramFollowerGirAgent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import selenium.SeleniumUtils;
import user.FollowingUser;
import user.User;
import user.UserManager;

public class Main {
    private final static Logger log = LogManager.getLogger(Main.class);

    private static void initialCheck(int argsLength) {
        if (argsLength <= 0) {
            log.error("You should provide config file path");
            System.exit(-1);
        }

        if (!SeleniumUtils.isSeleniumUp()) {
            log.error("Please start selenium server first.");
            System.exit(-1);
        }

        // todo check PhantomJS

        // todo check Firefox
    }

    public static void main(String[] args) throws InterruptedException {

        initialCheck(args.length);
        User user = UserManager.getInstance().getUser("samjjv@gmail.com");

        InstagramFollowerGirAgent instagramBot = new InstagramFollowerGirAgent(args[0],user);

        instagramBot.login();

        instagramBot.follow(new FollowingUser("amirali313"));

        Thread.sleep(5000);

        instagramBot.unFollow(new FollowingUser("amirali313"));

        Thread.sleep(5000);

        instagramBot.close();
    }
}
