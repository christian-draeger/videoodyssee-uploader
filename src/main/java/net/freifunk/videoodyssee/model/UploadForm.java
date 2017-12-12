package net.freifunk.videoodyssee.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadForm {

    private String name;
    private String email;
    private String title;
    private String releaseDate;
    private String conference;
    private String language;
    private String link;
    private String description;
    private String subtitle;
    private MultipartFile video;
    private List<String> persons;
    private List<String> tags;

}
