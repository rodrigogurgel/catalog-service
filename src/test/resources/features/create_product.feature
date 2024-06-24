Feature: Create product

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 65888cdc-7cb6-48f0-972d-65d8b96b4a85 | Mock Store | A description used for tests |

    And the following product information's:
      | name         | description                  | image                   |
      | Mock Product | A description used for tests | http://www.image.com.br |

  Scenario: Create a product with success
    When I add a product into store
    Then the product should be persist in the datastore

  Scenario: Create a product with error when store not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try add a product into store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error