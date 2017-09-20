package net.freifunk.videoodyssee.voctoweb.client;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ConferenceInformation implements Serializable {

    @JsonProperty("acronym")
    private String acronym;
    @JsonProperty("aspect_ratio")
    private String aspectRatio;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("title")
    private String title;
    @JsonProperty("schedule_url")
    private String scheduleUrl;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("webgen_location")
    private String webgenLocation;
    @JsonProperty("logo_url")
    private String logoUrl;
    @JsonProperty("images_url")
    private String imagesUrl;
    @JsonProperty("recordings_url")
    private String recordingsUrl;
    @JsonProperty("url")
    private String url;
    private static final long serialVersionUID = 4675615373692608317L;
}
