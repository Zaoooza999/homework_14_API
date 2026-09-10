package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class StatusTests {
    @Test
    public void totalAmountTest() {
        get("https://selenoid.qa.guru/ui/status")
                .then()
                .body("state.total", is(25));
    }
    @Test
    public void totalAmountTest_withLogs() {
        get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .body("state.total", is(25));
    }

    @Test
    public void totalAmountTest_withAllLogs() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .body("state.total", is(25));
    }
    @Test
    public void status200Test() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    public void containingItemsTest() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("state",allOf(hasKey("total"),hasKey("used"),hasKey("queued"),hasKey("pending"),
                        hasKey("warmReady"),hasKey("warmTotal"),hasKey("hotReady"),hasKey("hotTotal"),
                        hasKey("warmSlots")))
                .body("state.warmSlots[0]",hasKey("browser"))
                .body("state.warmSlots.browser",hasItem("chrome"))
                .body("state.warmSlots.browser",hasItems("chrome","chromium"))
                .body("browsers",hasKey("android"))
                .body("browsers",allOf(hasKey("chrome"),hasKey("firefox")))
                .body("browsers.android",is(0))
                .body("browsers",hasEntry("firefox",0))
                .body("browsers",allOf(hasEntry("chrome",0),hasEntry("firefox",0)));
    }

    @Test
    public void statusSchemaTest() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas\\status_response_schema.json"));
    }

    @Test
    public void bestTotalAmountTest() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas\\status_response_schema.json"))
                .body("state.total", is(25))
                .body("state",allOf(hasKey("total"),hasKey("used"),hasKey("queued"),hasKey("pending"),
                        hasKey("warmReady"),hasKey("warmTotal"),hasKey("hotReady"),hasKey("hotTotal"),
                        hasKey("warmSlots")))
                .body("state.warmSlots[0]",hasKey("browser"))
                .body("state.warmSlots.browser",hasItem("chrome"))
                .body("state.warmSlots.browser",hasItems("chrome","chromium"))
                .body("browsers",hasKey("android"))
                .body("browsers",allOf(hasKey("chrome"),hasKey("firefox")))
                .body("browsers.android",is(0))
                .body("browsers",hasEntry("firefox",0))
                .body("browsers",allOf(hasEntry("chrome",0),hasEntry("firefox",0)));
    }
}

