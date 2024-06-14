package me.shivzee.callbacks;

/**
 * The WorkStatus of a Method
 */
@FunctionalInterface
public interface WorkCallback {
    void workStatus(boolean status);
}
