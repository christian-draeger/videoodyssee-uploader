package net.freifunk.videoodyssee.storage;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.freifunk.videoodyssee.model.VideoFileInformation;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

    @Value("${spring.servlet.multipart.location}")
    private Path tempUploadDir;

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            Files.copy(file.getInputStream(), this.tempUploadDir.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public VideoFileInformation store(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int responseCode = connection.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK && connection.getContentType().startsWith("video/")) {
            String filename = "";
            String disposition = connection.getHeaderField("Content-Disposition");

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    filename = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                filename = urlString.substring(urlString.lastIndexOf("/") + 1,
                        urlString.length());
            }

            // opens input stream from the HTTP connection
            InputStream inputStream = connection.getInputStream();
            String saveFilePath = this.tempUploadDir.resolve(filename).toAbsolutePath().toString();

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            connection.disconnect();
            return VideoFileInformation.builder().filename(filename).build();
        } else {
            log.warn("File not found or content type invalid. HTTP Status {}, content type {}", responseCode, connection.getContentType());
            connection.disconnect();
            throw new IOException("File not found or content type invalid.");
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.tempUploadDir, 1)
                    .filter(path -> !path.equals(this.tempUploadDir))
                    .map(this.tempUploadDir::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return tempUploadDir.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(tempUploadDir.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(tempUploadDir);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize net.freifunk.videoodyssee.storage", e);
        }
    }
}
