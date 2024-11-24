package org.acme

class ActivityController {

    ActivityService activityService

    def index() {
        render(contentType: "application/json", view: "/activity/index", model: [activities: activityService.listActivities() ])
    }

    // Fetch the activity specified by the id param
    def getActivity(Long id) {
        def activity = activityService.fetchActivityById(id)

        if (activity) {
            render(contentType: "application/json", model: [activity: activity])
        } else {
            render(status: 404, text: "Activity not found")
        }
    }
}
