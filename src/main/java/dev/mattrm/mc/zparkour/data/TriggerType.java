package dev.mattrm.mc.zparkour.data;

public enum TriggerType {
    COURSE_START,
    SECTION_END,
    COURSE_END;

    public static TriggerType from(String typeStr) {
        switch (typeStr.trim().toLowerCase()) {
            case "start":
                return TriggerType.COURSE_START;
            case "end":
                return TriggerType.COURSE_END;
            case "checkpoint":
                return TriggerType.SECTION_END;
            default:
                return null;
        }
    }
}
