package com.nlp.aclpkotlin;

public class KVObjectJava {
    public String value, type;
    public int key;

    public KVObjectJava(String value, String type, int key){
        this.value = value;
        this.type = type;
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
