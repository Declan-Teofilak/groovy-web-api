package org.acme

import grails.gorm.transactions.Transactional

class LevelService {

    /**
     * Responsible for fetching levels available in the app
     */
    @Transactional(readOnly = true)
    List<Level> listLevels() {
        Level.list()
    }
}
