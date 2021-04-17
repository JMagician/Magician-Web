package com.magician.web.execute.model;

import java.io.InputStream;
import java.util.UUID;

/**
 * 返回流
 */
public class ResponseInputStream {

    /**
     * 名称
     */
    private String name = UUID.randomUUID().toString() + ".data";

    /**
     * 流
     */
    private InputStream inputStream;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
