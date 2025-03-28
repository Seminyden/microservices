package com.gmail.seminyden.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps", "com.gmail.seminyden.component"},
        plugin = {"pretty", "html:target/resource-service-component-cucumber-report.html"}
)
public class ResourceServiceComponentTest {
}
