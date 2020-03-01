package rantanen.kps.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import rantanen.kps.Event;
import rantanen.kps.EventPublisher;
import rantanen.kps.domain.MatchFinishedEvent;

@Service
public class WebSocketEventPublisher implements EventPublisher {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void publish(Event e) {
        if(e instanceof MatchFinishedEvent) {
            simpMessagingTemplate.convertAndSend("/topic/" + ((MatchFinishedEvent) e).getMatchId(), e);
        }
    }
}
