package kr.ypshop.softend.file.config;

import lombok.Getter;

@Getter
public enum MessageType {
    INFO("info"), ERROR("error"), ETC("etc");

    private final String key;
    MessageType(String key) {
        this.key = key;
    }
}
