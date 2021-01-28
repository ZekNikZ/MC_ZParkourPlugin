package dev.mattrm.mc.zparkour.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.mattrm.mc.zparkour.Constants;
import dev.mattrm.mc.zparkour.data.Session;
import dev.mattrm.mc.zparkour.data.Trigger;
import dev.mattrm.mc.zparkour.data.TriggerType;
import dev.mattrm.mc.zparkour.util.MathUtils;
import dev.mattrm.mc.zparkour.util.TimeUtils;
import dev.mattrm.mc.zparkour.util.TriTuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TriggerService {
    private static TriggerService instance = null;

    public static TriggerService initialize(JavaPlugin plugin) {
        return (instance = new TriggerService(plugin));
    }

    public static TriggerService getInstance() {
        if (instance == null) {
            throw new RuntimeException("Service not initialized.");
        }

        return instance;
    }

    private final JavaPlugin plugin;
    private List<Trigger> triggers;
    private final Map<UUID, Location> regionPos1;
    private final Map<UUID, Location> regionPos2;

    public TriggerService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.triggers = Lists.newArrayList();
        this.regionPos1 = Maps.newHashMap();
        this.regionPos2 = Maps.newHashMap();
    }

    public List<Trigger> getTriggers() {
        return this.triggers;
    }

    public void onPlayerInTriggerRegion(Player player, Location loc) {
        if (ParkourSessionService.getInstance().onCooldown(player.getUniqueId())) {
            return;
        }

        for (Trigger trigger : triggers) {
            if (trigger.contains(loc)) {
                ParkourSessionService.getInstance().resetCooldown(player.getUniqueId());
                switch (trigger.getType()) {
                    case COURSE_START:
                        boolean alreadyInSession = ParkourSessionService.getInstance().inSession(player.getUniqueId());

                        ParkourSessionService.getInstance().startSession(player.getUniqueId(), trigger.getCourseId());

                        if (alreadyInSession) {
                            player.sendMessage(Constants.CHAT_PREFIX + "Reset your time. Ready... set... go!");
                        } else {
                            player.sendMessage(Constants.CHAT_PREFIX + "Ready... set... go!");
                        }

                        break;
                    case SECTION_END:
                    case COURSE_END:
                        if (!ParkourSessionService.getInstance().inSession(player.getUniqueId())) {
                            player.sendMessage(Constants.CHAT_PREFIX + "This is a checkpoint for a parkour or elytra course. Go to the beginning to start a run!");
                            break;
                        }

                        ParkourSessionService.CheckpointResult result = ParkourSessionService.getInstance().triggerHit(player.getUniqueId(), trigger);

                        switch (result) {
                            case SUCCESS:
                                if (trigger.getType() == TriggerType.COURSE_END) {
                                    Session session = ParkourSessionService.getInstance().endSession(player.getUniqueId());

                                    player.sendMessage(Constants.CHAT_PREFIX + "You finished! Final time: " + TimeUtils.format(session.getFinalTime()));
                                    for (int i = 0; i < session.getSectionTimes().size(); i++) {
                                        long time = session.getSectionTimes().get(i);
                                        player.sendMessage(Constants.CHAT_PREFIX + "   Section " + (i + 1) + ": " + TimeUtils.format(time));
                                    }
                                } else {
                                    Session session = ParkourSessionService.getInstance().getSession(player.getUniqueId());

                                    player.sendMessage(Constants.CHAT_PREFIX + "You finished section " + session.getLastCheckpoint() + " in " + TimeUtils.format(session.getLastSectionTime()));
                                }
                                break;
                            case WRONG_COURSE:
                                player.sendMessage(Constants.CHAT_PREFIX + "This checkpoint is for a different course!");
                                break;
                            case WRONG_CHECKPOINT:
                                player.sendMessage(Constants.CHAT_PREFIX + "This is not the next checkpoint! Try again!");
                                break;
                            case NOT_IN_SESSION:
                            case SESSION_NOT_STARTED:
                                player.sendMessage(Constants.CHAT_PREFIX + "This is a checkpoint for a parkour or elytra course. Go to the beginning to start a run!");
                                break;
                        }

                        break;
                }
                break;
            }
        }
    }

    public void setPlayerCorner(UUID uuid, int corner, Location location) {
        if (corner == 1) {
            this.regionPos1.put(uuid, location);
        } else {
            this.regionPos2.put(uuid, location);
        }
    }

    public Trigger createTrigger(UUID uniqueId, int courseId, TriggerType type, int sectionNum) {
        if (!CourseService.getInstance().courseExists(courseId) || !this.canCreateTrigger(uniqueId)) {
            Bukkit.broadcastMessage("can't create trigger");
            return null;
        }

        Trigger trigger = new Trigger(
                this.regionPos1.get(uniqueId).getBlockX(),
                this.regionPos2.get(uniqueId).getBlockX(),
                this.regionPos1.get(uniqueId).getBlockY(),
                this.regionPos2.get(uniqueId).getBlockY(),
                this.regionPos1.get(uniqueId).getBlockZ(),
                this.regionPos2.get(uniqueId).getBlockZ(),
                type,
                courseId,
                sectionNum,
                Objects.requireNonNull(this.regionPos1.get(uniqueId).getWorld()).getEnvironment()
        );

        int maxSize = this.plugin.getConfig().getInt("max-trigger-region-width");
        TriTuple<Integer, Integer, Integer> size = trigger.getSize();
        if (size.first > maxSize || size.second > maxSize || size.third > maxSize) {
            Bukkit.broadcastMessage("too big");
            return null;
        }

        CourseService.getInstance().getCourseById(courseId).addTrigger(trigger);
        this.triggers.add(trigger);

        return trigger;
    }

    // 0 = start
    // -1 = end
    public void removeTrigger(int courseId, int sectionNum) {
        if (!CourseService.getInstance().courseExists(courseId)) {
            throw new IllegalArgumentException("Course does not exist");
        }

        if (sectionNum == -1) {
            sectionNum = CourseService.getInstance().getCourseById(courseId).getTriggers().size() - 1;
        }

        CourseService.getInstance().getCourseById(courseId).removeTrigger(sectionNum);
    }

    public void removeTriggers(int courseId) {
        if (!CourseService.getInstance().courseExists(courseId)) {
            throw new IllegalArgumentException("Course does not exist");
        }

        this.triggers.removeIf(t -> t.getCourseId() == courseId);
    }

    public boolean canCreateTrigger(UUID uniqueId) {
        return this.regionPos1.containsKey(uniqueId) && this.regionPos2.containsKey(uniqueId) && Objects.requireNonNull(this.regionPos1.get(uniqueId).getWorld()).getEnvironment() == Objects.requireNonNull(this.regionPos2.get(uniqueId).getWorld()).getEnvironment();
    }

    public boolean deleteIfExists(int courseId, int triggerId) {
        if (MathUtils.between(triggerId, -1, CourseService.getInstance().getCourseById(courseId).getTriggers().size() - 1)) {
            CourseService.getInstance().getCourseById(courseId).removeTrigger(triggerId);
            return true;
        }

        return false;
    }
}
