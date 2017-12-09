package net.freifunk.videoodyssee.lambdacd.client;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.Unirest;

import net.freifunk.videoodyssee.model.UploadForm;

@Component
public class ProcessorClient {

    @Value("${lambdacd.trigger.endpoint}")
    private String endpoint;

    @Value("${lambdacd.trigger.user}")
    private String user;

    @Value("${lambdacd.trigger.pass}")
    private String pass;

    public void trigger(UploadForm form) {

        JSONObject payload = new JSONObject();
        try {
            payload.put("name", form.getName());
            payload.put("email", form.getEmail());
            payload.put("title", form.getTitle());
            payload.put("releaseDate", form.getReleaseDate());
            payload.put("conference", form.getConference());
            payload.put("fileName", form.getVideo().getOriginalFilename());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Unirest
                .post(endpoint)
                .basicAuth(user, pass)
                .body(payload);
    }
}
