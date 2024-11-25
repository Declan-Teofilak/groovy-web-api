package org.acme

class ActivityController {

    ActivityService activityService

    def index() {
        render(contentType: "application/json", view: "/activity/index", model: [activities: activityService.listActivities() ])
    }
}
