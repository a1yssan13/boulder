package org.cis120.boulder;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {
    //Tests the main game logic
    GameCourt g = new GameCourt(new JLabel());

    @Test
    public void test() {
        assertNotEquals("CIS 120", "CIS 160");
    }

    //Tests the actions of person
    @Test
    public void testSisyphusChangesState() {
        Sisyphus person = new Sisyphus(50,
                100, GameCourt.SISYPHUS_WIDTH, GameCourt.SISPHYUS_HEIGHT);
        person.changeState(State.KILL);
        assertEquals(person.getState(), State.KILL);
        person.changeState(State.UP);
        assertEquals(person.getState(), State.UP);
        person.changeState(State.DOWN);
        assertEquals(person.getState(), State.DOWN);
    }

    @Test
    public void testSisyphusMovesinFlatland() {
        //sisyphus is in flat areas
        Sisyphus person = new Sisyphus(GameCourt.FLAT_AREA * GameCourt.COURT_PIXEL - 60,
                100, GameCourt.SISYPHUS_WIDTH, GameCourt.SISPHYUS_HEIGHT);
        int initialx = person.getx();
        person.walk(GameCourt.FLAT_AREA - 10, 500);
        assertNotEquals(person.getx(), initialx); //person should move right
        assertTrue(person.getx() <= GameCourt.FLAT_AREA * GameCourt.COURT_PIXEL);
        int bottomHill = (GameCourt.COURT_Y - GameCourt.BOTTOM_HILL
                - GameCourt.ROCK_SIZE) * GameCourt.COURT_PIXEL;
        assertEquals(person.gety(), bottomHill); //should be at bottom of hill
    }

    @Test
    public void testSisyphusMovesOnHill() {
        //sisyphus is in flat areas
        Sisyphus person = new Sisyphus(GameCourt.FLAT_AREA * GameCourt.COURT_PIXEL - 60,
                100, GameCourt.SISYPHUS_WIDTH, GameCourt.SISPHYUS_HEIGHT);
        int initialx = person.getx();
        person.walk(GameCourt.FLAT_AREA + 10, 500);
        assertNotEquals(person.getx(), initialx); //person should move right
        int flatArea = GameCourt.FLAT_AREA * GameCourt.COURT_PIXEL;
        assertTrue(person.getx() >= flatArea);
        assertTrue(person.getx() < GameCourt.COURT_WIDTH - flatArea);
        assertTrue(person.gety() <= 500 * GameCourt.COURT_PIXEL);
    }

    //Tests the weather of the program

    //edge case when sisyphus is at edge of hill
    @Test
    public void testSisyphusAtEdgeOfHill() {
        //testing edge case
        int initialx = GameCourt.COURT_WIDTH - GameCourt.SISYPHUS_WIDTH;
        int initialy = GameCourt.TOP_HILL * GameCourt.COURT_PIXEL;
        Sisyphus person = new Sisyphus(initialx, initialy,
                GameCourt.SISYPHUS_WIDTH, GameCourt.SISPHYUS_HEIGHT);
        person.walk(GameCourt.COURT_X, GameCourt.TOP_HILL);
        assertEquals(person.getx(), initialx); //sisyphus doesn't move when he's at the top
    }

    @Test
    public void testRainChangesAmount() {
        Rain rainstorm = new Rain();
        int initialSize = rainstorm.getRainSize();
        rainstorm.change();
        assertEquals(initialSize, rainstorm.getRainSize());
        for (int i = 0; i <= Rain.CHANGE_HEAVY; i++) {
            rainstorm.change();
        }
        assertNotEquals(initialSize, rainstorm.getRainSize());
    }

    @Test
    public void testRainAlwaysRaining() {
        Rain rainstorm = new Rain();
        int initialSize = rainstorm.getRainSize();
        rainstorm.change();
        assertEquals(initialSize, rainstorm.getRainSize());
        for (int i = 0; i <= 30; i++) {
            for (int j = 0; j < Rain.CHANGE_HEAVY; j++) {
                rainstorm.change();
            }
            int newSize = rainstorm.getRainSize();
            assertNotEquals(initialSize, newSize);
            assertTrue(newSize > 0);
            initialSize = newSize;
        }
    }

    @Test
    public void testSunStateChanges() {
        Sun sunshine = new Sun();
        int index = sunshine.getIndex();
        sunshine.change();
        assertNotEquals(index, sunshine.getIndex());
        sunshine.change();
        assertEquals(index, sunshine.getIndex());
    }

    @Test
    public void testSpeedofRock() {
        double fast = g.getSpeed(5);
        double medium = g.getSpeed(3);
        double slow = g.getSpeed(1);
        assertTrue(fast > medium && medium > slow);
    }

    @Test
    public void testSpeedofRockisZero() {
        double noPresses = g.getSpeed(0);
        assertEquals(noPresses, -1.0 * GameCourt.MAX_ROCKSPEED / 2.0);
    }

    @Test
    public void testWeatherChangesOverTime() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        String weather = g.getWeather();
        boolean hasChanged = false;
        for (int i = 0; i < 20; i++) {
            g.reset();
            if (!(weather.equals(g.getWeather()))) {
                hasChanged = true;
            }
        }
        assertTrue(hasChanged);
    }

    @Test
    public void testRockMovesUpMidHill() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        g.setRockX(50);
        g.setRockY(GameCourt.BOTTOM_HILL); //boulder is clearly below the hill
        g.moveUp(1);
        assertEquals(g.getRockX(), 51);
        assertEquals(g.getRockY(), 52);
    }

    @Test
    public void testRockMovesUpAtTopOfHill() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        g.setRockX(180);
        g.setRockY(GameCourt.BOTTOM_HILL); //boulder is clearly below the hill
        g.moveUp(1);
        assertEquals(g.getRockX(), 181);
        assertEquals(g.getRockY(), GameCourt.TOP_HILL + GameCourt.ROCK_SIZE);
    }

    @Test
    public void testRockMovesUpAtBottomOfHill() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        g.setRockX(10);
        g.setRockY(GameCourt.BOTTOM_HILL); //boulder is clearly below the hill
        g.moveUp(1);
        assertEquals(g.getRockX(), 11);
        assertEquals(g.getRockY(), GameCourt.BOTTOM_HILL + GameCourt.ROCK_SIZE);
    }

    @Test
    public void testRockDoesntMoveAtTipTop() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        g.setRockX(190);
        g.setRockY(GameCourt.BOTTOM_HILL); //boulder is clearly below the hill
        g.moveUp(1);
        assertEquals(g.getRockX(), 190);
        assertEquals(g.getRockY(), GameCourt.BOTTOM_HILL);
        assertFalse(g.getDirection());
    }

    @Test
    public void testRockMovesDown() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        g.setRockX(52);
        g.setRockY(GameCourt.TOP_HILL); //boulder is clearly below the hill
        g.moveDown(1);
        assertEquals(g.getRockX(), 51);
        assertEquals(g.getRockY(), 52);
    }

    @Test
    public void testRockMovesDownFromTop() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        g.setRockX(191);
        g.setRockY(GameCourt.TOP_HILL + GameCourt.ROCK_SIZE); //boulder is clearly below the hill
        g.moveDown(1);
        assertEquals(g.getRockX(), 190);
        assertEquals(g.getRockY(), GameCourt.TOP_HILL + GameCourt.ROCK_SIZE);
    }

    @Test
    public void testRockMovesDownFromBottomHill() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        g.setRockX(9);
        g.setRockY(GameCourt.BOTTOM_HILL + GameCourt.ROCK_SIZE); //boulder is clearly below the hill
        g.moveDown(1);
        assertEquals(g.getRockX(), 8);
        assertEquals(g.getRockY(), GameCourt.BOTTOM_HILL + GameCourt.ROCK_SIZE);
    }

    @Test
    public void testShowsMenuIfNotForwardAtVeryBottom() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        g.setRockX(3);
        g.setDirection(false);
        g.moveDown(1);
        assertTrue(g.getMenu());
    }
    //Tests

    @Test
    public void testCalculateRockSpeedOffArray() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        LinkedList<Integer> e = new LinkedList<>(Arrays.asList(
                0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0
        ));
        double speed = g.calculateRockSpeed(e);
        assertEquals(speed, g.getSpeed(6));
    }

    @Test
    public void testCalculateRockSpeedOffIncompleteArray() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        LinkedList<Integer> e = new LinkedList<>(Arrays.asList(
                0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0
        ));
        double speed = g.calculateRockSpeed(e);
        assertNotEquals(speed, g.getSpeed(3));
        assertEquals(speed, 0);
    }

    @Test
    public void testCalculateRockSpeedOffArrayLengthOne() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        LinkedList<Integer> e = new LinkedList<>(Arrays.asList(
                0
        ));
        double speed = g.calculateRockSpeed(e);
        assertNotEquals(speed, g.getSpeed(0));
        assertEquals(speed, 0);
    }

    @Test
    public void testCalculateRockSpeedOffNoPresses() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        LinkedList<Integer> e = new LinkedList<>(Arrays.asList(
                0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        ));
        double speed = g.calculateRockSpeed(e);
        assertEquals(speed, g.getSpeed(0));
    }

    @Test
    public void testCalculateRockSpeedOffNullArray() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        double speed = g.calculateRockSpeed(null);
        assertEquals(speed, 0);
    }

    //edge case with a realy high amount of key presses
    @Test
    public void testCalculateRockSpeedOffALotOfPresses() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        LinkedList<Integer> e = new LinkedList<>(Arrays.asList(
                0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 6, 3, 4, 5, 6, 5, 2, 7
        ));
        double speed = g.calculateRockSpeed(e);
        assertEquals(speed, g.getSpeed(20));
    }

    @Test
    public void testResetButton() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        g.setDirection(false);
        assertEquals(g.getDirection(), false);
        g.reset();
        assertNotEquals(g.getDirection(), false);
    }

    @Test
    public void testMoveItem() {
        GameCourt g = new GameCourt(new JLabel());
        g.reset();
        g.setDirection(false);
        assertEquals(g.getDirection(), false);
        g.reset();
        assertNotEquals(g.getDirection(), false);
    }

}
