package org.acme

import grails.gorm.transactions.Transactional

class UserService {

    /**
     * Responsible for fetching users in the application
     */
    @Transactional
    List<User> listUsers() {
        return User.list()
    }

    /**
     * Responsible for completing an activity
     */
    @Transactional
    def complete(Long userId, Long activityId) {
        def activity = Activity.findById(activityId)
        def user = User.findById(userId)

        if (activity && user)
        {
            def completedActivity = new CompletedActivity()
            completedActivity.activity = activity
            completedActivity.user = user
            completedActivity.dateCreated = new Date()
            completedActivity.save()
        }
        else
        {
            render status: 400, text: "Unable to complete Activity"
        }

    }

    /**
     * Responsible for fetching all completed activities
     */
    static
    def fetchAllCompletedActivitiesForUser(Long userId)
    {
        def user = User.findById(userId)

        if (!user) {
            render status: 400, text: "Bad User Id"
            return
        }

        def activities = CompletedActivity.where {
            user.id == userId
        }.list()

        return activities
    }

    /**
     * Responsible for determining a User's total completed activities score
     */
    static
    def determineUserScore(Long userId)
    {
        List<CompletedActivity> activities = fetchAllCompletedActivitiesForUser(userId)

        def score = 0

        for (CompletedActivity activity in activities)
        {
            score += activity.activity.level.pointsAwarded
        }

        return score
    }

    /**
     * Responsible for determining the user's level (returns the Integer position)
     */
    static
    def determineUserLevelPosition(Long userId)
    {
        List<CompletedActivity> activities = fetchAllCompletedActivitiesForUser(userId)

        HashMap<Integer, Integer> levelMappings = new HashMap<Level, Integer>()

        // For each activity, map the position to the number of occurrences
        // Using a hashmap here for quicker retrieval
        for (CompletedActivity activity in activities)
        {
            levelMappings[activity.activity.level.position] = (levelMappings[activity.activity.level.position] ?: 0) + 1
        }

        // Used to track the current highest level fetched from the map
        def currBestLevel = 0

        for (Level level in Level.list(sort: "position", order: "asc"))
        {
            if (level.position > currBestLevel && levelMappings[level.position] >= level.activitiesRequired)
                currBestLevel = level.position
        }

        return currBestLevel
    }
}
