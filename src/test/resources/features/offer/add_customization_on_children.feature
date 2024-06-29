Feature: Add Customization on children

  Background:
    Given that there is the following Products in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
      | id                                   | name              | description          | status    | image                     |
      | 4bb8b866-2137-4f10-8604-c7acb850d686 | Produto de teste  | Produto para testes  | AVAILABLE | https://www.image.com.br  |
      | 55847dfc-93cb-4fa8-b164-5ae66bdb60d1 | Produto1 de teste | Produto1 para testes | AVAILABLE | https://www.image1.com.br |
      | 55847dfc-93cb-4fa8-b164-5ae66bdb60d2 | Produto2 de teste | Produto2 para testes | AVAILABLE | https://www.image2.com.br |
    And the the following Customizations
      | id                                   | name                   | description               | status    |
      | 693038f4-85d9-4e01-b8c8-ee503d7cadcc | Customization de teste | Customization para testes | AVAILABLE |
      | 693038f4-85d9-4e01-b8c8-ee503d7cadc2 | Customization de teste | Customization para testes | AVAILABLE |
    And the Customization with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc" has the following Options
      | id                                   | product_id                           | price | status    |
      | 4bb8b866-2137-4f10-8604-c7acb850d686 | 55847dfc-93cb-4fa8-b164-5ae66bdb60d1 | 26.50 | AVAILABLE |
    And the Customization with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadc2" has the following Options
      | id                                   | product_id                           | price | status    |
      | 4bb8b866-2137-4f10-8604-c7acb850d687 | 55847dfc-93cb-4fa8-b164-5ae66bdb60d2 | 26.50 | AVAILABLE |
    And the information of the Offer
      | id                                   | product_id                           | price | status    |
      | 4bb8b866-2137-4f10-8604-c7acb850d686 | 4bb8b866-2137-4f10-8604-c7acb850d686 | 26.50 | AVAILABLE |
    And that the Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" has the Customization with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc"
    And that there is a Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Store with the Id "2c371f9f-ea24-421b-803d-5e477caf8e34"
    And that there is an Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't an Offer with the Id "edcf7543-f4e7-44f7-8d72-9d51b75d4b0f"
    And that there isn't a Product with the Id "edcf7543-f4e7-44f7-8d72-9d51b75d4b0f"

  Scenario: Adding a Customization on children successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to add a Customization with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadc2" on children with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then the Customization with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadc2" should be added in the offer

  Scenario: Failure in adding a Customization on children when the parent Option doesn't exist
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to add a Customization with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadc2" on children with the Id "4bb8b866-2137-4f10-8604-c7acb850d689" in the Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then I should encounter a "OptionNotFoundException" error

  Scenario: Failure in adding a Customization on children when the Store doesn't exist
    Given the Id of the Store is "2c371f9f-ea24-421b-803d-5e477caf8e34"
    When I attempt to add a Customization with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadc2" on children with the Id "4bb8b866-2137-4f10-8604-c7acb850d689" in the Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then I should encounter a "StoreNotFoundException" error

  Scenario: Failure in adding a Customization on children when the Offer doesn't exist
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to add a Customization with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadc2" on children with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Offer with the Id "edcf7543-f4e7-44f7-8d72-9d51b75d4b0f"
    Then I should encounter a "OfferNotFoundException" error

  Scenario: Failure in adding a Customization on children when the Product doesn't exist
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And the the following Customizations
      | id                                   | name                   | description               | status    |
      | 693038f4-85d9-4e01-b8c8-ee503d7cadc3 | Customization de teste | Customization para testes | AVAILABLE |
    And the Customization with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadc3" has the following Options
      | id                                   | product_id                           | price | status    |
      | 4bb8b866-2137-4f10-8604-c7acb850d683 | edcf7543-f4e7-44f7-8d72-9d51b75d4b0f | 26.50 | AVAILABLE |
    When I attempt to add a Customization with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadc3" on children with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then I should encounter a "ProductsNotFoundException" error
