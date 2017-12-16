package selenium;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

public interface SeleniumUtils {
    Logger log = LogManager.getLogger(SeleniumUtils.class);
    String seleniumTestURL= "http://localhost:4444/wd/hub/status";

    static boolean isSeleniumUp() {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(seleniumTestURL)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            String body = response.body().string();
            JSONObject JBody = new JSONObject(body);

            log.info("selenium is ready : {}" , () -> JBody.getJSONObject("value").get("ready"));

            return (boolean) JBody.getJSONObject("value").get("ready");

        } catch (IOException e) {
            return false;
        }
    }
}
