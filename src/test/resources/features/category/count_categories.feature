Feature: Count Categories

  Background:
    Given that there is a Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Store with the Id "2c371f9f-ea24-421b-803d-5e477caf8e34"

  Scenario: Counting Categories successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to count the Categories with the begins with as "Product"
    Then the Categories should be counted in the database with the begins with as "Product"

  Scenario: Failure in getting a Categories when the Store doesn't exist
    Given the Id of the Store is "2c371f9f-ea24-421b-803d-5e477caf8e34"
    When I attempt to count the Categories with the begins with as "Product"
    Then I should encounter a "StoreNotFoundException" error

  Scenario: Failure in getting a Categories when the begins with parameter has length less than 3 characters
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to count the Categories with the begins with as "Pr"
    Then I should encounter a "BeginsWithLengthException" error