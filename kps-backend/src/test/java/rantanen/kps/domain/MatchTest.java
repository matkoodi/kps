package rantanen.kps.domain;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import rantanen.kps.EventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class MatchTest {
    private Player player1 = Player.newPlayer();
    private Player player2 = Player.newPlayer();

    private Hand rock = new Hand(Hand.Shape.ROCK);
    private Hand paper = new Hand(Hand.Shape.PAPER);
    private Hand scissors = new Hand(Hand.Shape.SCISSORS);

    @Test
    public void testWinner() {
        verifyMatch(paper, rock, Optional.of(player1));
        verifyMatch(paper, scissors, Optional.of(player2));
        verifyMatch(rock, scissors, Optional.of(player1));
    }

    @Test
    public void testDraw() {
        verifyMatch(paper, paper, Optional.empty());
        verifyMatch(scissors, scissors, Optional.empty());
        verifyMatch(rock, rock, Optional.empty());
    }


    private void verifyMatch(Hand player1Hand, Hand player2Hand, Optional<Player> winner) {
        // given
        EventPublisher mockEventPublisher = mock(EventPublisher.class);
        Match match = new Match(UUID.randomUUID().toString(), mockEventPublisher);

        // when
        match.join(player1);
        match.join(player2);

        // then
        assertThat(match.getResult().isFinished(), is(false));
        assertThat(match.getResult().getPlayerHands().isPresent(), is(false));
        assertThat(match.getResult().getWinner().isPresent(), is(false));

        // when
        match.play(player1, player1Hand);

        // then
        assertThat(match.getResult().isFinished(), is(false));
        assertThat(match.getResult().getPlayerHands().isPresent(), is(false));
        assertThat(match.getResult().getWinner().isPresent(), is(false));

        // when
        match.play(player2, player2Hand);

        // then
        MatchResult finalStatus = match.getResult();
        assertThat(finalStatus.isFinished(), is(true));
        assertThat(finalStatus.getPlayerHands().isPresent(), is(true));
        assertThat(finalStatus.getWinner(), is(winner));
        ArgumentCaptor<MatchFinishedEvent> event = ArgumentCaptor.forClass(MatchFinishedEvent.class);
        Mockito.verify(mockEventPublisher, times(1)).publish(event.capture());
        assertThat(event.getValue().getResult(), is(finalStatus));
    }

}