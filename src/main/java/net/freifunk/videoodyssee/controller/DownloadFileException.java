package net.freifunk.videoodyssee.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DownloadFileException extends RuntimeException {
    public DownloadFileException(String videoUrl) {
    }
}
