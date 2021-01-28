package dev.mattrm.mc.zparkour.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.mattrm.mc.zparkour.data.Session;
import dev.mattrm.mc.zparkour.data.Trigger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public class ParkourSessionService {
    private static ParkourSessionService instance = null;

    public static ParkourSessionService initialize(JavaPlugin plugin) {
        return (instance = new ParkourSessionService(plugin));
    }

    public static ParkourSessionService getInstance() {
        if (instance == null) {
            throw new RuntimeException("Service not initialized.");
        }

        return instance;
    }

    private final JavaPlugin plugin;
    private final Map<UUID, Session> sessions;
    private final Map<UUID, Long> lastCheckedTimes;

    public ParkourSessionService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.sessions = Maps.newHashMap();
        this.lastCheckedTimes = Maps.newHashMap();
    }

    public boolean hasSession(UUID uuid) {
        return this.sessions.containsKey(uuid);
    }

    public Session getSession(UUID uuid) {
        return this.sessions.get(uuid);
    }

    public void startSession(UUID uuid, int courseId) {
        Session session = new Session(uuid, courseId);

        session.start();

        this.sessions.put(uuid, session);
    }

    public boolean inSession(UUID uniqueId) {
        return this.hasSession(uniqueId) && this.getSession(uniqueId).hasStarted();
    }

    public Session endSession(UUID uniqueId) {
        Session session = this.getSession(uniqueId);
        session.end();

        this.sessions.remove(uniqueId);

        return session;
    }

    public boolean onCooldown(UUID uniqueId) {
        return System.currentTimeMillis() - this.lastCheckedTimes.getOrDefault(uniqueId, 0L) < 2000;
    }

    public void resetCooldown(UUID uniqueId) {
        this.lastCheckedTimes.put(uniqueId, System.currentTimeMillis());
    }

    public enum CheckpointResult {
        SUCCESS(true),
        WRONG_COURSE(false),
        WRONG_CHECKPOINT(false),
        NOT_IN_SESSION(false),
        SESSION_NOT_STARTED(false);

        private final boolean success;

        CheckpointResult(boolean success) {
            this.success = success;
        }

        public boolean test() {
            return this.success;
        }
    }

    // TRUE: correct checkpoint
    // FALSE: incorrect checkpoint
    public CheckpointResult triggerHit(UUID uuid, Trigger trigger) {
        if (!this.hasSession(uuid)) {
            return CheckpointResult.NOT_IN_SESSION;
        }

        Session session = this.getSession(uuid);

        if (!session.hasStarted()) {
            return CheckpointResult.SESSION_NOT_STARTED;
        } else if (trigger.getCourseId() != session.getCourseId()) {
            return CheckpointResult.WRONG_COURSE;
        } else if (trigger.getSectionId() != session.getLastCheckpoint() + 1) {
            return CheckpointResult.WRONG_CHECKPOINT;
        }

        session.nextCheckpoint();

        return CheckpointResult.SUCCESS;
    }
}
