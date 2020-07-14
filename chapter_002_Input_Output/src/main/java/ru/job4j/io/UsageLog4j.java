package ru.job4j.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsageLog4j {

    private static final Logger LOG = LoggerFactory.getLogger(
            UsageLog4j.class.getName());

    public static void main(String[] args) {
        byte paramByte = 1;
        short paramShort = 2;
        char paramChar = 3;
        int paramInt = 4;
        long paramLong = 5;
        float paramFloat = 6;
        double paramDouble = 7;
        boolean paramBoolean = false;
        LOG.debug("paramByte : {}, paramShort : {}, paramChar : {}, paramInt "
                          + ": {}, " + "paramLong : {}, paramFloat : {}, "
                          + "paramDouble : {}, paramBoolean : {} ", paramByte,
                  paramShort, paramChar, paramInt, paramLong, paramFloat,
                  paramDouble, paramBoolean);

        LOG.error("beda", new Exception());
    }
}