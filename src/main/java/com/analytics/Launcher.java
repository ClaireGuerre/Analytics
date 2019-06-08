package com.analytics;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class Launcher {

    public static final URI BASE_URI = getBaseURI();

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost/analytics/").port(9991).build();
    }

    public static void main(String[] args) {
        ResourceConfig ressourceConfig = new ResourceConfig();
        ressourceConfig.registerClasses(Analytics.class);

        try {
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, ressourceConfig);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

