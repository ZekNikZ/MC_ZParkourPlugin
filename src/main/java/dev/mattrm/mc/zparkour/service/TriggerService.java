package dev.mattrm.mc.zparkour.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.mattrm.mc.zparkour.data.Trigger;
import dev.mattrm.mc.zparkour.data.TriggerType;
import dev.mattrm.mc.zparkour.util.MathUtils;
import dev.mattrm.mc.zparkour.util.TriTuple;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
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
    private Map<UUID, Location> regionPos1;
    private Map<UUID, Location> regionPos2;

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
        throw new RuntimeException("Not implemented");
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
            return null;
        }

        Trigger trigger = new Trigger(
            this.regionPos1.get(uniqueId).getBlockX(),
            this.regionPos1.get(uniqueId).getBlockY(),
            this.regionPos1.get(uniqueId).getBlockZ(),
            this.regionPos2.get(uniqueId).getBlockX(),
            this.regionPos2.get(uniqueId).getBlockY(),
            this.regionPos2.get(uniqueId).getBlockZ(),
            type,
            courseId,
            sectionNum
        );

        int maxSize = this.plugin.getConfig().getInt("max-trigger-region-width");
        TriTuple<Integer, Integer, Integer> size = trigger.getSize();
        if (size.first > maxSize || size.second > maxSize || size.third > maxSize) {
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
        return this.regionPos1.containsKey(uniqueId) && this.regionPos2.containsKey(uniqueId);
    }

    public boolean deleteIfExists(int courseId, int triggerId) {
        if (MathUtils.between(triggerId, -1, CourseService.getInstance().getCourseById(courseId).getTriggers().size() - 1)) {
            CourseService.getInstance().getCourseById(courseId).removeTrigger(triggerId);
            return true;
        }

        return false;
    }
}
