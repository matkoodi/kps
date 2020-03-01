package rantanen.kps.domain;

import rantanen.kps.EventPublisher;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MatchService {
    private ConcurrentHashMap<String, Match> matches = new ConcurrentHashMap<>();
    private final EventPublisher eventPublisher;

    public MatchService(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public Optional<Player> joinMatch(String matchId) {
        Match match = matches.computeIfAbsent(matchId, matchIdArg -> new Match(matchIdArg, eventPublisher));
        Player player = Player.newPlayer();

        return match.join(player) ? Optional.of(player) : Optional.empty();
    }

    public boolean playHand(String matchId, String playerId, Hand.Shape shape) {
        Match match = matches.get(matchId);

        if(match == null) {
            return false;
        }

        return match.play(new Player(playerId), new Hand(shape));
    }
}
