package rantanen.kps.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(of = "playerId")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Player {
    private final UUID playerId;

    static Player newPlayer() {
        return new Player(UUID.randomUUID());
    }

    Player(String playerId) {
        this.playerId = UUID.fromString(playerId);
    }
}
