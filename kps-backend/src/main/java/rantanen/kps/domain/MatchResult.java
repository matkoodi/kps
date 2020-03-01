package rantanen.kps.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class MatchResult {
    private final boolean finished;
    private final Optional<Player> winner;
    private final Optional<Map<Player,Optional<Hand>>> playerHands;

    public boolean isDraw() {
        return finished && !winner.isPresent();
    }
}
