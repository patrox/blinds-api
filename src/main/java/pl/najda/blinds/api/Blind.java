package pl.najda.blinds.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.util.Collection;

@AutoValue
public abstract class Blind {
    @JsonCreator
    public static Blind create(@JsonProperty("name") String name,
                               @JsonProperty("description") String description,
                               @JsonProperty("channels") Collection<Integer> channels) {
        return new AutoValue_Blind(name, description, channels);
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract Collection<Integer> getChannels();
}
