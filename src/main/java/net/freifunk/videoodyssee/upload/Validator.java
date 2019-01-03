package net.freifunk.videoodyssee.upload;

import net.freifunk.videoodyssee.model.UploadForm;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
public class Validator {

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String URL_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public RedirectAttributes validateForm(UploadForm form, RedirectAttributes redirectAttributes) {

        if (form.getName().isEmpty()) {
            redirectAttributes.addFlashAttribute("nameErrorMessage", "Please add your name.");
        }

        if (form.getEmail().isEmpty()) {
            redirectAttributes.addFlashAttribute("emailErrorMessage", "Please add your Email address.");
        }

        if (!form.getEmail().isEmpty() && !form.getEmail().matches(EMAIL_REGEX)) {
            redirectAttributes.addFlashAttribute("emailErrorMessage", "Email address not valid.");
        }

        if (form.getTitle().isEmpty()) {
            redirectAttributes.addFlashAttribute("titleErrorMessage", "Please add the videos title.");
        }

        if (form.getVideo().getSize() == 0 && (form.getVideoUrl() == null || form.getVideoUrl().trim().isEmpty())) {
            redirectAttributes.addFlashAttribute("videoErrorMessage", "Please select a file to upload or give an URL to the file.");
        }

//        if (!form.getVideoUrl().isEmpty() && !form.getVideoUrl().matches(URL_REGEX)) {
//            redirectAttributes.addFlashAttribute("videoUrlErrorMessage", "The file url is not a valid url.");
//        }

        if (!form.getLink().isEmpty() && !form.getLink().matches(URL_REGEX)) {
            redirectAttributes.addFlashAttribute("linkUrlErrorMessage", "The file url is not a valid url.");
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
