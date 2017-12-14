package net.freifunk.videoodyssee.lambdacd.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Value("${upload.path}")
    private String uploadPath;

    public void trigger(UploadForm form) {

        log.info("trigger lambdacd method called");

        JSONObject payload = new JSONObject();
        try {
            payload.put("name", form.getName());
            payload.put("email", form.getEmail());
            payload.put("title", form.getTitle());
            payload.put("subtitle", "");
            payload.put("date", getCurrentDate());
            payload.put("releaseDate", form.getReleaseDate());
            payload.put("videoFilePath", uploadPath + form.getVideo().getOriginalFilename());
            payload.put("conferenceAcronym", "");
            payload.put("language", "");
            payload.put("link", "");
            payload.put("description", "");
            payload.put("tags", "[]");
            payload.put("persons", "[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        log.info("lambdaCD responds with status {}", response.getStatus());
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date).toString();
    }
}
