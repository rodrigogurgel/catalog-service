Feature: Create Product

  Background:
    Given the information of the Product
      | id                                   | name             | description         | status    | image                    |
      | 693038f4-85d9-4e01-b8c8-ee503d7cadcc | Produto de teste | Produto para testes | AVAILABLE | https://www.image.com.br |
    And that there is a Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Store with the Id "2c371f9f-ea24-421b-803d-5e477caf8e34"
    And that there is a Product with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Product with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc"

  Scenario: Creating a Product successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to create a Product
    Then the Product should be stored in the database

  Scenario: Failure in creating a Product when the Store doesn't exist
    Given the Id of the Store is "2c371f9f-ea24-421b-803d-5e477caf8e34"
    When I attempt to create a Product
    Then I should encounter a "StoreNotFoundException" error

  Scenario: Failure in creating a Product when a Product with the same Id already exists
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to create a Product using the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then I should encounter a "ProductAlreadyExistsException" error