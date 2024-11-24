package org.acme

import static grails.gorm.hibernate.mapping.MappingBuilder.orm

/**
 * Models the completion of an activity by a user
 */
class CompletedActivity implements Serializable {
    
    User user
    Activity activity
    Date dateCreated

    static mapping = orm {
        composite('user', 'activity')
    }
}
