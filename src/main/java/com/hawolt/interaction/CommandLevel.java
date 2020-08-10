package com.hawolt.interaction;

public enum CommandLevel {
    DEFAULT,
    SUPPORT,
    MODERATOR,
    ADMIN,
    OWNER,
    DEVELOPER;
    private static CommandLevel[] LEVELS = CommandLevel.values();

    public static CommandLevel findByName(String name) {
        name = name.toUpperCase();
        for (CommandLevel level : LEVELS) {
            if (level.name().equals(name)) {
                return level;
            }
        }
        return null;
    }
}
