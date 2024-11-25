package org.acme

import grails.orm.HibernateCriteriaBuilder
import org.hibernate.FetchMode

import grails.gorm.transactions.Transactional

class ActivityService {

    /**
     * Responsible for fetching activities available for users to complete
     */
    @Transactional(readOnly = true)
    List<Activity> listActivities() {
        return (Activity.createCriteria().list({
            HibernateCriteriaBuilder builder = (HibernateCriteriaBuilder) delegate
            builder.fetchMode("level", FetchMode.JOIN)
            builder.fetchMode("competencies", FetchMode.JOIN)
        }) as List<Activity>).unique()
    }
}
