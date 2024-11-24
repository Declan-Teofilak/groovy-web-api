package org.acme

/**
 * Models an experience by it's title, level of difficulty, and competencies built
 */
class Activity implements Serializable {

    static hasMany = [competencies: Competency]

    Long id
    String title
    Level level

    static constraints = {
    }

    static mapping = {
        id generator: 'assigned'
    }
}
