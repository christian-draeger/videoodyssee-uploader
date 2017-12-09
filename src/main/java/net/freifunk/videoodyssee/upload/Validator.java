package net.freifunk.videoodyssee.upload;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.freifunk.videoodyssee.model.UploadForm;

@Component
public class Validator {

    public RedirectAttributes validateForm(UploadForm form, RedirectAttributes redirectAttributes) {

        if (form.getVideo().getSize() == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload");
        }

        if (form.getEmail().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please add Email address");
        }

        if (form.getName().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please add name");
        }

        if (form.getTitle().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please add title");
        }

        if (form.getReleaseDate().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please add release date");
        }

        return redirectAttributes;
    }

    public boolean redirect(RedirectAttributes redirectAttributes) {
        return !redirectAttributes.getFlashAttributes().isEmpty();
    }
}
