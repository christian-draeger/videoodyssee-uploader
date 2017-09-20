package net.freifunk.videoodyssee.voctoweb.client;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Conferences implements Serializable {

    @JsonProperty("conferences")
    @Valid
    private List<ConferenceInformation> conferencesList;
    private static final long serialVersionUID = 5108926256863801989L;

}
