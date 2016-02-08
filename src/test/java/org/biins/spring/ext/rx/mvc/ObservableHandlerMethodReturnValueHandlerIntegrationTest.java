package org.biins.spring.ext.rx.mvc;

import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.*;

/**
 * @author Martin Janys
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ObservableSpringConfig.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ObservableHandlerMethodReturnValueHandlerIntegrationTest {

    @Value("${local.server.port}")
    int port;

    @Before
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void asyncObservableTest() throws Exception {
        when().get("/api/async")
                .then()
                .contentType("application/text")
                .statusCode(HttpStatus.SC_OK)
                .content(Matchers.is("hello"));
    }

}