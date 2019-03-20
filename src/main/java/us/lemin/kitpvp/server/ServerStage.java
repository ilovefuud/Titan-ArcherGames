package us.lemin.kitpvp.server;

import java.util.concurrent.TimeUnit;

public enum ServerStage {
    STARTING("Starting", TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES)),
    GRACE("Grace", TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES)),
    FIGHTING("Fighting", null);

    public String name;
    public Long duration;


    ServerStage(String name, Long duration) {
        this.name = name;
        this.duration = duration;
    }

    public Long getDuration() {
        return duration == null ? -1 : duration;
    }
}
