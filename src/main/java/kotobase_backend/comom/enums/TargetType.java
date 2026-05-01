package kotobase_backend.comom.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TargetType {
    vocab,
    kanji,
    grammar;

    @JsonCreator
    public static TargetType from(String value) {
        if (value == null) return null;
        return TargetType.valueOf(value.toUpperCase());
    }
}
