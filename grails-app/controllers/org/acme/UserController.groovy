package org.acme

import java.time.LocalDate

class UserController {

    UserService userService

    def index() {
        render(contentType: "application/json", view: "/user/index", model: [users: userService.listUsers() ])
    }

    def complete(Long userId, Long activityId) {
        try
        {
            userService.complete(userId, activityId)
            respond(["Activity completed: UserId - $userId | ActivityId - $activityId."])
        }
        catch (Exception e)
        {
            render status: 400, text: e.message
        }
    }
}
