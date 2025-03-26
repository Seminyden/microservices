package com.gmail.seminyden.e2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/e2e",
        glue = {"steps", "com.gmail.seminyden.component"},
        plugin = {"pretty", "html:target/song-service-component-cucumber-report.html"}
)
public class SongServiceComponentTest {
}