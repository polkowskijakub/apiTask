package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.example.helpers.apiCalls.getRegularMarketPrice;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class f0001 {

    static Playwright playwright;
    static Browser browser;
    static String pageUrl = "https://finance.yahoo.com/chart/NVDA";

    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate(pageUrl);

        assertThat(page).hasTitle(Pattern.compile("Yahoo is part of the Yahoo family of brands"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept all")).click();

        assertThat(page.getByText("NVIDIA Corporation (NVDA)")).isVisible();

    }

    @AfterEach
    void closeContext() {
        context.close();
    }


    @Test
    void marketPriceShouldBeEqual() {
        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("3M"))).isVisible();


        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("1Y")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Interval 1D")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("1 year")).click();

        ElementHandle element = page.querySelector("[data-test=qsp-price]");
        String value = element.getAttribute("value");
        Double db = Double.valueOf(value);

        Double apiValue = getRegularMarketPrice("1Y", "NVDA", "1Y");
        assertEquals(apiValue, db);

    }
}
