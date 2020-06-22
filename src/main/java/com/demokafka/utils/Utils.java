package com.demokafka.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    public static Properties readProperties(String fileName) throws IOException {
        InputStream stream = Utils.class.getResourceAsStream("/" + fileName);
        Properties props = new Properties();
        props.load(stream);
        stream.close();
        return props;
    }
}
