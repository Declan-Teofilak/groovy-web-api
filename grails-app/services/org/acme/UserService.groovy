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
        // Very cool that GROM supports this convention based on the Domain specification of the composite key!
        def completedActivity = CompletedActivity.findByUserAndActivity(user, activity)

        if (completedActivity)
        {
            throw new Exception("409: Acitvity ($activityId) has already been completed for supplied user ($userId)")
        }

        //Ensure we don't already have a completed activity and that the activity and user are valid
        if (activity && user)
        {
            completedActivity = new CompletedActivity()
            completedActivity.activity = activity
            completedActivity.user = user
            completedActivity.dateCreated = new Date()
            completedActivity.save()
        }
        else
        {
            if (!activity)
                throw new Exception("400: Unable to find Activity for supplied activityId ($activityId)")
            else if (!user)
                throw new Exception("400: Unable to find User for supplied userId ($userId)")
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
            throw new Exception("400: No user found for the supplied userId ($userId)")
        }

        def activities = CompletedActivity.where {
            user.id == userId
        }.list()

        return activities
    }

    /**
     * Responsible for determining a User's total completed activities score
     * PARAMS: userId - Long (Id of User we are determining score for)
     * RETURNS: score - Integer (Summation of points accumulated across activities completed)
     */
    static
    def determineUserScore(Long userId)
    {
        List<CompletedActivity> activities = fetchAllCompletedActivitiesForUser(userId)

        def score = 0

        for (CompletedActivity activity in activities)
        {
            // NOTE: We multiply the points awarded for this activity's level based on the amount of competencies it holds
            score += (activity.activity.level.pointsAwarded * activity.activity.competencies.size())
        }

        return score
    }

    /**
     * Responsible for determining the user's level
     * PARAMS: userId - Long (Id of User we are determining the level for)
     * RETURNS: currBestLevel - Integer (Highest "position based" level) based on README ruleset for leveling
     */
    static
    def determineUserLevelPosition(Long userId)
    {
        List<CompletedActivity> activities = fetchAllCompletedActivitiesForUser(userId)

        HashMap<Integer, Integer> levelMappings = new HashMap<Integer, Integer>()

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
