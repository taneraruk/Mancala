package com.bol.game.component;

import com.bol.game.enumeration.GameState;
import com.bol.game.pojo.Board;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class GameComponentTest {

    @Test
    public void nextPlayerTurn() {

        final GameComponent gameComponent = new GameComponent();
        gameComponent.start();
        // |0|6|6|6|6|6|6| |
        // | |6|6|6|6|6|6|0|

        final Board board = gameComponent.getBoard();
        final int[] pits  = board.getPits();

        Assertions.assertThat(board.getState()).isEqualTo(GameState.STARTED);
        Assertions.assertThat(pits).containsExactly(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);
        Assertions.assertThat(board.getTurn()).isEqualTo(GameComponent.FIRST_PLAYER);

        gameComponent.select(1);
        // |0|6|6|6|6|6|7| |
        // | |6|0|7|7|7|7|1|

        Assertions.assertThat(board.getState()).isEqualTo(GameState.PLAYING);
        Assertions.assertThat(pits).containsExactly(6, 0, 7, 7, 7, 7, 1, 7, 6, 6, 6, 6, 6, 0);
        Assertions.assertThat(board.getTurn()).isEqualTo(GameComponent.SECOND_PLAYER);
    }

    @Test
    public void samePlayerTurn() {

        final GameComponent gameComponent = new GameComponent();
        gameComponent.start();
        // |0|6|6|6|6|6|6| |
        // | |6|6|6|6|6|6|0|

        final Board board = gameComponent.getBoard();
        final int[] pits  = board.getPits();

        gameComponent.select(0);
        // |0|6|6|6|6|6|6| |
        // | |0|7|7|7|7|7|1|

        Assertions.assertThat(board.getState()).isEqualTo(GameState.PLAYING);
        Assertions.assertThat(pits).containsExactly(0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0);
        Assertions.assertThat(board.getTurn()).isEqualTo(GameComponent.FIRST_PLAYER);
    }

    @Test
    public void samePlayerTurnWithSkip() {

        final GameComponent gameComponent = new GameComponent();
        gameComponent.start();
        // |0|6|6|6|6|6|6| |
        // | |6|6|6|6|6|6|0|

        final Board board = gameComponent.getBoard();
        final int[] pits  = board.getPits();

        System.arraycopy(new int[]{6, 6, 6, 6, 6, 14, 0, 6, 6, 6, 6, 6, 6, 2}, 0, pits, 0, pits.length);
        // |2|6|6|6|6|6| 6| |
        // | |6|6|6|6|6|14|0|

        gameComponent.select(5);
        // |2|7|7|7|7|7|7| |
        // | |7|7|7|7|7|1|2|

        Assertions.assertThat(board.getState()).isEqualTo(GameState.PLAYING);
        Assertions.assertThat(pits).containsExactly(7, 7, 7, 7, 7, 1, 2, 7, 7, 7, 7, 7, 7, 2);
        Assertions.assertThat(board.getTurn()).isEqualTo(GameComponent.FIRST_PLAYER);
    }

    @Test
    public void skipOpponentsBigBit() {

        final GameComponent gameComponent = new GameComponent();
        gameComponent.start();
        // |0|6|6|6|6|6|6| |
        // | |6|6|6|6|6|6|0|

        final Board board = gameComponent.getBoard();
        final int[] pits  = board.getPits();

        System.arraycopy(new int[]{2, 7, 7, 7, 7, 9, 1, 6, 6, 6, 6, 6, 6, 0}, 0, pits, 0, pits.length);
        // |0|6|6|6|6|6|6| |
        // | |2|7|7|7|7|9|1|

        gameComponent.select(5);
        // |0|7|7|7|7|7|7| |
        // | |3|8|7|7|7|0|2|

        Assertions.assertThat(board.getState()).isEqualTo(GameState.PLAYING);
        Assertions.assertThat(pits).containsExactly(3, 8, 7, 7, 7, 0, 2, 7, 7, 7, 7, 7, 7, 0);
        Assertions.assertThat(board.getTurn()).isEqualTo(GameComponent.SECOND_PLAYER);
    }

    @Test
    public void captureStones() {

        final GameComponent gameComponent = new GameComponent();
        gameComponent.start();

        final Board board = gameComponent.getBoard();
        final int[] pits  = board.getPits();

        System.arraycopy(new int[]{6, 0, 9, 9, 8, 9, 1, 6, 6, 6, 6, 6, 6, 0}, 0, pits, 0, pits.length);
        // |0|6|6|6|6|6|6| |
        // | |6|0|9|9|8|9|1|

        gameComponent.select(5);
        // |0|7|7|7|7|7|7| |
        // | |7|1|9|9|8|0|2|

        // |0|7|0|7|7|7|7| |
        // | |7|8|9|9|8|0|2|

        Assertions.assertThat(board.getState()).isEqualTo(GameState.PLAYING);
        Assertions.assertThat(pits).containsExactly(7, 8, 9, 9, 8, 0, 2, 7, 7, 7, 7, 0, 7, 0);
        Assertions.assertThat(board.getTurn()).isEqualTo(GameComponent.SECOND_PLAYER);
    }

    @Test
    public void endGameFirstPlayerIsWinner() {

        final GameComponent gameComponent = new GameComponent();
        gameComponent.start();
        // |0|6|6|6|6|6|6| |
        // | |6|6|6|6|6|6|0|

        final Board board = gameComponent.getBoard();
        final int[] pits  = board.getPits();

        System.arraycopy(new int[]{0, 0, 0, 0, 0, 2, 38, 4, 0, 3, 0, 2, 1, 22}, 0, pits, 0, pits.length);
        // |22|1|2|0|3|0|4|  |
        // |  |0|0|0|0|0|2|38|

        gameComponent.select(5);
        // |33|0|0|0|0|0|0|  |
        // |  |0|0|0|0|0|0|39|

        Assertions.assertThat(board.getState()).isEqualTo(GameState.OVER);
        Assertions.assertThat(pits).containsExactly(0, 0, 0, 0, 0, 0, 39, 0, 0, 0, 0, 0, 0, 33);
        Assertions.assertThat(board.getTurn()).isEqualTo(GameComponent.FIRST_PLAYER);
        Assertions.assertThat(board.getWinner()).isEqualTo(GameComponent.FIRST_PLAYER);
    }

    @Test
    public void endGameSecondPlayerIsWinner() {

        final GameComponent gameComponent = new GameComponent();
        gameComponent.start();
        // |0|6|6|6|6|6|6| |
        // | |6|6|6|6|6|6|0|

        final Board board = gameComponent.getBoard();
        final int[] pits  = board.getPits();

        System.arraycopy(new int[]{4, 0, 3, 0, 2, 1, 22, 0, 0, 0, 0, 0, 2, 38}, 0, pits, 0, pits.length);
        // |38|2|0|0|0|0|0|  |
        // |  |4|0|3|0|2|1|22|

        gameComponent.select(0);
        Assertions.assertThat(board.getState()).isEqualTo(GameState.PLAYING);
        Assertions.assertThat(pits).containsExactly(0, 1, 4, 1, 3, 1, 22, 0, 0, 0, 0, 0, 2, 38);
        Assertions.assertThat(board.getTurn()).isEqualTo(GameComponent.SECOND_PLAYER);

        gameComponent.select(5);
        // |39|0|0|0|0|0|0|  |
        // |  |0|0|0|0|0|0|33|

        Assertions.assertThat(board.getState()).isEqualTo(GameState.OVER);
        Assertions.assertThat(pits).containsExactly(0, 0, 0, 0, 0, 0, 33, 0, 0, 0, 0, 0, 0, 39);
        Assertions.assertThat(board.getTurn()).isEqualTo(GameComponent.SECOND_PLAYER);
        Assertions.assertThat(board.getWinner()).isEqualTo(GameComponent.SECOND_PLAYER);
    }

    @Test
    public void endGameDrawMatch() {

        final GameComponent gameComponent = new GameComponent();
        gameComponent.start();
        // |0|6|6|6|6|6|6| |
        // | |6|6|6|6|6|6|0|

        final Board board = gameComponent.getBoard();
        final int[] pits  = board.getPits();

        System.arraycopy(new int[]{0, 0, 0, 0, 0, 2, 35, 4, 0, 3, 0, 2, 1, 25}, 0, pits, 0, pits.length);
        // |25|1|2|0|3|0|4|  |
        // |  |0|0|0|0|0|2|35|

        gameComponent.select(5);
        // |36|0|0|0|0|0|0|  |
        // |  |0|0|0|0|0|0|36|

        Assertions.assertThat(board.getState()).isEqualTo(GameState.OVER);
        Assertions.assertThat(pits).containsExactly(0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 0, 36);
        Assertions.assertThat(board.getTurn()).isEqualTo(GameComponent.FIRST_PLAYER);
        Assertions.assertThat(board.getWinner()).isEqualTo(GameComponent.MATCH_DRAW);
    }
}