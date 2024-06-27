Feature: Update offer

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Store | A description used for tests |

    And the following category exists:
      | id                                   | name          | description                  | status    |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Category | A description used for tests | AVAILABLE |

    And the following product exists:
      | id                                   | name         | description                  | image                   |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Product | A description used for tests | http://www.image.com.br |

    And the following offer exists:
      | id                                   | price  | status    |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | 100.00 | AVAILABLE |

  Scenario: Update a offer with successfully
    When I update offer price to "50.00" into store
    Then the offer with new information should be persist in the datastore

  Scenario: Update a offer with error when store not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try update a offer into store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

  Scenario: Update a offer with error when offer not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no offer associated
    When I try update a offer with this id "92ddeebf-da50-402f-b850-19e5fb093a0a" from store
    Then I get an error

  Scenario: Update a offer with error when category not exists
    Given an id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try update a offer into category with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

  Scenario: Update a offer with error when product not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no product associated
    When I try update a offer product to a product with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error