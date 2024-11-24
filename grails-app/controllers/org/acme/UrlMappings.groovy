package org.acme

class UrlMappings {

    static mappings = {
        "/api/levels"(controller: "level", action: "index")
        "/api/activities"(controller: "activity", action: "index")
        "/api/users"(controller: "user", action: "index")
        "/api/users/$userId/complete/$activityId"(controller: "user", action: "complete")
        "/api/leaderboard"(controller: "leaderboard", action: "index")
        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
