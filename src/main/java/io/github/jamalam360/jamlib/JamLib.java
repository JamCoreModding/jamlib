package io.github.jamalam360.jamlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JamLib {
    public static Logger getLogger(String subName) {
        return LogManager.getLogger("JamLib/" + subName);
    }
}
