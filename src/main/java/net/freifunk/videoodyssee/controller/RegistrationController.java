package net.freifunk.videoodyssee.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import net.freifunk.videoodyssee.lambdacd.client.ProcessorClient;
import net.freifunk.videoodyssee.model.UploadForm;
import net.freifunk.videoodyssee.storage.StorageFileNotFoundException;
import net.freifunk.videoodyssee.storage.StorageService;
import net.freifunk.videoodyssee.upload.FileMover;
import net.freifunk.videoodyssee.upload.Validator;
import net.freifunk.videoodyssee.voctoweb.client.Conferences;
import net.freifunk.videoodyssee.voctoweb.client.PublicApiClient;

@Slf4j
@Controller
public class RegistrationController {

    private final StorageService storageService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private FileMover fileMover;

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
        return "uploadForm";
    }

    @PostMapping("/add")
    public String addVideo(@ModelAttribute UploadForm form, ModelMap model, RedirectAttributes redirectAttributes) {

        validator.validateForm(form, redirectAttributes);
        if (validator.redirect(redirectAttributes)) {
            return "redirect:/";
        }

        // TODO: validate if file is video file
        final MultipartFile file = form.getVideo();

        model.put("name", form.getName());
        model.put("email", form.getEmail());
        model.put("title", form.getTitle());
        model.put("releaseDate", form.getReleaseDate());
        model.put("conference", form.getConference());
        model.put("videoName", file.getOriginalFilename());

        storageService.store(file);
        fileMover.copyTo(uploadPath, file);
        processorClient.trigger(form);

        model.put("message", "Upload successful");

        return "success";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/video/{videoName}")
    @ResponseBody
    public byte[] getVideo(@PathVariable("videoName") String fileName) throws IOException {
        File file = new File(uploadPath + fileName);
        return Files.readAllBytes(file.toPath());
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<String> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
