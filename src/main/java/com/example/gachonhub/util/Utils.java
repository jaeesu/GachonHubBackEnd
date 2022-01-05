package com.example.gachonhub.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface Utils {

    public final String AuthProvider = "github";

    @Getter
    @AllArgsConstructor
    public enum TokenType {
        X_AUTH_TOKEN("X-AUTH-TOKEN"),
        REFRESH_TOKEN("REFRESH-TOKEN");

        private String value;
    }
}
