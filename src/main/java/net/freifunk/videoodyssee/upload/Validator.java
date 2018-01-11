package net.freifunk.videoodyssee.upload;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.freifunk.videoodyssee.model.UploadForm;

@Component
public class Validator {

    public RedirectAttributes validateForm(UploadForm form, RedirectAttributes redirectAttributes) {

        if (form.getName().isEmpty()) {
            redirectAttributes.addFlashAttribute("nameErrorMessage", "Please add your name.");
        }

        if (form.getEmail().isEmpty()) {
            redirectAttributes.addFlashAttribute("emailErrorMessage", "Please add your Email address.");
        }

        if (form.getTitle().isEmpty()) {
            redirectAttributes.addFlashAttribute("titleErrorMessage", "Please add the videos title.");
        }

        if (form.getVideo().getSize() == 0 && (form.getVideoUrl() == null || form.getVideoUrl().trim().isEmpty())) {
            redirectAttributes.addFlashAttribute("videoErrorMessage", "Please select a file to upload or give an URL to the file.");
        }

        if (form.getConference().isEmpty()) {
            redirectAttributes.addFlashAttribute("conferenceErrorMessage", "Please select a conference.");
        }

        if (form.getLanguage().isEmpty()) {
            redirectAttributes.addFlashAttribute("languageErrorMessage", "Please select a language.");
        }

        if (!redirectAttributes.getFlashAttributes().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please fill in all mandatory fields.");
        }

        return redirectAttributes;
    }

    public boolean redirect(RedirectAttributes redirectAttributes) {
        return !redirectAttributes.getFlashAttributes().isEmpty();
    }
}
