package net.freifunk.videoodyssee.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import net.freifunk.videoodyssee.model.VideoFileInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import net.freifunk.videoodyssee.lambdacd.client.ProcessorClient;
import net.freifunk.videoodyssee.model.Languages;
import net.freifunk.videoodyssee.model.UploadForm;
import net.freifunk.videoodyssee.storage.StorageFileNotFoundException;
import net.freifunk.videoodyssee.storage.StorageService;
import net.freifunk.videoodyssee.upload.Validator;
import net.freifunk.videoodyssee.voctoweb.client.Conferences;
import net.freifunk.videoodyssee.voctoweb.client.PublicApiClient;

@Slf4j
@Controller
public class RegistrationController {

    private static final String SEPARATOR = ",";
    private final StorageService storageService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private Validator validator;

    @Autowired
    public RegistrationController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    private PublicApiClient publicApiClient;

    @Autowired
    private ProcessorClient processorClient;

    @GetMapping("/")
    public String uploaderForm(Model model) {
        Conferences listOfAllConferences = publicApiClient.getListOfAllConferences();
        model.addAttribute("conferences", listOfAllConferences.getConferencesList());
        Map<String, String> languages = Arrays.stream(Languages.values()).collect(Collectors.toMap(Languages::getAbbrevation, Languages::getValue));
        model.addAttribute("languages", languages);
        return "uploadForm";
    }

    @PostMapping("/add")
    public ModelAndView addVideo(@ModelAttribute UploadForm form, ModelMap model, RedirectAttributes redirectAttributes) {

        validator.validateForm(form, redirectAttributes);
        if (validator.redirect(redirectAttributes)) {
            return new ModelAndView("redirect:/");
        }

        // TODO: validate if file is video file
        String videoFileName;
        if (form.getVideoUrl() == null || form.getVideoUrl().trim().isEmpty()) {
            final MultipartFile file = form.getVideo();
            storageService.store(file);
            videoFileName = file.getOriginalFilename();
        } else {
            try {
                VideoFileInformation videoFileInformation = storageService.store(form.getVideoUrl());
                videoFileName = videoFileInformation.getFilename();
            } catch (IOException e) {
                throw new DownloadFileException(form.getVideoUrl());
            }
        }

        model.put("title", form.getTitle().split(SEPARATOR));
        model.put("persons", form.getPersons().split(SEPARATOR));
        model.put("tags", form.getTags());
        model.put("conference", form.getConference());
        model.put("language", form.getLanguage());
        model.put("releaseDate", form.getReleaseDate());
        model.put("videoName", videoFileName);
        model.put("name", form.getName());
        model.put("email", form.getEmail());
        model.put("link", form.getLink());
        model.put("description", form.getDescription());

        processorClient.trigger(form);

        model.put("buildNumber", 2); // TODO: use buildnumber that will be returned from processorClient

        waitForAnimation(1000);

        return new ModelAndView("redirect:/#upload", "formData", model);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<String> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    private void waitForAnimation(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.warn("waitForAnimation failed", e);
        }
    }
}
