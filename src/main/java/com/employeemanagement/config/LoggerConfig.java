package com.employeemanagement.config;

import java.io.IOException;
import java.util.logging.*;

public class LoggerConfig {
    private static final String LOG_FILE_PATH = "C:/Users/sivan-pt7887/Desktop/apache-tomcat-9.0.102-windows-x64/apache-tomcat-9.0.102/logs/employeeMgmt.log";
    private static final Logger rootLogger = Logger.getLogger("");
    static {
        rootLogger.setUseParentHandlers(false);
        for (Handler handler : rootLogger.getHandlers()) {
            String handlerClassName = handler.getClass().getName();
            // Keep Tomcat’s handlers (ConsoleHandler, AsyncFileHandler, FileHandler)
            if (!handlerClassName.contains("ConsoleHandler") &&
                    !handlerClassName.contains("FileHandler") &&
                    !handlerClassName.contains("AsyncFileHandler")) {
                rootLogger.removeHandler(handler);
            }
        }
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE_PATH, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            rootLogger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to configure logger: " + e);
        }
    }

    public static Logger getLogger() {
        return rootLogger;
    }
}