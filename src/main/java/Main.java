import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    private final static Logger log = Logger.getLogger(Main.class);

    private static WebDriver driver;
    private static Properties prop = new Properties();
    private static String loginUrl;
    private static String logoutUrl;

    private static void loadProperties(String propertyFilePath) {
        try (InputStream input = new FileInputStream(propertyFilePath)) {

            prop.load(input);

            if ("Firefox".equals(prop.getProperty("browser")))
                driver = new FirefoxDriver(); // as you can see it's firefox :)
            else
                driver = new PhantomJSDriver(); // load browser without ui

            log.info("browser " + prop.getProperty("browser") + " was chosen");

            loginUrl = prop.getProperty("loginUrlPath");
            log.info("loginUrl : " + loginUrl);

            logoutUrl = prop.getProperty("logoutUrlPath");
            log.info("logoutUrl : " + logoutUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isSeleniumUp() {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://localhost:4444/wd/hub/status")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            String body = response.body().string();
            JSONObject JBody = new JSONObject(body);

            log.info("selenium is ready : " + JBody.getJSONObject("value").get("ready"));

            return (boolean) JBody.getJSONObject("value").get("ready");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void initialCheck(int argsLength) {
        if (argsLength <= 0) {
            log.error("You should provide config file path");
            System.exit(-1);
        }

        if (!isSeleniumUp()) {
            log.error("Please start selenium server first.");
            System.exit(-1);
        }

        // todo check geckodriver

        // todo check PhantomJS and Firefox
    }

    public static void main(String[] args) {

        initialCheck(args.length);

        loadProperties(args[0]);

        // todo get command
        try {

            String expectedTitle = "Instagram";
            String actualTitle = "";

            // launch Fire fox and direct it to the Base URL
            driver.get(loginUrl);
//            driver.manage().window().fullscreen();

            // get the actual value of the title
            actualTitle = driver.getTitle();
            driver.findElement(By.xpath(".//*[@id='react-root']/section/main/article/div[2]/div[2]/p/a")).click();

            Thread.sleep(5000);
            driver.findElement(By.xpath(".//*[@id='react-root']/section/main/article/div[2]/div[1]/div/form/div[1]/div/input")).sendKeys(TempUser.username);
            driver.findElement(By.xpath(".//*[@id='react-root']/section/main/article/div[2]/div[1]/div/form/div[2]/div/input")).sendKeys(TempUser.password);

            driver.findElement(By.id(".//*[@id='react-root']/section/main/article/div[2]/div[1]/div/form/span/button")).click();
            Thread.sleep(5000);

            log.info("I was here :)");

        /*
         * compare the actual title of the page with the expected one and print
         * the result as "Passed" or "Failed"
         */
            if (actualTitle.contentEquals(expectedTitle)) {
                System.out.println("Test Passed!");
            } else {
                System.out.println("Test Failed");
            }

            //close Fire fox
            driver.close();

        } catch (Exception e) {
            e.printStackTrace();
            driver.close();
        }
    }
}
