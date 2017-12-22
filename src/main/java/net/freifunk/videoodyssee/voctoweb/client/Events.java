package net.freifunk.videoodyssee.voctoweb.client;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Events implements Serializable {

    private static final long serialVersionUID = 5892745473249862710L;
    private List<Event> listOfEvents;

}
