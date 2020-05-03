package com.alibaba.datax.plugin.writer.hugegraphwriter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HugegraphLogger {
    public static Logger get(Class<?> cl){
        return LoggerFactory.getLogger(cl);
    }
}
