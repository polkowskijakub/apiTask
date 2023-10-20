package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.RequestOptions;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class App {
    public static void main(String[] args) {

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();
            page.navigate("https://finance.yahoo.com/chart/NVDA");

            assertThat(page).hasTitle(Pattern.compile("Yahoo is part of the Yahoo family of brands"));
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept all"))
                    .click();

            assertThat(page.getByText("NVIDIA Corporation (NVDA)")).isVisible();
            assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("3M"))).isVisible();


            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("3M"))
                    .click();

            ElementHandle element = page.querySelector("[data-test=qsp-price]");
            String value = element.getAttribute("value");
            Double db = Double.valueOf(value);
    //        System.out.println("Value attribute: " + value);
            Double apiValue = getRegularMarketPrice();
            assertEquals(apiValue, db);
            browser.close();
            playwright.close();

        }

    }

    public static Double getRegularMarketPrice()
    {
        Playwright playwright2 = Playwright.create();
        APIRequest request = playwright2.request();
        APIRequestContext apiRequestContext = request.newContext();
        Map<String, String> headers = new HashMap<>();
        headers.put("X-RapidAPI-Key", "9b534b4f73msh25841653c29e525p18623ejsnc2d7a6a587e5");
        headers.put("X-RapidAPI-Host", "apidojo-yahoo-finance-v1.p.rapidapi.com");
        headers.put("Content-Type", "application/json");

        String baseUrl = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v3/get-chart";

        apiRequestContext = playwright2.request()
                .newContext(new APIRequest.NewContextOptions().setBaseURL(baseUrl)
                        .setExtraHTTPHeaders(headers));
        APIResponse response = apiRequestContext.get(baseUrl, RequestOptions.create()
                .setQueryParam("interval","1Y")
                .setQueryParam("symbol", "NVDA")
                .setQueryParam("range", "1Y")
        );
        int responseCode=response.status();
        assertEquals(200,responseCode);


        String responseText = response.text();
      //  System.out.println(responseCode);
     //   System.out.println(responseText);


        JSONObject jsonObject = new JSONObject(responseText);

        double regularMarketPrice = jsonObject
                .getJSONObject("chart")
                .getJSONArray("result")
                .getJSONObject(0)
                .getJSONObject("meta")
                .getDouble("regularMarketPrice");


        playwright2.close();
        return regularMarketPrice;
    }
}