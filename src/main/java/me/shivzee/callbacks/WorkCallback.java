package me.shivzee.callbacks;

/**
 * Functional interface for handling the status of a work operation.
 */
@FunctionalInterface
public interface WorkCallback {

    /**
     * Called to indicate the status of a work operation.
     *
     * @param status {@code true} if the work operation was successful, {@code false} otherwise
     */
    void workStatus(boolean status);
}
