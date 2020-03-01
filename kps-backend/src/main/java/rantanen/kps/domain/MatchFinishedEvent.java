package rantanen.kps.domain;

import lombok.Data;
import rantanen.kps.Event;

@Data
public final class MatchFinishedEvent extends Event {
    private final String matchId;
    private final MatchResult result;
}
