package com.magician.web.mvc.execute.model;

import java.util.UUID;

/**
 * 返回流
 */
public class ResponseInputStream {

    /**
     * file name
     */
    private String name = UUID.randomUUID().toString() + ".data";

    /**
     * file byte array
     */
    private byte[] bytes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
