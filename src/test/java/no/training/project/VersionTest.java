package no.training.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.training.project.exception.ServiceException;
import no.training.project.model.ReleaseResponse;
import no.training.project.resource.version.VersionResource;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

public class VersionTest {
    @Mock
    private HttpClient defaultHttpClient;
    @Mock
    private ObjectMapper objectMapper;


    private static ReleaseResponse getRelease() throws IOException {
        String response = new String(VersionTest.class.getResourceAsStream("/mock-data/release-response.json").readAllBytes());
        ReleaseResponse responseAsObject = new ObjectMapper().readValue(response, ReleaseResponse.class);
        return responseAsObject;
    }

    @Test
    public void successTest() throws IOException, InterruptedException, ServiceException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        String externalResponseString = new String(VersionTest.class.getResourceAsStream("/mock-data/release-response.json").readAllBytes());
        when(response.body()).thenReturn(externalResponseString);

        HttpResponse response2= mock(HttpResponse.class);
        when(response2.statusCode()).thenReturn(404);
        VersionResource version = new VersionResource(httpClient);
        when(httpClient.send(any(), any())).thenReturn(response, response2);
        List<String> release = version.getReleaseVersion("arm");
        Assertions.assertNotNull(release);
    }


    @Test
    public void validAndNonParsableResponse() throws IOException, InterruptedException, ServiceException {

        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        String externalResponseString = new String(VersionTest.class.getResourceAsStream("/mock-data/release-response.json").readAllBytes());
        when(response.body()).thenReturn(externalResponseString);

        HttpResponse response2 = mock(HttpResponse.class);
        when(response2.statusCode()).thenReturn(200);
        String externalResponseString2 = new String(VersionTest.class.getResourceAsStream("/mock-data/release-response-not-parsable.json").readAllBytes());
        when(response2.body()).thenReturn(externalResponseString2);
        try {
            VersionResource version = new VersionResource(httpClient);
            when(httpClient.send(any(), any())).thenReturn(response).thenReturn(response2);
            List<String> release1 = version.getReleaseVersion("arm");
        } catch (Exception exception) {
            verify(httpClient, times(2)).send(any(), any());
            verify(response, times(1)).statusCode();
            verify(response2, times(1)).statusCode();
            verify(response, times(1)).body();
            verify(response2, times(1)).body();
        }
    }

    @Test
    public void releaseNotFoundTest() throws ServiceException, IOException, InterruptedException {
        try {
            HttpClient httpClient = mock(HttpClient.class);
            HttpResponse response = mock(HttpResponse.class);
            when(response.statusCode()).thenReturn(404);
            VersionResource version = new VersionResource(httpClient);
            when(httpClient.send(any(), any())).thenReturn(response);
            version.getReleaseVersion("");
        } catch (ServiceException exception) {
            Assertions.assertEquals("Internal error found", exception.getErrorMessage());
            Assertions.assertEquals(404, exception.getStatusCode());
        }
    }

    @Test
    public void getInternalErrorTest() throws IOException, InterruptedException {
        try {
            HttpClient httpCLient = mock(HttpClient.class);
            HttpResponse response = mock(HttpResponse.class);
            when(response.statusCode()).thenReturn(500);
            VersionResource version = new VersionResource(httpCLient);
            when(httpCLient.send(any(), any())).thenReturn(response);
            version.getReleaseVersion("");
        } catch (ServiceException exception) {
            Assertions.assertEquals("Internal error found", exception.getErrorMessage());
            Assertions.assertEquals(500, exception.getStatusCode());
        }
    }

    @Test
    public void getHandleException() throws IOException, InterruptedException, ServiceException {
        try {
            HttpClient httpClient = mock(HttpClient.class);
            VersionResource version = new VersionResource(httpClient);

            when(httpClient.send(any(), any())).thenThrow(new RuntimeException("Found Runtime Exception"));
            version.getReleaseVersion("");
        } catch (RuntimeException exception) {
            Assertions.assertEquals("Found Runtime Exception", exception.getMessage());
        }
    }
}




