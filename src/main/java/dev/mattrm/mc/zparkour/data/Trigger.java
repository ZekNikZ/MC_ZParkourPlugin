package dev.mattrm.mc.zparkour.data;

import dev.mattrm.mc.zparkour.util.TriTuple;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

import static dev.mattrm.mc.zparkour.util.MathUtils.between;

public class Trigger {
    private int x1, x2, y1, y2, z1, z2;
    private TriggerType type;
    private int courseId, sectionId;
    private final World.Environment dimension;

    public Trigger(int x1, int x2, int y1, int y2, int z1, int z2, TriggerType type, int courseId, int sectionId, World.Environment environment) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
        this.type = type;
        this.courseId = courseId;
        this.sectionId = sectionId;
        this.dimension = environment;
    }

    public World.Environment getDimension() {
        return this.dimension;
    }

    public TriggerType getType() {
        return this.type;
    }

    public void setType(TriggerType type) {
        this.type = type;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getSectionId() {
        return this.sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getX1() {
        return this.x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return this.x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return this.y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return this.y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getZ1() {
        return this.z1;
    }

    public void setZ1(int z1) {
        this.z1 = z1;
    }

    public int getZ2() {
        return this.z2;
    }

    public void setZ2(int z2) {
        this.z2 = z2;
    }

    public boolean contains(Location location) {
        return contains(location.getBlockX(), location.getBlockY(), location.getBlockZ(), Objects.requireNonNull(location.getWorld()).getEnvironment());
    }

    public boolean contains(int x, int y, int z, World.Environment dimension) {
        return between(x, x1, x2) && between(y, y1, y2) && between(z, z1, z2) && this.dimension == dimension;
    }

    @Override
    public String toString() {
        return "TriggerRegion{" +
                "x1=" + x1 +
                ", x2=" + x2 +
                ", y1=" + y1 +
                ", y2=" + y2 +
                ", z1=" + z1 +
                ", z2=" + z2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trigger)) return false;
        Trigger that = (Trigger) o;
        return x1 == that.x1 &&
                x2 == that.x2 &&
                y1 == that.y1 &&
                y2 == that.y2 &&
                z1 == that.z1 &&
                z2 == that.z2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, x2, y1, y2, z1, z2);
    }

    public TriTuple<Integer, Integer, Integer> getSize() {
        return new TriTuple<>(Math.abs(x1 - x2), Math.abs(y1 - y2), Math.abs(z1 - z2));
    }
}
