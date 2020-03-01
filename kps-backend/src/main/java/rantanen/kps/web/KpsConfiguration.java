package rantanen.kps.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rantanen.kps.domain.MatchService;

@Configuration
public class KpsConfiguration {
    @Autowired
    private WebSocketEventPublisher webSocketEventPublisher;

    @Bean
    public MatchService matchService() {
        return new MatchService(webSocketEventPublisher);
    }
}
