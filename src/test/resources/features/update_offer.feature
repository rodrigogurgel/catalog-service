Feature: Update category

  Background:
    Given the following store exists:
      | id                                   | name       | description                  |
      | 65888cdc-7cb6-48f0-972d-65d8b96b4a85 | Mock Store | A description used for tests |

    And the following category exists:
      | id                                   | name          | description                  | status    |
      | 5034c1ac-75ac-4e72-a443-7266da86b911 | Mock Category | A description used for tests | AVAILABLE |
