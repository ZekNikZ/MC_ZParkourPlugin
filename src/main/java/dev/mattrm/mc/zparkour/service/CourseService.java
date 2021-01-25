package dev.mattrm.mc.zparkour.service;

import com.google.common.collect.Lists;
import dev.mattrm.mc.zparkour.data.Course;
import dev.mattrm.mc.zparkour.data.Trigger;
import dev.mattrm.mc.zparkour.util.UUIDUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class CourseService {
    private static CourseService instance = null;

    public static CourseService initialize(JavaPlugin plugin) {
        return (instance = new CourseService(plugin));
    }

    public static CourseService getInstance() {
        if (instance == null) {
            throw new RuntimeException("Service not initialized.");
        }

        return instance;
    }

    private final JavaPlugin plugin;
    private List<Course> courses;

    public CourseService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.courses = Lists.newArrayList();
    }

    public int createCourse(String name, UUID owner) {
        int id = this.courses.stream().mapToInt(Course::getId).max().orElse(-1) + 1;

        this.courses.add(new Course(id, owner, name));

        return id;
    }

    public void removeCourse(int courseId) {
        if (!courseExists(courseId)) {
            throw new IllegalArgumentException("Course does not exist");
        }

        TriggerService.getInstance().removeTriggers(courseId);
        this.courses.removeIf(c -> c.getId() == courseId);
    }

    public List<Course> getCourses() {
        return this.courses;
    }

    public boolean courseExists(int courseId) {
        return this.courses.stream().anyMatch(c -> c.getId() == courseId);
    }

    public boolean canModify(int courseId, UUID owner, boolean isOp) {
        return this.getCourseById(courseId).getOwner().equals(owner) || isOp;
    }

    public Course getCourseById(int courseId) {
        return this.courses.stream().filter(c -> c.getId() == courseId).findFirst().orElseThrow(() -> new IllegalArgumentException("Course does not exist"));
    }
}
