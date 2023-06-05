package no.training.project;

import no.training.project.resource.datecalculation.DateConversion;
import no.training.project.resource.health.HealthResource;
import no.training.project.resource.martianweather.MartianWeather;
import no.training.project.resource.version.VersionResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    public static final URI BASE_URI = URI.create("http://localhost:8080");
    public static void main(String[] args) {
        ResourceConfig config = new ResourceConfig();
        config.register(HealthResource.class);
        config.register(DateConversion.class);
        config.register(MartianWeather.class);
        config.register(VersionResource.class);
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, config);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
             LOG.debug("Interrupted Exception");
        }
        httpServer.shutdownNow();
    }
}
