Feature: Delete offer

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

  Scenario: Delete a offer with successfully
    When I delete a offer with id "5034c1ac-75ac-4e72-a443-7266da86b911" from store with id "5034c1ac-75ac-4e72-a443-7266da86b911" and category id "5034c1ac-75ac-4e72-a443-7266da86b911"
    Then the offer should be remove from datastore

  Scenario: Delete a offer with error when store not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try delete a offer from store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

  Scenario: Delete a offer with error when category not exists
    Given an id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try delete a offer from category with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error
