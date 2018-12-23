package com.bol.game.pojo;

import com.bol.game.enumeration.GameState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Board entity is used to keep active game's informations.
 * 
 * @author Taner Aruk
 *
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Board {
    private GameState state;
    private int       turn;
    private int[]     pits;
    private int       winner;
}
