package org.acme

class LeaderboardController {

    def index() {

        def leaderboardData = []

        // Sort the users into the leaderboard Data
        // We get their full name and then use the two UserService methods to fetch their score and level position
        User.list().each { user ->
            leaderboardData << [
                    userId: user.id,
                    points: UserService.determineUserScore(user.id),
                    level: UserService.determineUserLevelPosition(user.id),
                    standing: user.classStanding
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

    def indexFilteredByStanding(String classStandingFilter) {

        def leaderboardData = []

            // Sort the users into the leaderboard Data
            // the users are first filtered based on their classStanding
            // We get their full name and then use the two UserService methods to fetch their score and level position
            User.where { classStanding == classStandingFilter }
                .list()
                .each { user ->
            leaderboardData << [
                    userId: user.id,
                    points: UserService.determineUserScore(user.id),
                    level: UserService.determineUserLevelPosition(user.id),
                    standing: user.classStanding
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
