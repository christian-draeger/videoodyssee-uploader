package net.freifunk.videoodyssee.controller;

import lombok.extern.slf4j.Slf4j;
import net.freifunk.videoodyssee.lambdacd.client.ProcessorClient;
import net.freifunk.videoodyssee.model.Languages;
import net.freifunk.videoodyssee.model.UploadForm;
import net.freifunk.videoodyssee.storage.FileSystemStorageService;
import net.freifunk.videoodyssee.storage.StorageFileNotFoundException;
import net.freifunk.videoodyssee.upload.Validator;
import net.freifunk.videoodyssee.voctoweb.client.Conferences;
import net.freifunk.videoodyssee.voctoweb.client.PublicApiClient;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class RegistrationController {

    private static final String SEPARATOR = ",";
    @Autowired
    private FileSystemStorageService storageService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private Validator validator;

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
        String videoFileName = storageService.storeFileAndGetFilename(form);

        model.put("title", form.getTitle());
        model.put("persons", form.getPersons().split(SEPARATOR));
        model.put("tags", form.getTags().split(SEPARATOR));
        model.put("conference", form.getConference());
        model.put("language", form.getLanguage());
        model.put("releaseDate", form.getReleaseDate());
        model.put("videoName", videoFileName);
        model.put("name", form.getName());
        model.put("email", form.getEmail());
        model.put("link", form.getLink());
        model.put("description", form.getDescription());

        processorClient.trigger(form, videoFileName);

        model.put("buildNumber", 2); // TODO: use buildnumber that will be returned from processorClient

        waitForAnimation(1000);

        return new ModelAndView("success", "formData", model);
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
