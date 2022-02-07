package com.example.gachonhub.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface Utils {

    public final String AuthProvider = "github";

    @Getter
    @AllArgsConstructor
    public enum TokenType {
        AUTHORIZATION("AUTHORIZATION"),
        REFRESH("REFRESH");

        private String value;
    }
}
