package dev.mattrm.mc.zparkour.data;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

public class Session {
    private final UUID playerUUID;
    private final int courseId;
    private long startTime = -1;
    private int lastCheckpoint = -1;
    private boolean started = false;
    private List<Long> sectionTimes;
    private List<Long> sectionStarts;
    private long finalTime = -1;

    public Session(UUID playerUUID, int courseId) {
        this.playerUUID = playerUUID;
        this.courseId = courseId;
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.lastCheckpoint = 0;
        this.started = true;
        this.sectionTimes = Lists.newArrayList();
        this.sectionStarts = Lists.newArrayList(this.startTime);
    }

    public long end() {
        this.started = false;
        this.finalTime = System.currentTimeMillis() - this.startTime;
        return this.finalTime;
    }

    public boolean hasStarted() {
        return this.started;
    }

    public int getLastCheckpoint() {
        return this.lastCheckpoint;
    }

    public void nextCheckpoint() {
        this.sectionStarts.add(System.currentTimeMillis());
        this.sectionTimes.add(this.sectionStarts.get(this.sectionStarts.size() - 1) - this.sectionStarts.get(this.sectionStarts.size() - 2));
        ++this.lastCheckpoint;
    }

    public List<Long> getSectionTimes() {
        return this.sectionTimes;
    }

    public long getFinalTime() {
        return this.finalTime;
    }

    public long getLastSectionTime() {
        return this.sectionTimes.get(this.sectionTimes.size() - 1);
    }
}
