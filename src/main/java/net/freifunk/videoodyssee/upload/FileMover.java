package net.freifunk.videoodyssee.upload;

import java.io.File;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileMover {

    public void copyTo(String path, MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        final File file = new File("upload-dir/" + fileName);
        file.renameTo(new File(path + fileName));
        file.delete();
    }
}
