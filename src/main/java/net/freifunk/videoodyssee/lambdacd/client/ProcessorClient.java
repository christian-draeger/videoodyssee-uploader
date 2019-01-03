package net.freifunk.videoodyssee.lambdacd.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import net.freifunk.videoodyssee.model.UploadForm;
import net.freifunk.videoodyssee.voctoweb.client.Event;
import net.freifunk.videoodyssee.voctoweb.client.PublicApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProcessorClient {

    private static final String SEPARATOR = ",";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${lambdacd.trigger.endpoint}")
    private String endpoint;

    @Value("${lambdacd.trigger.user}")
    private String user;

    @Value("${lambdacd.trigger.pass}")
    private String pass;

    @Value("${spring.servlet.multipart.location}")
    private String tempUploadDir;

    @Autowired
    private PublicApiClient publicApiClient;

    public void trigger(UploadForm form, String videoFileName) {

        LambdacdData data = buildLambdacdData(form, videoFileName);

        log.info("trigger lambdacd method called");

        String payload = null;
        try {
            payload = OBJECT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("error converting data to json", e);
        }

        log.info(payload);
        log.info(endpoint);
        log.info("auth: {} {}", user, pass);

        HttpResponse<String> response = null;

        try {
            response = Unirest
                    .post(endpoint)
                    .basicAuth(user, pass)
                    .body(payload)
                    .asString();
        } catch (UnirestException e) {
            log.warn("Error sending request to pipeline: ", e);
        }

        if (response != null) {
            log.info("lambdaCD responds with status {} - {} - {}", response.getStatus(), response.getStatusText(), response.getBody());
        }
    }

    public LambdacdData buildLambdacdData(@ModelAttribute UploadForm form, String videoFileName) {
        return LambdacdData.builder()
                .uuid(form.getUuid())
                .title(form.getTitle())
                .persons(Arrays.asList(form.getPersons().split(SEPARATOR)))
                .tags(Arrays.asList(form.getTags().split(SEPARATOR)))
                .conferenceAcronym(form.getConference())
                .videoFilePath(tempUploadDir + videoFileName)
                .language(form.getLanguage())
                .releaseDate(form.getReleaseDate())
                .name(form.getName())
                .email(form.getEmail())
                .link(form.getLink())
                .description(form.getDescription())
                .slug(slugifyString(form.getTitle()))
                .date(getCurrentDate())
                .build();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String slugifyString(String string) {
        int counter = 1;
        List<String> slugs = publicApiClient.getListOfallEvents().getListOfEvents()
                .stream()
                .map(Event::getSlug)
                .collect(Collectors.toList());
        StringBuilder slug = new StringBuilder(new Slugify().slugify(string));
        while (slugs.contains(slug.toString())) {
            slug.append("-").append(counter);
            counter++;
        }
        return slug.toString();
    }

}