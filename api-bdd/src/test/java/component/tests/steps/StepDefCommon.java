package component.tests.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StepDefCommon {

    private final ScenarioContext scenarioContext;

    public StepDefCommon(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }


    @When("I perform a GET request for {string}")
    public void iPerformAGETRequest(String path) {
        performGet(path);
    }

    @When("I perform a GET request for {string} with parameters:")
    public void iPerformAGETRequestWithParameters(String path, DataTable parameters) {
        Map<String, String> map = parameters.asMap(String.class, String.class);
        performGet(path, map);
    }


    @Then("I receive the request response code {int}")
    public void iReceiveTheRequestResponseCode(int expectedStatusCode) {
        assertThat(scenarioContext.getLatestResponseCode(), is(expectedStatusCode));
    }

    void performGet(String path) {
        performGet(path, new HashMap<>());
    }


    private void performGet(String path, Map<String, String> parameters) {
        Response response = given()
            .log().all()
            .params(parameters)
            .headers(scenarioContext.getHeaders())
            .when()
            .log().all()
            .get(scenarioContext.getBaseURL() + path);

        String responseBody = response.getBody().asString();
        if (responseBody.isEmpty()) {
            scenarioContext.logNullResponse("GET", path);
        } else {
            scenarioContext.setLatestResponse(response);
            scenarioContext.logResponse(response);
        }
    }

    @Then("I save {string} file with content {string}")
    public void iCreateAFileWithContent(String fileName, String content) {
        try {
            FileWriter myWriter = new FileWriter(fileName+".txt");
            myWriter.write(content);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
