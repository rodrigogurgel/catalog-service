Feature: Count Offers

  Background:
    Given that there is a Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Store with the Id "2c371f9f-ea24-421b-803d-5e477caf8e34"
    And that there is a Category with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Category with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc"

  Scenario: Counting Offers successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to count Offers with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686" and begins with as "     "
    Then the Offers should be counted in the database with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686" and begins with as "     "

  Scenario: Counting Offers successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to count Offers with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686" and begins with as null
    Then the Offers should be counted in the database with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686" and begins with as null

  Scenario: Failure in counting an Offer when the Store doesn't exist
    Given the Id of the Store is "2c371f9f-ea24-421b-803d-5e477caf8e34"
    When I attempt to count Offers with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686" and begins with as "Offer"
    Then I should encounter a "StoreNotFoundException" error

  Scenario: Failure in counting an Offer when the Category doesn't exist
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to count Offers with the Category Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc" and begins with as "Offer"
    Then I should encounter a "CategoryNotFoundException" error

  Scenario: Failure in counting an Offer when the begins with parameter has length less than 3 characters
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to count Offers with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686" and begins with as "Of"
    Then I should encounter a "BeginsWithLengthException" error