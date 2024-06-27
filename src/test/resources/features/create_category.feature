Feature: Create category

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 65888cdc-7cb6-48f0-972d-65d8b96b4a85 | Mock Store | A description used for tests |

    And the following category information:
      | name          | description                  | status    |
      | Mock Category | A description used for tests | AVAILABLE |

  Scenario: Create a category with successfully
    When I add a category into store
    Then the category should be persist in the datastore

  Scenario: Create a category with error when store not exists
    Given an id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try add a category into store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error