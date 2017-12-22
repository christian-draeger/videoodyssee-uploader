package net.freifunk.videoodyssee.model;

import lombok.Getter;

@Getter
public enum Languages {

    ENGLISH("eng", "English"),
    GERMAN("deu", "German"),
    SWISS_GERMAN("gsw", "Swiss German"),
    FRENCH("fra", "French"),
    SPANISH("spa", "Spanish"),
    JAPANESE("jpn", "Japanese"),
    RUSSION("rus", "Russian");

    String abbrevation;
    String value;

    Languages(String abbrevation, String value) {
        this.abbrevation = abbrevation;
        this.value = value;
    }


}
