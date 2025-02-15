package de.codelix.emsbridge.command;

@FunctionalInterface
public interface ExceptionRunnable {
    void run() throws Exception;
}
