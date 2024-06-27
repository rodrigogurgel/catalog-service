Feature: Create store

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 65888cdc-7cb6-48f0-972d-65d8b96b4a85 | Mock Store | A description used for tests |

  Scenario: Create a store with successfully
    Given the following store information:
      | name       | description                  |
      | Mock Store | A description used for tests |
    When I create a store
    Then the store should be persisted in the datastore

  Scenario: Create an store with error when store already exists
    When I try to create a store with the id "65888cdc-7cb6-48f0-972d-65d8b96b4a85" that already exists
    Then I should get an error StoreAlreadyExistsException