Feature: Get Offers

  Background:
    Given that there is a Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Store with the Id "2c371f9f-ea24-421b-803d-5e477caf8e34"
    And that there is a Category with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Category with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc"

  Scenario: Getting Offers successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get an Offer with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686", limit as "1", offset as "0" and begins with as "Offer"
    Then the Offers should be retrieved from database with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686", limit as "1", offset as "0" and begins with as "Offer"

  Scenario: Getting Offers successfully when the limit parameter is negative
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get an Offer with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686", limit as "-1", offset as "1" and begins with as "Offer"
    Then the Offers should be retrieved from database with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686", limit as "0", offset as "1" and begins with as "Offer"

  Scenario: Getting Offers successfully when the limit parameter is greater then 20
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get an Offer with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686", limit as "21", offset as "1" and begins with as "Offer"
    Then the Offers should be retrieved from database with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686", limit as "20", offset as "1" and begins with as "Offer"

  Scenario: Getting Offers successfully when the offset parameter is negative
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get an Offer with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686", limit as "21", offset as "-1" and begins with as "Offer"
    Then the Offers should be retrieved from database with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686", limit as "20", offset as "0" and begins with as "Offer"

  Scenario: Failure in getting an Offer when the Store doesn't exist
    Given the Id of the Store is "2c371f9f-ea24-421b-803d-5e477caf8e34"
    When I attempt to get an Offer with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686", limit as "1", offset as "1" and begins with as "Offer"
    Then I should encounter a "StoreNotFoundException" error

  Scenario: Failure in getting an Offer when the Category doesn't exist
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get an Offer with the Category Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc", limit as "1", offset as "1" and begins with as "Offer"
    Then I should encounter a "CategoryNotFoundException" error

  Scenario: Failure in getting an Offer when the begins with parameter has length less than 3 characters
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get an Offer with the Category Id "4bb8b866-2137-4f10-8604-c7acb850d686", limit as "1", offset as "1" and begins with as "Of"
    Then I should encounter a "BeginsWithLengthException" error