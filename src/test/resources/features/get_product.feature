Feature: Get product

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 65888cdc-7cb6-48f0-972d-65d8b96b4a85 | Mock Store | A description used for tests |

    And the following product exists:
      | id                                   | name         | description                  | image                   |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Product | A description used for tests | http://www.image.com.br |

  Scenario: Get a product with successfully
    When I get a product with id "5034c1ac-75ac-4e72-a443-7266da86b911" from store with id "65888cdc-7cb6-48f0-972d-65d8b96b4a85"
    Then the product should have same information

  Scenario: Get a product with error when store not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try get a product from store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

  Scenario: Get a product with error when product not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no product associated
    When I try get a product with this id "92ddeebf-da50-402f-b850-19e5fb093a0a" from store
    Then I get an error
