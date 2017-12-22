package net.freifunk.videoodyssee.lambdacd.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.slugify.Slugify;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.extern.slf4j.Slf4j;
import net.freifunk.videoodyssee.model.UploadForm;
import net.freifunk.videoodyssee.voctoweb.client.Event;
import net.freifunk.videoodyssee.voctoweb.client.PublicApiClient;

@Slf4j
@Component
public class ProcessorClient {

    @Value("${lambdacd.trigger.endpoint}")
    private String endpoint;

    @Value("${lambdacd.trigger.user}")
    private String user;

    @Value("${lambdacd.trigger.pass}")
    private String pass;

    @Value("${spring.http.multipart.location}")
    private String tempUploadDir;

    @Autowired
    private PublicApiClient publicApiClient;

    public void trigger(UploadForm form) {

        log.info("trigger lambdacd method called");

        JSONObject payload = new JSONObject();
        try {
            payload.put("name", form.getName());
            payload.put("email", form.getEmail());
            payload.put("title", form.getTitle());
            payload.put("subtitle", form.getSubTitle());
            payload.put("date", getCurrentDate());
            payload.put("releaseDate", form.getReleaseDate());
            payload.put("videoFilePath", tempUploadDir + form.getVideo().getOriginalFilename());
            payload.put("conferenceAcronym", form.getConference());
            payload.put("language", form.getLanguage());
            payload.put("link", form.getLink());
            payload.put("description", form.getDescription());
            payload.put("tags", new JSONArray().put(form.getTags()));
            payload.put("persons", new JSONArray().put(form.getPersons()));
            payload.put("slug", slugifyString(form.getTitle()));
        } catch (JSONException e) {
            log.warn("Error building JSON: ", e);
        }

        log.info(payload.toString());
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

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String slugifyString(String string) {
        int counter = 1;
        List<String> slugs = publicApiClient.getListOfallEvents().getListOfEvents().stream().map(Event::getSlug).collect(Collectors.toList());
        StringBuilder slug = new StringBuilder(new Slugify().slugify(string));
        while (slugs.contains(slug.toString())) {
            slug.append("-").append(counter);
            counter++;
        }
        return slug.toString();
    }
}
