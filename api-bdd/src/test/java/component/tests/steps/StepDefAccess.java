package component.tests.steps;

import component.tests.helpers.SecurityHelper;
import io.cucumber.java.en.Given;


public class StepDefAccess {
    private final ScenarioContext scenarioContext;

    public StepDefAccess(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        StepDefCommon stepDefCommon = new StepDefCommon(scenarioContext);
    }

    @Given("I am an {string} user")
    public void thatIAmAuthorizedUserAndCallEndpoint(String role) {
        SecurityHelper.setupRole(scenarioContext, role);
        setupHeaders();
    }

    private void setupHeaders() {
        scenarioContext.setBaseURL("https://apidojo-yahoo-finance-v1.p.rapidapi.com/");
        scenarioContext.getHeaders().put("Content-Type", "application/json");
    }


}
