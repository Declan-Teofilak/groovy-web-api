package org.acme

import java.time.LocalDate

class UserController {

    UserService userService

    def index() {
        render(contentType: "application/json", view: "/user/index", model: [users: userService.listUsers() ])
    }

    def complete(Long userId, Long activityId) {
        userService.complete(userId, activityId)
        respond(["Activity completed: USER_ID {userId}, ACTIVITY_ID {activityId}."])
    }
}
