package net.freifunk.videoodyssee.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import net.freifunk.videoodyssee.model.VideoFileInformation;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    VideoFileInformation store(String urlString) throws IOException;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
