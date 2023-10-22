package component.tests.helpers;

import component.tests.steps.ScenarioContext;
import lombok.experimental.UtilityClass;


@UtilityClass
public class SecurityHelper {

    private static final String X_RAPIDAPI_KEY = "X-RapidAPI-Key";
    private static final String xRapidApiKey = "ENTER_YOUR_CREDENTIALS";
    private static final String X_RAPIDAPI_HOST = "X-RapidAPI-Host";
    private static final String xRapidApiHost = "ENTER_YOUR_CREDENTIALS";

    public static void setupRole(ScenarioContext scenarioContext, String role) {
        scenarioContext.getHeaders().remove("X-RapidAPI-Key");
        scenarioContext.getHeaders().remove("X-RapidAPI-Host");

        switch (role) {
            case "authorized": {
                scenarioContext.getHeaders().put(X_RAPIDAPI_KEY, xRapidApiKey);
                scenarioContext.getHeaders().put(X_RAPIDAPI_HOST, xRapidApiHost);
                break;
            }
            case "unauthorized": {
                scenarioContext.getHeaders().put(X_RAPIDAPI_KEY, "");
                scenarioContext.getHeaders().put(X_RAPIDAPI_HOST, "");
                break;
            }

        }


    }
}


