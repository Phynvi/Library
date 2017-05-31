package entity.geometry.area;

/**
 * Represents a type of state when an {@code Entity} changes location within an
 * {@code Area}.
 * 
 * @author Albert Beaupre
 */
public enum AreaChangeType {

    /**
     * This {@code AreaChangeType} is used when an {@code Entity} has been
     * teleported in/out of an {@code Area}.
     */
    TELEPORT,

    /**
     * This {@code AreaChangeType} is used when an {@code Entity} has walked
     * in/out of an {@code Area}.
     */
    WALK,

    /**
     * This {@code AreaChangeType} is used when an {@code Entity} has been moved
     * in/out of an {@code Area} by the server.
     */
    SERVER

}
