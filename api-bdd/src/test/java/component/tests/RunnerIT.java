package component.tests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Help for running all tests
 */

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:features"},
        glue = {"component/tests/steps"},
        tags = "@All and not @Ignore",
        plugin = {"json:target/report/cucumber.json"})
public class RunnerIT {
    private RunnerIT() {
    }

}