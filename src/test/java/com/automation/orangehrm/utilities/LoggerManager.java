package com.automation.orangehrm.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Single place that creates log4j2 Logger instances, so every class (BaseTest,
// ActionDriver, ...) gets its logger the same way instead of calling LogManager directly.
public class LoggerManager {
    // clazz is used as the logger's name, so log lines show which class logged them.
    public static Logger getLogger(Class<?> clazz) {
     return LogManager.getLogger(clazz);
    }
}
