package ch.virustracker.app.controller;

import ch.virustracker.app.controller.restapi.RestApiController;

public class Controller {

    private final RestApiController restApiController;

    public Controller() {
        this.restApiController = new RestApiController();
    }

    public void fetchNewInfections() {
        restApiController.fetchInfectedTokens(null);
    }
}
