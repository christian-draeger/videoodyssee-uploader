package net.freifunk.videoodyssee.lambdacd.client;

import lombok.Builder;
import lombok.Data;
import org.json.JSONArray;

@Builder
@Data
public class LambdacdData {
    String name;
    String email;
    String title;
    String subtitle;
    String releaseDate;
    String videoFileName;
    String conferenceAcronym;
    String language;
    String link;
    String description;
    JSONArray tags;
    JSONArray persons;
}
