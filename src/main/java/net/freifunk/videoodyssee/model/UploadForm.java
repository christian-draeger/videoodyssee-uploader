package net.freifunk.videoodyssee.model;

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
    private MultipartFile video;

}
