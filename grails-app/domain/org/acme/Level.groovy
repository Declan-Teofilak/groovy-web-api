package org.acme

/**
 * Models the difficulty and a user's level of expertise
 */
class Level {

    Long id
    String title
    Integer position
    Integer activitiesRequired
    Integer pointsAwarded

    static constraints = {
    }

    static mapping = {
        id generator: 'assigned'
    }
}
