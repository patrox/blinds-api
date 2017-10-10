package pl.najda.blinds.api;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBlindsDao implements BlindsDao {

    private final Map<String, Blind> blinds = new ConcurrentHashMap<>();

    @Override
    public Optional<Blind> getBlind(String blindName) {
        return Optional.ofNullable(blinds.get(blindName));
    }

    @Override
    public Collection<Blind> getBlinds() {
        return blinds.values();
    }

    @Override
    public void add(Blind blind) {
        blinds.put(blind.getName(), blind);
    }
}
