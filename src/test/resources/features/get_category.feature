Feature: Get category

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 65888cdc-7cb6-48f0-972d-65d8b96b4a85 | Mock Store | A description used for tests |

    And the following category exists:
      | id                                   | name          | description                  | status    |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Category | A description used for tests | AVAILABLE |

  Scenario: Get a category with successfully
    When I get a category with id "5034c1ac-75ac-4e72-a443-7266da86b911" from store with id "65888cdc-7cb6-48f0-972d-65d8b96b4a85"
    Then the category should have same information

  Scenario: Get a category with error when store not exists
    Given an id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try get a category from store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

  Scenario: Get a category with error when category not exists
    Given an id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no category associated
    When I try get a category with this id "92ddeebf-da50-402f-b850-19e5fb093a0a" from store
    Then I get an error
