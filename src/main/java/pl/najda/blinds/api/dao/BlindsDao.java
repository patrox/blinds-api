package pl.najda.blinds.api.dao;

import pl.najda.blinds.api.model.Blind;

import java.util.Collection;
import java.util.Optional;

public interface BlindsDao {

    Optional<Blind> getBlind(String blindName);

    Collection<Blind> getBlinds();

    void add(Blind blind);
}
