package com.cremonezzi.impl.carlsenbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CarlsenTest {
    private Carlsen carlsenBot;

    @BeforeEach
    public void config() {
        carlsenBot = new Carlsen();
    }

    @Test
    @DisplayName("Should raise if have zap and and winning")
    public void ShouldRaiseIfZapAndWinning() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of(
               GameIntel.RoundResult.WON
        );
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should choose lowest card in hand if is first playing")
    public void ShouldChooseLowestInHandIfFirstPlaying() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

        // Game info
        List<GameIntel.RoundResult> roundResults = Collections.emptyList();
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isEqualTo("[7D]");
    }

    @Test
    @DisplayName("Should choose higher card in hand if is losing")
    public void ShouldChooseHigherInHandIfLosing() {
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isEqualTo("[AS]");
    }

    @Test
    @DisplayName("Should discard when opponent zap")
    public void ShouldDiscardWhenOpponentZap() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON
        );
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(2)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS))
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isEqualTo("[XX]");
    }

    @Test
    @DisplayName("Should not accept raise if dont have manilha")
    public void ShoulNotAcceptIfNoManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of();
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(2)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS))
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should accept raise have manilha")
    public void ShoulAcceptIfManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of();
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(2)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS))
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(0);
    }

}