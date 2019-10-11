package org.udg.caes.webclient;

import java.io.InputStream;

public class WebClient {
    public String getContent(ConnectionFactory connectionFactory) {
        StringBuffer content = new StringBuffer();
        try {
            InputStream is = connectionFactory.getData();
            int count;
            while (-1 != (count = is.read())) {
                content.append(new String(Character.toChars(count)));
            }
            is.close();
        } catch (Exception e) {
            return null;
        }
        return content.toString();
    }
}

