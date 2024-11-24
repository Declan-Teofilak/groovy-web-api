package org.acme

/**
 * Models an application user
 */
class User implements Serializable {

    Long id
    String avatarUrl
    String firstName
    String lastName
    String classStanding

    static constraints = {
        avatarUrl url: true
    }

    static mapping = {
        id generator: 'assigned'
        table 'app_user'
    }
}
