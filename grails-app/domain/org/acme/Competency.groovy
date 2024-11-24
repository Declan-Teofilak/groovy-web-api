package org.acme

/**
 * Models a competency by its title and description
 */
class Competency implements Serializable {

    Long id
    String title
    String description

    static constraints = {
    }

    static mapping = {
        id generator: 'assigned'
    }
}
