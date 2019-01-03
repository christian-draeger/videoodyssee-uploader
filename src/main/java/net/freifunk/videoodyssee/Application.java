package net.freifunk.videoodyssee;

import net.freifunk.videoodyssee.storage.FileSystemStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(FileSystemStorageService storageService) {
        return args -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
