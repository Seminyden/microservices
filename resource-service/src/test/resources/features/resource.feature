Feature: Resource Management

  Background:
    Given the database is clean

  Scenario: Upload and retrieve new resource
    Given I have a valid file to upload
    When I upload the file
    Then I should receive a success response
    And I request the file by its ID
    Then I should receive the file

