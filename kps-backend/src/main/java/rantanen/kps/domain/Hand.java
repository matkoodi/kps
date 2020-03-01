package rantanen.kps.domain;

import lombok.Data;

import static rantanen.kps.domain.Hand.Shape.*;

@Data
public final class Hand {
    private final Shape shape;

    public enum Shape {
        ROCK, PAPER, SCISSORS;
    }

    public boolean beats(Hand otherHand) {
        Shape otherShape = otherHand.getShape();

        switch (this.shape) {
            case ROCK:
                return otherShape == SCISSORS;
            case PAPER:
                return otherShape == ROCK;
            case SCISSORS:
                return otherShape == PAPER;
            default:
                throw new IllegalArgumentException("Should not get here");
        }
    }
}
