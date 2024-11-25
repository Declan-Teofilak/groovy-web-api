package org.acme

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class UserSpec extends Specification implements DomainUnitTest<User> {

    def setup() {
        //NOTE: Mocking the domain classes here is required to interact with the GORM
        // since we are going to mock saving the completed activity, for instance
        mockDomain(User)
        mockDomain(Level)
        mockDomain(Activity)
        mockDomain(CompletedActivity)
    }

    def cleanup() {
    }

    void "Ensure users can complete activities"() {
        given: "Mock data is created"

        def level1 = new Level(title: "Level One", pointsAwarded: 10, position: 1)
        level1.save() >> level1

        def user1 = new User(firstName: "Dec", lastName: "T", classStanding: "Alumni", id: 1)
        user1.save() >> user1

        def activity1 = new Activity(title: "Mid-term", level: level1, id: 1)
        activity1.save() >> activity1

        when: "The UserService complete action is invoked for a given user and activity"
        // def userService = UserService
        // userService.complete(user1.id, activity1.id)

        // We are effectively mocking the above commented out call in the code below
        def userService = Mock(UserService)
        userService.complete(user1.id, activity1.id) >> { userId, activityId ->
            return new CompletedActivity(user: user1, activity: activity1)
        }

        def completedActivity = userService.complete(user1.id, activity1.id)

        then: "There is a CompletedActivity record for the user/activity"
        completedActivity != null
        completedActivity.userId == user1.id
        completedActivity.activityId == activity1.id
    }
}
