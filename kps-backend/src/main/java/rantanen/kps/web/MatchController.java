package rantanen.kps.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import rantanen.kps.domain.Hand;
import rantanen.kps.domain.MatchService;
import rantanen.kps.domain.Player;

import java.util.Optional;

@RestController
public class MatchController {
    @Autowired
    private MatchService matchService;

    private static final String MATCH_ID_PATTERN = "^[a-zA-Z0-9]*$";

    @PostMapping("/match/{matchId}")
    public ResponseEntity<String> join(@PathVariable String matchId) {
        if(StringUtils.isEmpty(matchId) || !matchId.matches(MATCH_ID_PATTERN)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Player> player = matchService.joinMatch(matchId);

        if(player.isPresent()) {
            return ResponseEntity.ok(player.get().getPlayerId().toString());
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/match/{matchId}/{playerId}/{shape}")
    public ResponseEntity<Void> playHand(@PathVariable String matchId, @PathVariable String playerId, @PathVariable Hand.Shape shape) {
        boolean success = matchService.playHand(matchId, playerId, shape);

        if(success) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
