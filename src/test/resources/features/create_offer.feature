Feature: Create offer

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 65888cdc-7cb6-48f0-972d-65d8b96b4a85 | Mock Store | A description used for tests |

    And the following category exists:
      | id                                   | name          | description                  | status    |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Category | A description used for tests | AVAILABLE |

    And the following product exists:
      | id                                   | name         | description                  | image                   |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Product | A description used for tests | http://www.image.com.br |

    And the following offer information:
      | price  | status    |
      | 100.00 | AVAILABLE |

  Scenario: Create a offer with success
    When I create a offer into store
    Then the offer should be persist in the datastore

  Scenario: Create a offer shouldn't update any product
    When I change product name to "New Product Name"
    And I create a offer into store with this product
    Then the offer with new product name should be persist in the datastore
    But the product shouldn't be updated in the datastore

  Scenario: Create a offer with error when store not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try create a offer into store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

  Scenario: Create a offer with error when category not exists
    Given an id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try create a offer into category with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

  Scenario: Create a offer with error when product not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no product associated
    When I try create a offer with product that has this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error