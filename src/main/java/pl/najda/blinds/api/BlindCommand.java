package pl.najda.blinds.api;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Optional;

public enum BlindCommand {
    OPEN,
    CLOSE,
    STOP;

    @JsonCreator
    public static BlindCommand fromString(final String string) {
        return BlindCommand.valueOf(Optional.ofNullable(string).map(String::toUpperCase).orElse(null));

    }
}
