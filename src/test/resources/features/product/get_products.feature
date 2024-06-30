Feature: Get Products

  Background:
    Given that there is a Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Store with the Id "2c371f9f-ea24-421b-803d-5e477caf8e34"

  Scenario: Getting Products successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get a Products with the limit as "1", offset as "1" and begins with as "Product"
    Then the Products should be retrieved from database with the limit as "1", offset as "1" and begins with as "Product"

  Scenario: Getting Products successfully when the limit parameter is negative
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get a Products with the limit as "-1", offset as "1" and begins with as "Product"
    Then the Products should be retrieved from database with the limit as "0", offset as "1" and begins with as "Product"

  Scenario: Getting Products successfully when the limit parameter is greater then 20
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get a Products with the limit as "21", offset as "1" and begins with as "Product"
    Then the Products should be retrieved from database with the limit as "20", offset as "1" and begins with as "Product"

  Scenario: Getting Products successfully when the offset parameter is negative
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get a Products with the limit as "21", offset as "-1" and begins with as "Product"
    Then the Products should be retrieved from database with the limit as "20", offset as "0" and begins with as "Product"

  Scenario: Failure in getting a Products when the Store doesn't exist
    Given the Id of the Store is "2c371f9f-ea24-421b-803d-5e477caf8e34"
    When I attempt to get a Products with the limit as "1", offset as "1" and begins with as "Product"
    Then I should encounter a "StoreNotFoundException" error

  Scenario: Failure in getting a Products when the begins with parameter has length less than 3 characters
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to get a Products with the limit as "1", offset as "1" and begins with as "Pr"
    Then I should encounter a "BeginsWithLengthException" error