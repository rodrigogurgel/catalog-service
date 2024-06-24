Feature: Delete category

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 65888cdc-7cb6-48f0-972d-65d8b96b4a85 | Mock Store | A description used for tests |

    And the following category exists:
      | id                                   | name          | description                  | status    |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Category | A description used for tests | AVAILABLE |

  Scenario: Delete a category with success
    When I delete a category from store
    Then the category should be remove from datastore

  Scenario: Delete a category with error when store not exists
    Given a id "92ddeebf-da50-402f-b850-19e5fb093a0a" with no store associated
    When I try remove a category from store with this id "92ddeebf-da50-402f-b850-19e5fb093a0a"
    Then I get an error

