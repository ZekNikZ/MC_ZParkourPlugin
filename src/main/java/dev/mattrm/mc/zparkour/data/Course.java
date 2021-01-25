package dev.mattrm.mc.zparkour.data;

import com.google.common.collect.Lists;
import dev.mattrm.mc.zparkour.util.MathUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class Course {
    private final int id;
    private final UUID owner;
    private String name;
    private final List<Trigger> triggers;

    public Course(int id, UUID owner, String name) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.triggers = Lists.newArrayList();
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

    public void addTrigger(Trigger trigger) {
        if (trigger.getType() == TriggerType.COURSE_START) {
            if (triggers.get(0).getType() == TriggerType.COURSE_START) {
                throw new IllegalArgumentException("Course already has a start trigger");
            }
            triggers.add(0, trigger);
        } else if (trigger.getType() == TriggerType.COURSE_END) {
            if (triggers.get(triggers.size() - 1).getType() == TriggerType.COURSE_END) {
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
                IntStream.range(0, this.triggers.size()).forEach(i -> this.triggers.get(i).setSectionId(i));
            }
        }
        throw new IllegalArgumentException("Invalid trigger type");
    }

    public void removeTrigger(int sectionNum) {
        this.triggers.remove(MathUtils.clamp(sectionNum, 0, this.triggers.size() - 1));
        IntStream.range(0, this.triggers.size()).forEach(i -> this.triggers.get(i).setSectionId(i));
    }
}
