import instagram.InstagramBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import selenium.SeleniumUtils;


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

        InstagramBot instagramBot = new InstagramBot(args[0]);

        instagramBot.login();

        Thread.sleep(2000);

        instagramBot.close();
    }
}
