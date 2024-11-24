package org.acme

class LevelController {

    LevelService levelService

    def index() {
        render(contentType: "application/json", view: "/level/index", model: [levels: levelService.listLevels() ])
    }
}
