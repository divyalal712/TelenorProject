package no.training.project;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.training.project.exception.ServiceException;
import no.training.project.resource.version.VersionResource;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class VersionTest {
    @Mock
    private HttpClient defaultHttpClient;
    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void successTest() throws IOException, InterruptedException, ServiceException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        String externalResponseString = externalResponseString();
        when(response.body()).thenReturn(externalResponseString);
        HttpResponse response2= mock(HttpResponse.class);
        when(response2.statusCode()).thenReturn(404);
        VersionResource version = new VersionResource(httpClient);
        when(httpClient.send(any(), any())).thenReturn(response, response2);
        List<String> release = version.getReleaseVersion("arm");
        Assertions.assertNotNull(release);
        Assertions.assertEquals(10, release.size());
    }

    private static String externalResponseString() throws IOException {
        String externalResponseString = new String(
                VersionTest.class.getResourceAsStream(
                        "/mock-data/release-response.json").readAllBytes());
        return externalResponseString;
    }
    @Test
    public void validAndNonParsableResponse() throws IOException, InterruptedException, ServiceException {

        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        String externalResponseString = externalResponseString();
        when(response.body()).thenReturn(externalResponseString);

        HttpResponse response2 = mock(HttpResponse.class);
        when(response2.statusCode()).thenReturn(200);
        String externalResponseString2 = new String(
                VersionTest.class.getResourceAsStream(
                        "/mock-data/release-response-not-parsable.json").readAllBytes());
        when(response2.body()).thenReturn(externalResponseString2);
        assertThrows(RuntimeException.class, () -> {
            VersionResource version = new VersionResource(httpClient);
            when(httpClient.send(any(), any())).thenReturn(response).thenReturn(response2);
            List<String> release1 = version.getReleaseVersion("arm");
            verify(httpClient, times(2)).send(any(), any());
            verify(response, times(1)).statusCode();
            verify(response2, times(1)).statusCode();
            verify(response, times(1)).body();
            verify(response2, times(1)).body();
        });
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
        assertThrows(RuntimeException.class, () -> {
            HttpClient httpClient = mock(HttpClient.class);
            VersionResource version = new VersionResource(httpClient);

            when(httpClient.send(any(), any())).thenThrow(new RuntimeException("Found Runtime Exception"));
            version.getReleaseVersion("");
            ILoggingEvent exception;
        });
    }
}




