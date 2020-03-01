package rantanen.kps.domain;

import lombok.EqualsAndHashCode;
import rantanen.kps.EventPublisher;

import java.util.*;

@EqualsAndHashCode(of="matchId")
class Match {
    private final String matchId;
    private final Map<Player,Optional<Hand>> playerHands = new LinkedHashMap<>();
    private final int numberOfPlayers = 2;
    private int version = 0;
    private final EventPublisher eventPublisher;

    Match(String matchId, EventPublisher eventPublisher) {
        this.matchId = matchId;
        this.eventPublisher = eventPublisher;
    }

    synchronized boolean join(Player player) {
        if(playerHands.containsValue(player)) {
            return false;
        }
        if(playerHands.size() >= numberOfPlayers) {
            return false;
        }
        else {
            this.playerHands.put(player, Optional.empty());
            version++;

            return true;
        }
    }

    synchronized boolean play(Player player, Hand hand) {
        if(playerExistsAndHandNotPlayed(player)) {
            playerHands.put(player, Optional.of(hand));
            version++;

            MatchResult result = getResult();

            if(result.isFinished()) {
                publishResult(result);
            }
            return true;
        }
        else {
            return false;
        }
    }

    synchronized MatchResult getResult() {
        if(!allHandsPresent()) {
            return new MatchResult(false, Optional.empty(), Optional.empty());
        }

        Optional<Player> winner = playerHands.keySet().stream()
                .filter(this::hasPlayerWon)
                .findFirst();

        return new MatchResult(true, winner, Optional.of(playerHands));
    }

    private boolean playerExistsAndHandNotPlayed(Player player) {
        return playerHands.containsKey(player) &&
                !playerHands.get(player).isPresent();
    }


    private boolean hasPlayerWon(Player player) {
        Optional<Hand> hand = playerHands.get(player);

        if(hand == null || !hand.isPresent()) {
            return false;
        }

        return playerHands.entrySet()
                .stream()
                .filter(entry -> !entry.getKey().equals(player))
                .allMatch(entry -> entry.getValue().isPresent() &&
                        hand.get().beats(entry.getValue().get()));
    }

    private boolean allHandsPresent() {
        return playerHands.size() == numberOfPlayers &&
                playerHands.values().stream().allMatch(Optional::isPresent);
    }

    private void publishResult(MatchResult result) {
        eventPublisher.publish(new MatchFinishedEvent(matchId, result));
    }
}
