package com.soulvirart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void info(String message) {
        logger.info(message);
    }

    public static void error(String message, Throwable t) {
        logger.error(message, t);
    }
}