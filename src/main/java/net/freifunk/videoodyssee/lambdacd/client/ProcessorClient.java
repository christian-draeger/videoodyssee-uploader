package net.freifunk.videoodyssee.lambdacd.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.extern.slf4j.Slf4j;
import net.freifunk.videoodyssee.model.UploadForm;

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

    public void trigger(UploadForm form) {

        log.info("trigger lambdacd method called");

        JSONObject payload = new JSONObject();
        try {
            payload.put("name", form.getName());
            payload.put("email", form.getEmail());
            payload.put("title", form.getTitle());
            payload.put("subtitle", "some subtitle string"); //TODO: add  input to form
            payload.put("date", getCurrentDate());
            payload.put("releaseDate", form.getReleaseDate());
            payload.put("videoFilePath", tempUploadDir + form.getVideo().getOriginalFilename());
            payload.put("conferenceAcronym", "fff17"); //TODO: acronym from conf
            payload.put("language", "deu"); //TODO: acronym from lang
            payload.put("link", form.getLink());
            payload.put("description", form.getDescription());
            payload.put("tags", new JSONArray().put(form.getTags()));
            payload.put("persons", new JSONArray().put(form.getPersons()));
        } catch (JSONException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }

        if (response != null) {
            log.info("lambdaCD responds with status {} - {} - {}", response.getStatus(), response.getStatusText(), response.getBody());
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date).toString();
    }
}
