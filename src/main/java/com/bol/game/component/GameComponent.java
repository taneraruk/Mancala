package com.bol.game.component;

import com.bol.game.enumeration.GameState;
import com.bol.game.pojo.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * GameComponent class performs these actions;
 * Start Game, Sow Stone, Manage State, Find Turn, End Game, Find Winner and Calculate Scores
 *
 * @author Taner Aruk
 */
@Slf4j
@Component
public class GameComponent {

    public static final int FIRST_PLAYER          = 0;
    public static final int SECOND_PLAYER         = 1;
    public static final int MATCH_DRAW            = -1;
    public static final int FIRST_PLAYER_BIG_PIT  = 6;
    public static final int SECOND_PLAYER_BIG_PIT = 13;

    private Board board;

    public Board getBoard() {

        return this.board;
    }

    public void start() {

        log.info("Game started");

        board = new Board();
        board.setState(GameState.STARTED);
        board.setPits(new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0});
        board.setTurn(FIRST_PLAYER);
        board.setWinner(MATCH_DRAW);
    }

    public void select(final int pitId) {

        if (pitId < 0 || pitId > 5) { // wrong selection
            return;
        }

        board.setState(GameState.PLAYING);

        final int[] pits         = board.getPits();
        final int   currentTurn  = board.getTurn();
        final int   turnShifting = currentTurn * 7;

        int lastPitId  = pitId + turnShifting;
        int stoneCount = pits[lastPitId];

        pits[lastPitId] = 0; // make selected pit empty

        while (stoneCount-- != 0) {

            // skip opponent's big pit
            if (lastPitId % pits.length == 5 + (currentTurn == FIRST_PLAYER ? 7 : 0)) {
                lastPitId++;
                log.info("Opponent's big pit skipped");
            }

            pits[++lastPitId % pits.length]++;
        }

        final int normalizedLastPitId = lastPitId % pits.length;

        // Capturing stones
        if (pits[normalizedLastPitId] == 1) {
            int shiftedPitId = (normalizedLastPitId + turnShifting) % pits.length;

            if (0 < shiftedPitId && shiftedPitId < 6) {
                int opponentsPidId  = pits.length - normalizedLastPitId - 2;
                int opponentsStones = pits[opponentsPidId];

                pits[opponentsPidId] = 0;
                pits[normalizedLastPitId] += opponentsStones;
            }
        }

        if (isGameOver()) {
            log.info("Game over, calculating scores...");

            board.setState(GameState.OVER);
            findWinner();
            return;
        }

        determineWhoseTurn(normalizedLastPitId);
    }

    private void determineWhoseTurn(final int pitOfLastStone) {

        final int currentTurn  = board.getTurn();
        final int turnShifting = currentTurn * 7;

        // If last distributed stone ended in current users big pit. Then current player plays one more time
        if (pitOfLastStone == 6 + turnShifting) {
            return; 
        }

        board.setTurn(currentTurn == FIRST_PLAYER ? SECOND_PLAYER : FIRST_PLAYER);
    }

    private void findWinner() {

        final int[] pits = board.getPits();

        for (int i = 0; i < 6; i++) {
            pits[FIRST_PLAYER_BIG_PIT] += pits[i];
            pits[i] = 0;

            pits[SECOND_PLAYER_BIG_PIT] += pits[i + 7];
            pits[i + 7] = 0;
        }

        if (pits[FIRST_PLAYER_BIG_PIT] > pits[SECOND_PLAYER_BIG_PIT]) {
            board.setWinner(FIRST_PLAYER);
            log.info("First user won!");
        } else if (pits[SECOND_PLAYER_BIG_PIT] > pits[FIRST_PLAYER_BIG_PIT]) {
            board.setWinner(SECOND_PLAYER);
            log.info("Second user won!");
        } else {
            board.setWinner(MATCH_DRAW);
            log.info("Draw state, try again!");
        }
    }

    private boolean isGameOver() {

        final int[] pits                = board.getPits();
        boolean     firstUserCompleted  = true;
        boolean     secondUserCompleted = true;

        for (int i = 0; i < 6; i++) {
            firstUserCompleted &= pits[i] == 0;
            secondUserCompleted &= pits[i + 7] == 0;
        }

        return firstUserCompleted || secondUserCompleted;
    }
}
