package net.freifunk.videoodyssee.upload;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileMover {

    @Value("${spring.http.multipart.location}")
    private String tempUploadDir;

    public void copyTo(String path, MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        final File file = new File(tempUploadDir + fileName);
        file.renameTo(new File(path + fileName));
        file.delete();
    }
}
