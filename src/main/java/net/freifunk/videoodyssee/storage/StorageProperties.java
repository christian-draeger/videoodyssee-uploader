package net.freifunk.videoodyssee.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    private String location = "java.io.tmpdir";

    public String getLocation() {
        return System.getProperty(location);
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
