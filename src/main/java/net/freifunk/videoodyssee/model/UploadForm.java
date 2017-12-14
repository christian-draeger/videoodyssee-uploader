package net.freifunk.videoodyssee.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadForm {

    private String title;
    private List<String> persons;
    private List<String> tags;
    private String conference;
    private String language;
    private String releaseDate;
    private MultipartFile video;
    private String name;
    private String email;
    private String link;
    private String description;

}
