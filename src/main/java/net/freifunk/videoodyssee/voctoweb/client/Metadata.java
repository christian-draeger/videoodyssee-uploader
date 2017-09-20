package net.freifunk.videoodyssee.voctoweb.client;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Metadata implements Serializable {

    @JsonProperty("related")
    @Valid
    private List<Integer> related;
    @JsonProperty("remote_id")
    private String remoteId;
    private static final long serialVersionUID = -4800938417645664950L;

}
