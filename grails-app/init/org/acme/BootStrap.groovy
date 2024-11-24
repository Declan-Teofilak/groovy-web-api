package org.acme

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord

class BootStrap {

    def init = { servletContext ->

        Map<Long, Level> levels = [:]
        List<User> users = []
        Map<Long, Competency> competencies = [:]
        Map<Long, Activity> activities = [:]

        Level.withTransaction {
            // Add initial levels
            getRecords("seed/levels.csv").each({ CSVRecord record ->

                Level model = new Level()
                model.with({
                    id = record.get("id").toLong()
                    title = record.get("title")
                    position = record.get("position").toInteger()
                    activitiesRequired = record.get("activitiesRequired").toInteger()
                    pointsAwarded = record.get("pointsAwarded").toInteger()
                })
                levels[model.id] = model
            })

            // Add initial users
            getRecords("seed/users.csv").each({ CSVRecord record ->

                User model = new User()
                model.with({
                    id = record.get("id").toLong()
                    avatarUrl = record.get("avatarUrl")
                    lastName = record.get("lastName")
                    firstName = record.get("firstName")
                    classStanding = record.get("classStanding")
                })
                model.save()
                users.add(model)
            })

            // Add initial users
            getRecords("seed/competencies.csv").each({ CSVRecord record ->

                Competency model = new Competency()
                model.with({
                    id = record.get("id").toLong()
                    title = record.get("title")
                    description = record.get("description")
                })
                model.save()
                competencies[model.id] = model
            })

            // Add initial activities
            getRecords("seed/activities.csv").each({ CSVRecord record ->

                Activity model = new Activity()
                model.with({
                    id = record.get("id").toLong()
                    title = record.get("title")
                    level = levels[record.get("levelId").toLong()]
                })

                activities[model.id] = model
            })

            // Build many to many association
            getRecords("seed/activity_competencies.csv").each({ CSVRecord record ->

                activities[record.get("activityId").toLong()]
                        .addToCompetencies(competencies[record.get("competencyId").toLong()])
            })

            Activity.saveAll(activities.values())
        }
    }

    def destroy = {
    }

    /**
     * Retrieve rows from a csv file specified by its path. Assumes csv contains a well formed header row.
     * @param path
     * @return
     */
    Iterable<CSVRecord> getRecords(String path) {
        Reader input = new FileReader(path)
        return CSVFormat.RFC4180.withFirstRecordAsHeader().parse(input)
    }
}
