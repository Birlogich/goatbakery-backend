package org.example.controller;

import org.example.utils.LogbackWrapper;
import org.slf4j.Logger;


import static spark.Spark.port;

public class ServiceMain {
    protected static Logger log = LogbackWrapper.getLogger(ServiceMain.class);

    public static void main(String[] args) {
        startService();
    }

    public static void startService() {
        log.info("starting services");
        startWebServer();
        log.info("Service startup complete");
    }

    private static void startWebServer() {
        log.info("starting API server");
        port(9090);
        log.info("using port: {}", 9090);
        new Controller();
    }
}
