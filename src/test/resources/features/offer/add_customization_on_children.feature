Feature: Add Customization on children

  Background:
    Given the information of the Customization to be added
      | id                                   | name                   | description               | status    |
      | 693038f4-85d9-4e01-b8c8-ee503d7cadcc | Customization de teste | Customization para testes | AVAILABLE |
    And the information of the Offer to be updated
      | id                                   | product_id                           | price | status    |
      | 4bb8b866-2137-4f10-8604-c7acb850d686 | 4bb8b866-2137-4f10-8604-c7acb850d686 | 26.50 | AVAILABLE |
    And that there is a Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Store with the Id "2c371f9f-ea24-421b-803d-5e477caf8e34"
    And that there is a Category with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there is a Product with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there is an Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Offer with the Id "edcf7543-f4e7-44f7-8d72-9d51b75d4b0f"
    And that there isn't a Product with the Id "edcf7543-f4e7-44f7-8d72-9d51b75d4b0f"
    And the Offer to be updated has a Customization with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    And the Offer to be updated has a Option with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Customization with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"

  Scenario: Adding a Customization on children successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to add a Customization on children with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then the Customization should be added in the offer

  Scenario: Failure in adding a Customization on children when the parent Option doesn't exist
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to add a Customization on children with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc"
    Then I should encounter a "OptionNotFoundException" error

  Scenario: Failure in adding a Customization on children when the Store doesn't exist
    Given the Id of the Store is "2c371f9f-ea24-421b-803d-5e477caf8e34"
    When I attempt to add a Customization on children with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc"
    Then I should encounter a "StoreNotFoundException" error

  Scenario: Failure in adding a Customization on children when the Offer doesn't exist
    Given the information of the Offer to be updated
      | id                                   | product_id                           | price | status    |
      | edcf7543-f4e7-44f7-8d72-9d51b75d4b0f | 4bb8b866-2137-4f10-8604-c7acb850d686 | 26.50 | AVAILABLE |
    And that there isn't a Offer with the Id "edcf7543-f4e7-44f7-8d72-9d51b75d4b0f"
    And the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to add a Customization on children with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then I should encounter a "OfferNotFoundException" error

  Scenario: Failure in adding a Customization on children when the Product doesn't exist
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And the information of the Offer to be updated
      | id                                   | product_id                           | price | status    |
      | 4bb8b866-2137-4f10-8604-c7acb850d686 | edcf7543-f4e7-44f7-8d72-9d51b75d4b0f | 26.50 | AVAILABLE |
    And the Offer to be updated has a Customization with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    And the Offer to be updated has a Option with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Customization with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    When I attempt to add a Customization on children with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then I should encounter a "ProductsNotFoundException" error
