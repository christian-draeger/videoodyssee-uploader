package net.freifunk.videoodyssee.voctoweb.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
public class Conferences implements Serializable {

    @JsonProperty("conferences")
    @Valid
    private List<ConferenceInformation> conferencesList;
    private static final long serialVersionUID = 5108926256863801989L;

}
