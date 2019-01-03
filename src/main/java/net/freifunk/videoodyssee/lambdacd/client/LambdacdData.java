package net.freifunk.videoodyssee.lambdacd.client;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class LambdacdData {
    String uuid;
    String name;
    String email;
    String title;
    String subtitle;
    String releaseDate;
    String date;
    String videoFilePath;
    String conferenceAcronym;
    String language;
    String link;
    String description;
    String slug;
    List<String> tags;
    List<String> persons;
}
