Feature: Delete Offer

  Background:
    Given that there is a Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't a Store with the Id "2c371f9f-ea24-421b-803d-5e477caf8e34"
    And that there is an Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" in the Store with the Id "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    And that there isn't an Offer with the Id "693038f4-85d9-4e01-b8c8-ee503d7cadcc"

  Scenario: Deleting an Offer successfully
    Given the Id of the Store is "259f3a2d-12d2-4b4d-9e10-0e59efb378a9"
    When I attempt to delete an Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then the Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686" should be deleted from database

  Scenario: Failure in deleting an Offer when the Store doesn't exist
    Given the Id of the Store is "2c371f9f-ea24-421b-803d-5e477caf8e34"
    When I attempt to delete an Offer with the Id "4bb8b866-2137-4f10-8604-c7acb850d686"
    Then I should encounter a "StoreNotFoundException" error