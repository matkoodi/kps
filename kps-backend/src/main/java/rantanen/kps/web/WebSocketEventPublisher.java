package rantanen.kps.web;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import rantanen.kps.Event;
import rantanen.kps.EventPublisher;
import rantanen.kps.domain.MatchFinishedEvent;
import rantanen.kps.domain.MatchResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WebSocketEventPublisher implements EventPublisher {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void publish(Event e) {
        if(e instanceof MatchFinishedEvent) {
            MatchFinishedEvent matchFinishedEvent = (MatchFinishedEvent) e;
            MatchResult result = matchFinishedEvent.getResult();
            ResultDTO payload = new ResultDTO(result);
            simpMessagingTemplate.convertAndSend("/topic/" + ((MatchFinishedEvent) e).getMatchId(), payload);
        }
    }

    @Data
    public static class ResultDTO {
        private final String winner;
        private final List<String> hands;

        public ResultDTO(MatchResult result) {
            if(result.getWinner().isPresent()) {
                this.winner = result.getWinner().get().getPlayerId().toString();
            } else {
                this.winner = null;
            }

            this.hands = result.getPlayerHands().get().values()
                    .stream()
                    .map(Optional::get)
                    .map(hand -> hand.getShape().toString())
                    .collect(Collectors.toList());
        }
    }
}
