package net.freifunk.videoodyssee.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadForm {

    private String title;
    private String subTitle;
    private String persons;
    private String tags;
    private String conference;
    private String language;
    private String releaseDate;
    private MultipartFile video;
    private String videoUrl;
    private String name;
    private String email;
    private String link;
    private String description;

}
