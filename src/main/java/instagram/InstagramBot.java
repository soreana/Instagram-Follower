package instagram;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class InstagramBot {
    private final static Logger log = LogManager.getLogger(InstagramBot.class);

    private static WebDriver driver;
    private Map<String, String> properties = new HashMap<>();

    // todo get user as an argument
    public InstagramBot(String propertyFilePath) {

        try (InputStream input = new FileInputStream(propertyFilePath)) {
            Properties prop = new Properties();

            prop.load(input);

            if ("Firefox".equals(prop.getProperty("instagram.browser")))
                driver = new FirefoxDriver(); // as you can see it's firefox :)
            else
                driver = new PhantomJSDriver(); // load browser without ui

            log.info("browser " + prop.getProperty("browser") + " was chosen");

            prop.keySet()
                    .stream()
                    .map(e -> (String) e)
                    .filter(e -> e.contains("instagram"))
                    .forEach(e -> properties.put(e, prop.getProperty(e)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login() {

        driver.get(properties.get("instagram.login.urlPath"));

        driver.findElement(By.xpath(properties.get("instagram.login.xpath.login"))).click();

        driver.findElement(By.xpath(properties.get("instagram.login.xpath.username"))).sendKeys("");

        driver.findElement(By.xpath(properties.get("instagram.login.xpath.password"))).sendKeys("");

        driver.findElement(By.xpath(properties.get("instagram.login.xpath.button"))).click();
    }

    public void close(){
        driver.close();
    }
}
