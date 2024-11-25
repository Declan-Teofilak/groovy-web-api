## Create Completion & Leaderboard APIs
The goal of this project is to extend a [Grails 6.x](https://docs.grails.org/6.1.x/guide/single.html) REST JSON API to include support for the following APIs:
1. Completing activities for a user (Completion API)
   1. This can be seen as "checking off" tasks on a task list
   2. Gaining experience/points as a result of completing an activity
1. Viewing the current leaderboard (Leaderboard API)
   1. Viewing which users are top performers based on points they've acquired

The application is set up with an in memory H2 DB so no external services are required to be installed. You have the following domain models already 
built and test data is seeded upon start up:
1. `grails-app/domain/org/acme/Activity.groovy`
1. `grails-app/domain/org/acme/Competency.groovy`
1. `grails-app/domain/org/acme/CompletedActivity.groovy`
1. `grails-app/domain/org/acme/Level.groovy`
1. `grails-app/domain/org/acme/User.groovy`

Be sure to include documentation on how to leverage the above APIs. Note the initial source is all written in Groovy 3.

### Running
As long as your system contains a configured JDK 11 you should be able to run the dev api via `./gradlew bootRun` to start the application. 

Once booted the following endpoints are available (all mapped via `grails-app/controllers/org/acme/UrlMappings.groovy`):
- GET http://localhost:8080/api/levels
  - The request's controller is at `grails-app/controllers/org/acme/LevelController.groovy`.
- GET http://localhost:8080/api/activities
   - The request's controller is at `grails-app/controllers/org/acme/ActivityController.groovy`.
- GET http://localhost:8080/api/users
   - The request's controller is at `grails-app/controllers/org/acme/UserController.groovy`.
- GET|POST http://localhost:8080/api/users/$userId/complete/$activityId
   - The request's controller is at `grails-app/controllers/org/acme/UserController.groovy`.
- GET http://localhost:8080/api/leaderboard
   - The request's controller is at `grails-app/controllers/org/acme/LeaderboardController.groovy`.

#### JDK Install Option 1 - Sdkman
Install [sdkman](https://sdkman.io/) on your machine and install a JDK 11 in this project, such as Zulu 11.

#### JDK Install Option 2 Use Nix & Devenv
Install [Devenv](https://devenv.sh/getting-started/) on your machine and run `devenv shell` in this project to bootstrap the required JDK in your shell.

### Completion API - Additional Info
The API skeleton is available at GET|POST http://localhost:8080/api/users/$userId/complete/$activityId, tapping the endpoint
should record a completion for a user and an activity. A user can only complete an activity once.

### Leaderboard API - Additional Info
The API skeleton is available at GET http://localhost:8080/api/leaderboard. The API should return the following information for each entry with a max of 20 entries:
1. Rank 1-20
1. User ID
1. Total points
1. Current level
1. User's Class Standing 

Entries should be ranked by the game rules below. The API should also contain a parameter to filter by class standing to only include users with a matching class standing.

### Game rules
The following are the requirements at each level, also note that an activity with multiple competencies acts as a multiplier on the points gained but does not count as multiple activity completions. 

Levels 1-3 should offer the following points and level progression:

1. Level 1
   1. Awards 10 pts per activity completion
   1. Completing 3 Level 1 activities levels up a user to Level 2
1. Level 2 : Points acquired per completion = 20
    1. Awards 20 pts per activity completion
    1. Completing 3 Level 2 activities levels up a user to Level 3
1. Level 3 : Points acquired per completion = 40
    1. Awards 40 pts per activity completion

Students are ranked by their overall level and then their total points acquired.
For example Student A at Level 2, Total points of 100 is higher than Student B at Level 1, Total points of 300.

### Troubleshooting
If you find yourself wanting to inspect queries that have been run or the current state of the development DB you can
set the environment variable `APP_SQL_LOGGER` prior to running the API, the API will then log all queries, parameters, and results.

You can also inspect the H2 database directly by opening url: `http://localhost:8080/h2-console/login.do` and then specifying
the connection url `dbc:h2:mem:devDb;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE`.

### Solutions are Judged on
1. Readability of your code
1. Maintainability of your code
1. Test coverage
1. Correctness
1. Documentation

### Submission
Update README to include descriptions for each required API endpoint and how to interact with them. Submit your project by providing a github url or zip file containing your submission.

### API Endpoint Updates

- GET|POST http://localhost:8080/api/users/$userId/complete/$activityId
    - The request's controller is at `grails-app/controllers/org/acme/UserController.groovy`.
    - If the supplied userId or activityId does not tie to a valid User or Activity respectively, you will receive a 400 status
    - Additionally, supplying a valid userId and activityId of which there already exists a CompletedActivity record will return a 409 status
      - The 409 Conflict status better indicates that the request could not be completed due to a duplicate record, rather than a functional error
- GET http://localhost:8080/api/leaderboard
    - The request's controller is at `grails-app/controllers/org/acme/LeaderboardController.groovy`.
    - The leaderboard request will return the top 20 users, sorted first by their level (descending), then by their total points earned (descending)
    - If no completed activities are on record, the response will return an empty array
- GET http://localhost:8080/api/leaderboard/standing/$classStanding
    - The request's controller is at `grails-app/controllers/org/acme/LeaderboardController.groovy`.
    - This request will return the leaderboard data for a specified subset of Users, determined by the User's Class Standing
    - The response will contain an empty data array, similar to the general leaderboard request, if there is no corresponding set of Users with a classStanding that matches the provided parameter

### Test Coverage

I added test coverage to ensure the basic functionality of completing an activity for a specific user works as expected. 
However, I chose not to expand the tests beyond this initial coverage for the following reasons:
   - My primary focus was on delivering the functional requirements, as I felt this was the most critical aspect of the task.
   - Given my limited familiarity with the language and tools, I prioritized learning how to implement the core functionality over diving deeply into writing additional tests.

That said, I’m pleased to have successfully learned the syntax for the test suite and covered the fundamental functionality. 
In a typical development scenario, I would aim to have comprehensive test coverage for all the code I write.
