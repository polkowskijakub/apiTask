package component.tests.steps;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ScenarioContext {

    private static final String TAB = "\t";
    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioContext.class);


    private Map<String, String> headers = new HashMap<>();
    private Response latestResponse;
    private int latestResponseCode;
    private String baseURL;


    public void logResponse(Response response) {
        String responseBody = response.getBody().jsonPath().prettyPrint();

        LOGGER.info("------------------------{}", TAB);
        LOGGER.info("-------RESPONSE BODY: {}", responseBody);
        LOGGER.info("------------------------{}", TAB);

    }

    public void logNullResponse(String method, String path) {

        LOGGER.info("------------------------{}", TAB);
        LOGGER.info("-------RESPONSE BODY for method:{}, calling {} path was empty", method,path);
        LOGGER.info("------------------------{}", TAB);

    }


    public void setLatestResponse(Response latestResponse) {
        this.latestResponse = latestResponse;
        this.latestResponseCode = latestResponse.getStatusCode();
    }

}
