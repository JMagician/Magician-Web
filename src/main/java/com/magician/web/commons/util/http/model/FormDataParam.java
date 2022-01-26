package com.magician.web.commons.util.http.model;

public class FormDataParam {

    private boolean isFile;

    private String name;

    private Object value;

    private byte[] file;

    private String fileName;

    public static FormDataParam create(){
        return new FormDataParam();
    }

    public boolean isFile() {
        return isFile;
    }

    public FormDataParam setFile(boolean file) {
        isFile = file;
        return this;
    }

    public String getName() {
        return name;
    }

    public FormDataParam setName(String name) {
        this.name = name;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public FormDataParam setValue(Object value) {
        this.value = value;
        return this;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
