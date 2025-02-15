package de.codelix.emsbridge.storage;

/**
 * A consumer that can throw exceptions
 * @param <T> The type to consume
 */
@FunctionalInterface
public interface ExceptionConsumer<T> {
    /**
     * Gets called when the object that can be consumed is ready
     * @param t The object
     * @throws Exception Any exception can be thrown
     */
    void accept(T t) throws Exception;
}
