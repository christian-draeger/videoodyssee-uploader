package net.freifunk.videoodyssee.voctoweb.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Serializable {

    @JsonProperty("guid")
    private String guid;
    @JsonProperty("title")
    private String title;
    @JsonProperty("subtitle")
    private String subtitle;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("link")
    private String link;
    @JsonProperty("description")
    private String description;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("persons")
    @Valid
    private List<String> persons;
    @JsonProperty("tags")
    @Valid
    private List<String> tags;
    @JsonProperty("view_count")
    private int viewCount;
    @JsonProperty("promoted")
    private boolean promoted;
    @JsonProperty("metadata")
    @Valid
    private Metadata metadata;
    @JsonProperty("date")
    private String date;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("length")
    private int length;
    @JsonProperty("duration")
    private int duration;
    @JsonProperty("thumb_url")
    private String thumbUrl;
    @JsonProperty("poster_url")
    private String posterUrl;
    @JsonProperty("frontend_link")
    private String frontendLink;
    @JsonProperty("url")
    private String url;
    @JsonProperty("conference_url")
    private String conferenceUrl;

    private static final long serialVersionUID = -8841088952485680047L;
}
