package net.freifunk.videoodyssee.voctoweb.client;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PublicApiClient {

    private static final String PUBLIC_CONFERENCES = "/public/conferences";
    private static final String PUBLIC_EVENTS = "/public/events";
    @Value("${voctoweb.baseuri}")
    private String baseUri;

    @PostConstruct
    private void init() {
        // setup to serialize Json from\to Object using jackson object mapper
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    log.warn("IO Exception", e);
                    throw new RuntimeException(e); //NOSONAR
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    log.warn("Error while processing json", e);
                    throw new RuntimeException(e); //NOSONAR
                }
            }
        });
    }


    public Conferences getListOfAllConferences() {
        try {
            HttpResponse<Conferences> conferencesHttpResponse = Unirest.get(baseUri + PUBLIC_CONFERENCES).asObject(Conferences.class);
            return conferencesHttpResponse.getBody();
        } catch (UnirestException e) {
            log.warn("Could not get Conferences from voctoweb!", e);
        }
        return new Conferences();
    }

    public Events getListOfallEvents() {
        try {
            HttpResponse<Events> eventsHttpResponse = Unirest.get(baseUri + PUBLIC_EVENTS).asObject(Events.class);
            return eventsHttpResponse.getBody();
        } catch (UnirestException e) {
            log.warn("Could not get Events from voctoweb!", e);
        }
        return new Events();
    }

}
