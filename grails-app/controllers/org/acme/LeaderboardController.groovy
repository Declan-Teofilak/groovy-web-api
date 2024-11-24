package org.acme

class LeaderboardController {

    def index() {

        def leaderboardData = []

        // Sort the users into the leaderboard Data
        // We get their full name and then use the two UserService methods to fetch their score and level position
        User.list().each { user ->
            leaderboardData << [
                    username: user.firstName + " " + user.lastName,
                    points: UserService.determineUserScore(user.id),
                    level: UserService.determineUserLevelPosition(user.id)
            ]
        }

        // Respond with the data, sorted by level desc and points asc
        // We also only take the top 20 rows
        respond([
                data: leaderboardData
                        ?.sort { a, b ->
                            b.level <=> a.level ?: b.points <=> a.points
                        }
                        ?.take(20)
        ])
    }
}
