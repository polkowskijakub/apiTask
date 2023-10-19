package component.tests.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;


public class StepDefApiTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(StepDefApiTests.class);
    private final ScenarioContext scenarioContext;

    public StepDefApiTests(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }


    @And("field {string} value is {string}")
    public void conditionIs(String property, String value) {
        Assert.assertNotNull(property + " not in latestResponse",
                scenarioContext.getLatestResponse().getBody().jsonPath().get(property));
        Assert.assertEquals(property + " has different value from " + value, value,
                scenarioContext.getLatestResponse().getBody().jsonPath().get(property).toString());

    }


    @Then("the response structure has the same content as the {string} schema")
    public void theResponseStructureHasTheSameContentsAsTheSelectedSchema(String schemaFile) {

        try {
            JsonNode node = new ObjectMapper().readTree(scenarioContext.getLatestResponse().getBody().asString());
            validateResponseSchema(node, schemaFile);
        } catch (IOException e) {
            LOGGER.debug("Exception found while reading response for api ", e);
            Assert.fail("Exception found while reading response from context:" + e);
        }

    }

    private void validateResponseSchema(JsonNode node, String schemaFile) {
        LOGGER.info("Validating response against schema : {}", schemaFile);
        MatcherAssert.assertThat(node.toString(),
                JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaFile));

    }

    @And("the response contains the following values:")
    public void theResponseContainsTheFollowingValues(Map<String, String> values) {
        values.forEach((key, value) ->
        {
            Assert.assertNotNull(key + " doesn't exist",
                    scenarioContext.getLatestResponse().getBody().jsonPath().get(key));
            Assert.assertEquals(key + " doesn't have value " + value,
                    scenarioContext.getLatestResponse().getBody().jsonPath().get(key).toString(), value);
        });

    }

    @And("value in field {string} is null")
    public void valueInFieldIsNull(String field) {
        Assert.assertNull("Field " + field + " is in latestResponse is not null.", scenarioContext.getLatestResponse().jsonPath().get(field));

    }

}
