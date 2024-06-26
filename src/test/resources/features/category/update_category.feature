Feature: Update Category

  Background:
    Given the information of the Category
      | id                                   | name                 | description             | status      |
      | 4bb8b866-2137-4f10-8604-c7acb850d686 | Categoria de teste 2 | Categoria para testes 2 | UNAVAILABLE |
    And that there is a Category with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Category with the Id "edcf7543-f4e7-44f7-8d72-9d51b75d4b0f"
    And that there is a Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Store with the Id "2c371f9f-ea24-421b-803d-5e477caf8e34"

  Scenario: Updating a Category successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to update a Category
    Then the Category should be updated in the database

  Scenario: Failure in updating a Category when the Store doesn't exist
    Given the Id of the Store is "2c371f9f-ea24-421b-803d-5e477caf8e34"
    When I attempt to update a Category
    Then I should encounter a "StoreNotFoundException" error

  Scenario: Failure in updating a Category when the Category doesn't exist
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to update a Category using the Id "edcf7543-f4e7-44f7-8d72-9d51b75d4b0f"
    Then I should encounter a "CategoryNotFoundException" error