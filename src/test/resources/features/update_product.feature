Feature: Update product

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 65888cdc-7cb6-48f0-972d-65d8b96b4a85 | Mock Store | A description used for tests |

    And the following product exists:
      | id                                   | name         | description                  | image                   |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Product | A description used for tests | http://www.image.com.br |

  Scenario: Update a product with success
    When I update product name to "New Product Name" into store
    Then the product with new information's should be persist in the datastore

  Scenario: Update a product with error when store not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try add a product into store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

  Scenario: Update a product with error when product not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no product associated
    When I try update a product with this id "92ddeebf-da50-402f-b850-19e5fb093a0a" from store
    Then I get an error