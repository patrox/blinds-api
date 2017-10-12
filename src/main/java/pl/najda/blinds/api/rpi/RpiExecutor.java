package pl.najda.blinds.api.rpi;

import pl.najda.blinds.api.model.BlindCommand;

import java.util.Collection;
import java.util.stream.Collectors;

public class RpiExecutor {

    public Collection<String> execute(Collection<Integer> channels, BlindCommand command) {

        return channels.stream().map(channel -> String.format("Executed %s on %d channel", command, channel))
                .collect(Collectors.toList());

    }
}
