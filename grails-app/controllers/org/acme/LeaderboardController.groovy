package org.acme

class LeaderboardController {

    def index() {

        def leaderboardData = []

        // Sort the users into the leaderboard Data
        // We get their full name and then use the two UserService methods to fetch their score and level position
        User.list().each { user ->
            leaderboardData << [
                    rank: 0,
                    userId: user.id,
                    points: UserService.determineUserScore(user.id),
                    level: UserService.determineUserLevelPosition(user.id),
                    standing: user.classStanding
            ]
        }

        def sortedData = sortLeaderboardAndAssignRank(leaderboardData)

        // Respond with the data, sorted by level desc and points asc
        respond([
                data: sortedData
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
                    rank: 0,
                    userId: user.id,
                    points: UserService.determineUserScore(user.id),
                    level: UserService.determineUserLevelPosition(user.id),
                    standing: user.classStanding
            ]
        }

        def sortedData = sortLeaderboardAndAssignRank(leaderboardData)

        // Respond with the data, sorted by level desc and points asc
        respond([
                data: sortedData
        ])
    }

    def sortLeaderboardAndAssignRank(List<Object> leaderboardData)
    {
        leaderboardData
                ?.sort { a, b ->
                    b.level <=> a.level ?: b.points <=> a.points
                }
                ?.take(20)

        // We user the local rank param here to iterate over the top twenty and assign said rank to the data point
        def rank = 1
        sortedData.take(20).each { data ->
            data["rank"] = rank
            rank++
        }

        return sortedData
    }
}
