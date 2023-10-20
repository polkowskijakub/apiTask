package org.example.helpers;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class apiCalls {

    private static String xRapidApiKey = "9b534b4f73msh25841653c29e525p18623ejsnc2d7a6a587e5";
    private static String xRapidApiHost = "apidojo-yahoo-finance-v1.p.rapidapi.com";
    private static String baseUrl = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/";


    public static Double getRegularMarketPrice(String interval, String symbol, String range)
    {
        String response = callApi(interval, symbol, range);

        JSONObject jsonObject = new JSONObject(response);

        double regularMarketPrice = jsonObject
                .getJSONObject("chart")
                .getJSONArray("result")
                .getJSONObject(0)
                .getJSONObject("meta")
                .getDouble("regularMarketPrice");
        return regularMarketPrice;
    }

    private static String callApi(String interval, String symbol, String range)
    {
        Playwright playwright = Playwright.create();
        APIRequest request = playwright.request();
        APIRequestContext apiRequestContext = request.newContext();
        Map<String, String> headers = new HashMap<>();
        headers.put("X-RapidAPI-Key", xRapidApiKey);
        headers.put("X-RapidAPI-Host", xRapidApiHost);
        headers.put("Content-Type", "application/json");

        String endpoint = baseUrl + "stock/v3/get-chart";

        apiRequestContext = playwright.request()
                .newContext(new APIRequest.NewContextOptions().setBaseURL(endpoint)
                        .setExtraHTTPHeaders(headers));
        APIResponse response = apiRequestContext.get(endpoint, RequestOptions.create()
                .setQueryParam("interval",interval)
                .setQueryParam("symbol", symbol)
                .setQueryParam("range", range)
        );
        int responseCode=response.status();
        assertEquals(200,responseCode);


        String responseText = response.text();

        playwright.close();
        return responseText;
    }

}
