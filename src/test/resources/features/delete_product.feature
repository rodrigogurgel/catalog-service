Feature: Delete product

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 65888cdc-7cb6-48f0-972d-65d8b96b4a85 | Mock Store | A description used for tests |

    And the following product exists:
      | id                                   | name         | description                  | image                   |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Product | A description used for tests | http://www.image.com.br |

  Scenario: Delete a product with success
    When I delete a product from store
    Then the product should be remove from datastore

  Scenario: Delete a product with error when store not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try remove a product from store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

