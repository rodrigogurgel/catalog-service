Feature: Create Offer

  Background:
    Given the information of the Offer
      | id                                   | product_id                           | price | status    |
      | 693038f4-85d9-4e01-b8c8-ee503d7cadcc | 4bb8b866-2137-4f10-8604-c7acb850d686 | 26.50 | AVAILABLE |
    And that there is a Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Store with the Id "2c371f9f-ea24-421b-803d-5e477caf8e34"
    And that there is a Category with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Category with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc"
    And that there is a Product with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Product with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc"
    And that there is an Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't an Offer with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc"

  Scenario: Creating an Offer successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And the Id of the Category is "4bb8b866-2137-4f10-8604-c7acb850d686"
    When I attempt to create an Offer
    Then the Offer should be stored in the database

  Scenario: Failure in creating an Offer when the Store doesn't exist
    Given the Id of the Store is "2c371f9f-ea24-421b-803d-5e477caf8e34"
    And the Id of the Category is "4bb8b866-2137-4f10-8604-c7acb850d686"
    When I attempt to create an Offer
    Then I should encounter a "StoreNotFoundException" error

  Scenario: Failure in creating an Offer when an Offer with the same Id already exists
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And the Id of the Category is "4bb8b866-2137-4f10-8604-c7acb850d686"
    When I attempt to create an Offer using the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then I should encounter a "OfferAlreadyExistsException" error

  Scenario: Failure in creating an Offer when the Category doesn't exist
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And the Id of the Category is "693038f4-85d9-4e01-b8c8-ee503d7cadcc"
    When I attempt to create an Offer
    Then I should encounter a "CategoryNotFoundException" error

  Scenario: Failure in creating an Offer when the Product doesn't exist
    Given the information of the Offer
      | id                                   | product_id                           | price | status    |
      | 693038f4-85d9-4e01-b8c8-ee503d7cadcc | 693038f4-85d9-4e01-b8c8-ee503d7cadcc | 26.50 | AVAILABLE |
    And the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And the Id of the Category is "4bb8b866-2137-4f10-8604-c7acb850d686"
    When I attempt to create an Offer
    Then I should encounter a "ProductsNotFoundException" error