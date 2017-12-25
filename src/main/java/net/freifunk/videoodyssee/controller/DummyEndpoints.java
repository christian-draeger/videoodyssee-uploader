package net.freifunk.videoodyssee.controller;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyEndpoints {

    @GetMapping("/build-status")
    public HashMap<String, String> getBuildStatus() {

        String status = randomStatus();

        HashMap<String, String> map = new HashMap<>();

        for (int i = 1; i <= 5; i++) {

            String key = "step"+i;

            map.put(key, status);
            if (status.equals("success")) {
                status = randomStatus();
            }

            if (status.equals("error")) {
                map.put(key, status);
                status = "unknown";
            } else {
                map.put(key, status);
            }
        }
        return map;
    }

    private String randomStatus() {
        final int i = ThreadLocalRandom.current().nextInt(0, 1+1);
        return i == 0 ? "success" : "error";
    }
}
