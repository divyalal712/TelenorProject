package no.training.project.Mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.training.project.model.ReleaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpResponse;

public class ReleaseMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(ReleaseMapper.class);
    public ReleaseResponse getReleaseMapperObject(HttpResponse<String> response){
        ReleaseResponse release;
        try {
            release = objectMapper.readValue(response.body(), ReleaseResponse.class);
        } catch (JsonProcessingException e) {
            LOG.error("The error in converting Json to object is: {}",e.getMessage());
            throw new RuntimeException(e);
        }
        LOG.debug("The response is {}",release);
        return release;
    }
}

