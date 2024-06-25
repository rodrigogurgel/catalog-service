Feature: Get offer

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

  Scenario: Get a offer with success
    When I get a offer with id "5034c1ac-75ac-4e72-a443-7266da86b911" from store with id "5034c1ac-75ac-4e72-a443-7266da86b911" and category id "5034c1ac-75ac-4e72-a443-7266da86b911"
    Then the offer should have same information

  Scenario: Get a offer with error when store not exists
    Given an id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try get a offer from store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

  Scenario: Get a offer with error when offer not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no offer associated
    When I try get a offer with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

  Scenario: Get a offer with error when category not exists
    Given an id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no category associated
    When I try get a offer from category with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error