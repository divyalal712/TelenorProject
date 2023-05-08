package no.training.project.resource.version;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.training.project.Mapper.ReleaseMapper;
import no.training.project.exception.ServiceException;
import no.training.project.model.ReleaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Path("/release-name")
public class VersionResource {
    private static final String JAVA_VERSION_URI = "https://api.adoptopenjdk.net/v3/info/release_names?architecture=%s&heap" +
            "_size=normal&image_type=jdk&page=2&page_size=10&project=jdk&release_type=ga" +
            "&sort_method=DEFAULT&sort_order=DESC&vendor=adoptopenjdk";
    private static final Logger LOG = LoggerFactory.getLogger(VersionResource.class);

    private final ReleaseMapper releaseMapper = new ReleaseMapper();
    private HttpClient httpClient;

    public VersionResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public VersionResource() {
        this.httpClient = HttpClient.newHttpClient();
    }
    @GET
    @Path("/version")
    @Produces(MediaType.APPLICATION_JSON)
    public ReleaseResponse getReleaseVersion(@QueryParam("architecture") @DefaultValue("x32") String architecture)
            throws IOException, InterruptedException, ServiceException {
        try {
            String uri = String.format(JAVA_VERSION_URI, architecture);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri))
                    .method("GET", HttpRequest.BodyPublishers.noBody()).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {

                ReleaseResponse releases = releaseMapper.getReleaseMapperObject(response);
                LOG.debug("Returns the external response: {}", releases != null);
                return releases;
            } else {
                LOG.debug("External call failed with status code {}, message {}", response.statusCode(), response.body());
                throw new ServiceException("Internal error found", response.statusCode());
            }
        } catch (Exception e) {
            LOG.error("Exception found", e);
            throw e;
        }
    }
}
