package instagram;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import user.FollowingUser;
import user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class InstagramFollowerGirAgent {
    private final static Logger log = LogManager.getLogger(InstagramFollowerGirAgent.class);

    private final WebDriver driver;
    private final User user;
    private final WebDriverWait wait;

    private Map<String, String> properties = new HashMap<>();

    public InstagramFollowerGirAgent(String propertyFilePath, User user) {
        this.user = user;

        try (InputStream input = new FileInputStream(propertyFilePath)) {
            Properties prop = new Properties();

            prop.load(input);

            if ("Firefox".equals(prop.getProperty("instagram.browser")))
                driver = new FirefoxDriver(); // as you can see it's firefox :)
            else {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setJavascriptEnabled(true);
                caps.setCapability("takesScreenshot", true);
                driver = new PhantomJSDriver(caps); // load browser without ui
            }

            driver.manage().window().setSize(new Dimension(400, 300));

            wait = new WebDriverWait(driver, 30);

            log.info("browser {} was chosen.", () -> prop.getProperty("instagram.browser"));

            prop.keySet()
                    .stream()
                    .map(e -> (String) e)
                    .filter(e -> e.contains("instagram"))
                    .forEach(e -> properties.put(e, prop.getProperty(e)));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void login() {
        driver.get(properties.get("instagram.login.urlPath"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(properties.get("instagram.login.xpath.button"))));

        driver.findElement(By.xpath(properties.get("instagram.login.xpath.username"))).sendKeys(user.getUsername());
        driver.findElement(By.xpath(properties.get("instagram.login.xpath.password"))).sendKeys(user.getPassword());
        driver.findElement(By.xpath(properties.get("instagram.login.xpath.button"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(properties.get("instagram.login.xpath.test"))));
    }

    private void takeScreenShot() {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            System.out.println("File:" + srcFile);
            FileUtils.copyFile(srcFile, new File("./target/screenshot_.png"));
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MutableInt {
        int value;

        MutableInt(int i) {
            value = i;
        }

        static MutableInt valueOf(int i) {
            return new MutableInt(i);
        }
    }

    private boolean isFollowed() {
        try {
            return !driver.findElement(By.xpath(properties.get("instagram.following.user.xpath.followButton"))).getText().equals("Follow");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return true;
        }
    }

    private void followRecursive(FollowingUser target, MutableInt depth, MutableInt max) throws InterruptedException {
        driver.get(properties.get("instagram.base.urlPath") + target.getUsername());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(properties.get("instagram.following.user.xpath.followButton"))));
        log.info("username: {} followed : {} ", target::getUsername, this::isFollowed);

        if (!isFollowed()) {
            clickOnFollowOrFollowingButton();
            log.info("following {}.", target.getUsername());
        }
        // todo choose some of it's follower randomly
        // todo recursive call on that targeted users
    }

    public void follow(FollowingUser target) throws InterruptedException {
        int depth = Integer.parseInt(properties.get("instagram.following.max.depth"));
        int number = Integer.parseInt(properties.get("instagram.following.max.number"));
        followRecursive(target, MutableInt.valueOf(depth), MutableInt.valueOf(number));
    }

    private void clickOnFollowOrFollowingButton(){
        driver.findElement(By.xpath(properties.get("instagram.following.user.xpath.followButton"))).click();
        wait.until(ExpectedConditions.textToBe(By.xpath(properties.get("instagram.following.user.xpath.followButton")), "Following"));
    }

    public void unFollow(FollowingUser target) {
        driver.get(properties.get("instagram.base.urlPath") + target.getUsername());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(properties.get("instagram.following.user.xpath.followButton"))));

        if (isFollowed()) {
            clickOnFollowOrFollowingButton();
            log.info("unFollowing {}.", target.getUsername());
        }
    }

    public void close() {
        driver.close();
    }
}
