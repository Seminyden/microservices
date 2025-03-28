Feature: Song service CRUD test

  Scenario: Successful song metadata creation
    Given a new song 1 with complete metadata
    When the song 1 is created in the system
    Then the system should return a valid song ID = 1

  Scenario: Successful song metadata retrieving
    Given a song 2 exists in the system
    When the song 2 metadata is retrieved
    Then the system should return the complete song 2 metadata

  Scenario: Successful song metadata deleting
    Given the song 3 is created in the system
    When the song 3 is deleted
    Then the system should confirm the deletion of 3 song metadata