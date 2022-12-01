package nl.enjarai.cicada.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class ProperLogger implements Logger {
    protected final String mod_id;
    protected final Logger wrappedLogger;

    protected ProperLogger(String mod_id, Logger wrappedLogger) {
        this.mod_id = mod_id;
        this.wrappedLogger = wrappedLogger;
    }

    public static ProperLogger getLogger(String mod_id) {
        return new ProperLogger(mod_id, LoggerFactory.getLogger(mod_id));
    }

    protected String modifyMessage(String message) {
        return "[" + mod_id + "] " + message;
    }

    // Proxies for all Logger methods

    @Override
    public void info(String s) {
        wrappedLogger.info(modifyMessage(s));
    }

    @Override
    public void info(String s, Object o) {
        wrappedLogger.info(modifyMessage(s), o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        wrappedLogger.info(modifyMessage(s), o, o1);
    }

    @Override
    public void info(String s, Object... args) {
        wrappedLogger.info(modifyMessage(s), args);
    }

    @Override
    public void info(String s, Throwable throwable) {
        wrappedLogger.info(modifyMessage(s), throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return wrappedLogger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String s) {
        wrappedLogger.info(marker, modifyMessage(s));
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        wrappedLogger.info(marker, modifyMessage(s), o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        wrappedLogger.info(marker, modifyMessage(s), o, o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        wrappedLogger.info(marker, modifyMessage(s), objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        wrappedLogger.info(marker, modifyMessage(s), throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return wrappedLogger.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        wrappedLogger.warn(modifyMessage(s));
    }

    @Override
    public void warn(String s, Object o) {
        wrappedLogger.warn(modifyMessage(s), o);
    }

    @Override
    public void warn(String s, Object... args) {
        wrappedLogger.warn(modifyMessage(s), args);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        wrappedLogger.warn(modifyMessage(s), o, o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        wrappedLogger.warn(modifyMessage(s), throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return wrappedLogger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String s) {
        wrappedLogger.warn(marker, modifyMessage(s));
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        wrappedLogger.warn(marker, modifyMessage(s), o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        wrappedLogger.warn(marker, modifyMessage(s), o, o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        wrappedLogger.warn(marker, modifyMessage(s), objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        wrappedLogger.warn(marker, modifyMessage(s), throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return wrappedLogger.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        wrappedLogger.error(modifyMessage(s));
    }

    @Override
    public void error(String s, Object o) {
        wrappedLogger.error(modifyMessage(s), o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        wrappedLogger.error(modifyMessage(s), o, o1);
    }

    public void error(String s, Object... args) {
        wrappedLogger.error(modifyMessage(s), args);
    }

    public void error(String s, Throwable throwable) {
        wrappedLogger.error(modifyMessage(s), throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return wrappedLogger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String s) {
        wrappedLogger.error(marker, modifyMessage(s));
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        wrappedLogger.error(marker, modifyMessage(s), o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        wrappedLogger.error(marker, modifyMessage(s), o, o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        wrappedLogger.error(marker, modifyMessage(s), objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        wrappedLogger.error(marker, modifyMessage(s), throwable);
    }

    @Override
    public String getName() {
        return wrappedLogger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return wrappedLogger.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        wrappedLogger.trace(modifyMessage(s));
    }

    @Override
    public void trace(String s, Object o) {
        wrappedLogger.trace(modifyMessage(s), o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        wrappedLogger.trace(modifyMessage(s), o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        wrappedLogger.trace(modifyMessage(s), objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        wrappedLogger.trace(modifyMessage(s), throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return wrappedLogger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String s) {
        wrappedLogger.trace(marker, modifyMessage(s));
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        wrappedLogger.trace(marker, modifyMessage(s), o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        wrappedLogger.trace(marker, modifyMessage(s), o, o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        wrappedLogger.trace(marker, modifyMessage(s), objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        wrappedLogger.trace(marker, modifyMessage(s), throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return wrappedLogger.isDebugEnabled();
    }

    public void debug(String s) {
        wrappedLogger.debug(modifyMessage(s));
    }

    @Override
    public void debug(String s, Object o) {
        wrappedLogger.debug(modifyMessage(s), o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        wrappedLogger.debug(modifyMessage(s), o, o1);
    }

    public void debug(String s, Object... args) {
        wrappedLogger.debug(modifyMessage(s), args);
    }

    public void debug(String s, Throwable throwable) {
        wrappedLogger.debug(modifyMessage(s), throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return wrappedLogger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String s) {
        wrappedLogger.debug(marker, modifyMessage(s));
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        wrappedLogger.debug(marker, modifyMessage(s), o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        wrappedLogger.debug(marker, modifyMessage(s), o, o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        wrappedLogger.debug(marker, modifyMessage(s), objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        wrappedLogger.debug(marker, modifyMessage(s), throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return wrappedLogger.isInfoEnabled();
    }
}
