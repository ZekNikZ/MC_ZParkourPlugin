package dev.mattrm.mc.zparkour.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.mattrm.mc.zparkour.util.MathUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class Course {
    private final int id;
    private final UUID owner;
    private final List<Trigger> triggers;
    private final Map<UUID, Long> leaderboard;
    private String name;
    private boolean published;

    public Course(int id, UUID owner, String name) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.published = false;
        this.triggers = Lists.newArrayList();
        this.leaderboard = Maps.newHashMap();
    }

    public int getId() {
        return this.id;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public List<Trigger> getTriggers() {
        return this.triggers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        if (published) {
            this.leaderboard.clear();
        }

        this.published = published;
    }

    public void addTrigger(Trigger trigger) {
        if (trigger.getType() == TriggerType.COURSE_START) {
            if (triggers.size() > 0 && triggers.get(0).getType() == TriggerType.COURSE_START) {
                throw new IllegalArgumentException("Course already has a start trigger");
            }
            triggers.add(0, trigger);
        } else if (trigger.getType() == TriggerType.COURSE_END) {
            if (triggers.size() > 0 && triggers.get(triggers.size() - 1).getType() == TriggerType.COURSE_END) {
                throw new IllegalArgumentException("Course already has an end trigger");
            }
            triggers.add(trigger);
        } else if (trigger.getType() == TriggerType.SECTION_END) {
            if (this.triggers.size() == 0) {
                this.triggers.add(trigger);
            } else {
                int pos = MathUtils.clamp(
                        trigger.getSectionId(),
                        this.triggers.get(0).getType() == TriggerType.COURSE_START ? 1 : 0,
                        this.triggers.get(this.triggers.size() - 1).getType() == TriggerType.COURSE_END ? this.triggers.size() - 1 : this.triggers.size()
                );
                this.triggers.add(pos, trigger);
            }
        } else {
            throw new IllegalArgumentException("Invalid trigger type");
        }
        IntStream.range(0, this.triggers.size()).forEach(i -> this.triggers.get(i).setSectionId(i));
    }

    public void removeTrigger(int sectionNum) {
        this.triggers.remove(MathUtils.clamp(sectionNum, 0, this.triggers.size() - 1));
        IntStream.range(0, this.triggers.size()).forEach(i -> this.triggers.get(i).setSectionId(i));
    }

    public boolean isValidCourse() {
        return this.triggers.size() >= 2 && this.triggers.get(0).getType() == TriggerType.COURSE_START && this.triggers.get(this.triggers.size() - 1).getType() == TriggerType.COURSE_END;
    }
}
