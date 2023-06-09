package tonieskelinen.mobilehulk;

/**
 *
 * @author Toni Eskelinen
 */
public class MissionSalvageTheBridge extends Mission {

    private final Coordinate terminal;

    public MissionSalvageTheBridge() {
        terminal = new Location(1, 19);
    }

    public int[][] getFloor() {
        int[][] tiles = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 26, 32, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 26, 17, 17, 17, 25, 0, 0, 0, 0, 0, 0, 18, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 17, 17, 17, 17, 22, 1, 1, 1, 16, 0, 0, 0, 0, 0, 0, 18, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 14, 1, 1, 1, 1, 1, 1, 1, 16, 0, 26, 17, 17, 17, 17, 22, 1, 21, 17, 17, 17, 17, 17, 25, 0, 0, 0 }, { 0, 0, 0, 0, 19, 19, 19, 19, 23, 1, 1, 1, 16, 0, 18, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 16, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 27, 23, 1, 20, 24, 0, 18, 1, 20, 19, 19, 23, 1, 20, 19, 19, 19, 23, 1, 16, 18, 28, 16 }, { 0, 26, 17, 17, 17, 17, 17, 17, 17, 22, 1, 21, 17, 17, 22, 1, 16, 0, 0, 18, 1, 16, 0, 0, 0, 18, 1, 16, 18, 1, 16 }, { 0, 18, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 16, 0, 0, 18, 1, 16, 0, 0, 0, 18, 1, 16, 18, 1, 16 }, { 0, 18, 1, 20, 19, 19, 19, 19, 19, 23, 1, 20, 19, 19, 19, 19, 24, 0, 0, 18, 1, 16, 0, 0, 0, 18, 1, 16, 18, 1, 16 }, { 26, 22, 1, 21, 25, 0, 0, 0, 0, 18, 1, 16, 0, 0, 0, 0, 0, 0, 0, 18, 1, 16, 0, 0, 0, 18, 1, 16, 18, 1, 16 }, { 18, 1, 1, 1, 21, 17, 17, 17, 17, 22, 1, 21, 17, 17, 17, 17, 17, 17, 17, 22, 1, 21, 17, 17, 17, 22, 1, 21, 22, 1, 16 }, { 18, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 16 }, { 18, 1, 1, 1, 20, 19, 19, 19, 19, 23, 1, 20, 19, 19, 19, 19, 19, 19, 19, 23, 1, 20, 19, 19, 19, 23, 1, 20, 23, 1, 16 }, { 27, 23, 1, 20, 24, 0, 0, 0, 0, 18, 1, 16, 0, 0, 0, 0, 0, 0, 0, 18, 1, 16, 0, 0, 0, 18, 1, 16, 18, 1, 16 }, { 26, 22, 1, 21, 25, 0, 0, 0, 0, 18, 1, 16, 0, 0, 0, 0, 0, 0, 0, 18, 1, 16, 0, 0, 0, 18, 1, 16, 18, 1, 16 }, { 18, 1, 1, 1, 21, 17, 17, 17, 17, 22, 1, 21, 17, 17, 17, 17, 17, 17, 17, 22, 1, 21, 17, 17, 17, 22, 1, 21, 22, 1, 16 }, { 18, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 16 }, { 18, 1, 1, 1, 20, 19, 19, 19, 19, 23, 1, 20, 19, 19, 19, 19, 19, 19, 19, 23, 1, 20, 19, 19, 19, 23, 1, 20, 23, 1, 16 }, { 27, 23, 1, 20, 24, 0, 0, 0, 0, 18, 1, 16, 0, 0, 0, 0, 0, 0, 0, 18, 1, 16, 0, 0, 0, 18, 1, 16, 18, 1, 16 }, { 0, 18, 1, 21, 17, 17, 17, 17, 17, 22, 1, 21, 17, 17, 17, 17, 25, 0, 0, 18, 1, 16, 0, 0, 0, 18, 1, 16, 18, 1, 16 }, { 0, 18, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 16, 0, 0, 18, 1, 16, 0, 0, 0, 18, 1, 16, 18, 1, 16 }, { 0, 27, 19, 19, 19, 19, 19, 19, 19, 23, 1, 20, 19, 19, 23, 1, 16, 0, 0, 18, 1, 16, 0, 0, 0, 18, 1, 16, 18, 1, 16 }, { 0, 0, 0, 0, 0, 0, 0, 0, 26, 22, 1, 21, 25, 0, 18, 1, 21, 17, 17, 22, 1, 21, 17, 17, 17, 22, 1, 16, 18, 29, 16 }, { 0, 0, 0, 0, 17, 17, 17, 17, 22, 1, 1, 1, 16, 0, 18, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 16, 0, 0, 0 }, { 0, 0, 0, 0, 14, 1, 1, 1, 1, 1, 1, 1, 16, 0, 27, 19, 19, 19, 19, 23, 1, 20, 19, 19, 19, 19, 19, 24, 0, 0, 0 }, { 0, 0, 0, 0, 19, 19, 19, 19, 23, 1, 1, 1, 16, 0, 0, 0, 0, 0, 0, 18, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 27, 19, 19, 19, 24, 0, 0, 0, 0, 0, 0, 18, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 34, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
        return tiles;
    }

    public int[][] getItems() {
        int[][] tiles = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 3, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 3, 5 }, { 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 3, 5, 0, 0, 0, 7, 3, 5, 0, 0, 0 }, { 0, 7, 3, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 9, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 7, 3, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 3, 5, 0, 0, 0, 7, 3, 5, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 3, 5 }, { 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 3, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
        return tiles;
    }

    public int[][] getSections() {
        int[][] tiles = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 47, 47, 0, 0, 0, 37, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 36, 36, 36, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 47, 47, 0, 0, 0, 37, 35, 0, 0, 0, 0, 0, 0, 0, 37, 35, 37, 42, 39, 43, 35, 0, 37, 35, 0, 0, 47, 47, 47 }, { 0, 0, 47, 47, 0, 0, 0, 37, 35, 0, 0, 0, 0, 0, 0, 0, 37, 35, 37, 35, 0, 37, 35, 0, 37, 35, 0, 0, 47, 47, 47 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 36, 36, 36, 0, 0, 36, 36, 41, 35, 37, 40, 36, 41, 35, 0, 37, 40, 36, 36, 0, 0, 0 }, { 0, 0, 0, 37, 35, 0, 0, 0, 37, 42, 39, 43, 35, 37, 42, 39, 39, 0, 0, 39, 39, 39, 0, 0, 0, 45, 45, 45, 0, 0, 0 }, { 0, 0, 0, 37, 35, 0, 0, 0, 37, 35, 0, 37, 35, 37, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 39, 39, 39, 0, 0, 0 }, { 0, 36, 36, 41, 35, 0, 0, 0, 37, 40, 36, 41, 35, 37, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 39, 39, 39, 0, 0, 0, 0, 0, 45, 45, 45, 0, 0, 0, 0, 0, 0, 0, 36, 36, 36, 0, 0, 0, 36, 36, 36, 36, 36, 36 }, { 0, 0, 0, 0, 37, 35, 0, 0, 37, 42, 39, 43, 35, 0, 0, 37, 35, 0, 37, 42, 39, 43, 35, 0, 37, 42, 39, 43, 42, 39, 39 }, { 0, 0, 0, 0, 37, 35, 0, 0, 37, 35, 0, 37, 35, 0, 0, 37, 35, 0, 37, 35, 0, 37, 35, 0, 37, 35, 0, 37, 35, 0, 0 }, { 0, 0, 0, 0, 37, 35, 0, 0, 37, 40, 36, 41, 35, 0, 0, 37, 35, 0, 37, 40, 36, 41, 35, 0, 37, 40, 36, 41, 40, 36, 36 }, { 0, 36, 36, 36, 0, 0, 0, 0, 0, 39, 39, 39, 0, 0, 0, 0, 0, 0, 0, 39, 39, 39, 0, 0, 0, 39, 39, 39, 39, 39, 39 }, { 0, 39, 39, 39, 0, 0, 0, 0, 0, 36, 36, 36, 0, 0, 0, 0, 0, 0, 0, 36, 36, 36, 0, 0, 0, 36, 36, 36, 36, 36, 36 }, { 0, 0, 0, 0, 37, 35, 0, 0, 37, 42, 39, 43, 35, 0, 0, 37, 35, 0, 37, 42, 39, 43, 35, 0, 37, 42, 39, 43, 42, 39, 39 }, { 0, 0, 0, 0, 37, 35, 0, 0, 37, 35, 0, 37, 35, 0, 0, 37, 35, 0, 37, 35, 0, 37, 35, 0, 37, 35, 0, 37, 35, 0, 0 }, { 0, 0, 0, 0, 37, 35, 0, 0, 37, 40, 36, 41, 35, 0, 0, 37, 35, 0, 37, 40, 36, 41, 35, 0, 37, 40, 36, 41, 40, 36, 36 }, { 0, 36, 36, 36, 0, 0, 0, 0, 0, 45, 45, 45, 0, 0, 0, 0, 0, 0, 0, 39, 39, 39, 0, 0, 0, 39, 39, 39, 39, 39, 39 }, { 0, 39, 39, 43, 35, 0, 0, 0, 37, 42, 39, 43, 35, 37, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 37, 35, 0, 0, 0, 37, 35, 0, 37, 35, 37, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 36, 36, 36, 0, 0, 0 }, { 0, 0, 0, 37, 35, 0, 0, 0, 37, 40, 36, 41, 35, 37, 40, 36, 36, 0, 0, 36, 36, 36, 0, 0, 0, 45, 45, 45, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 39, 39, 39, 0, 0, 39, 39, 43, 35, 37, 42, 39, 43, 35, 0, 37, 42, 39, 39, 0, 0, 0 }, { 0, 0, 47, 47, 0, 0, 0, 37, 35, 0, 0, 0, 0, 0, 0, 0, 37, 35, 37, 35, 0, 37, 35, 0, 37, 35, 0, 0, 47, 47, 47 }, { 0, 0, 47, 47, 0, 0, 0, 37, 35, 0, 0, 0, 0, 0, 0, 0, 37, 35, 37, 40, 36, 41, 35, 0, 37, 35, 0, 0, 47, 47, 47 }, { 0, 0, 47, 47, 0, 0, 0, 37, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 39, 39, 39, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
        return tiles;
    }

    public void init(HulkGameCanvas game) {
        int heading = PieceWithHeading.HEADING_UP;
        game.addMarine(Marine.TYPE_SERGEANT, 20, 28, heading);
        game.addMarine(Marine.TYPE_FLAMERMARINE, 20, 29, heading);
        game.addMarine(Marine.TYPE_MARINE, 20, 30, heading);
        game.addMarine(Marine.TYPE_TECHMARINE, 20, 31, heading);
        game.addMarine(Marine.TYPE_MARINE, 20, 32, heading);
        heading = PieceWithHeading.HEADING_DOWN;
        game.addMarine(Marine.TYPE_SERGEANT, 20, 5, heading);
        game.addMarine(Marine.TYPE_FLAMERMARINE, 20, 4, heading);
        game.addMarine(Marine.TYPE_MARINE, 20, 3, heading);
        game.addMarine(Marine.TYPE_TECHMARINE, 20, 2, heading);
        game.addMarine(Marine.TYPE_MARINE, 20, 1, heading);
        game.addBlip(2, 14);
        game.addBlip(2, 19);
        if (isTerminaFixed(game)) {
            throw new InternalException(this, "init", "Sanity check failed.");
        }
    }

    public int getHeadingForBlip(Coordinate blip) {
        int heading = PieceWithHeading.HEADING_RIGHT;
        if (blip.getX() > 10) {
            if (blip.getY() > 15) {
                heading = PieceWithHeading.HEADING_UP;
            } else {
                heading = PieceWithHeading.HEADING_DOWN;
            }
        }
        return heading;
    }

    public int getEntryAreaX(Coordinate c) {
        if (c.getX() > 15) {
            return 29;
        } else {
            return 4;
        }
    }

    public int getEntryAreaY(Coordinate c) {
        if (c.getX() > 15) {
            if (c.getY() < 15) {
                return 8;
            } else {
                return 25;
            }
        } else {
            if (c.getY() < 15) {
                return 6;
            } else {
                return 27;
            }
        }
    }

    public Coordinate[] getReinforcementLocations(HulkGameCanvas game) {
        switch(game.currentTurn) {
            case 1:
            case 5:
            case 9:
                return new Coordinate[] { new Location(29, 7) };
            case 2:
            case 6:
            case 10:
                return new Coordinate[] { new Location(29, 26) };
            case 3:
            case 7:
            case 11:
                return new Coordinate[] { new Location(3, 27) };
            case 4:
            case 8:
            case 12:
                return new Coordinate[] { new Location(3, 6) };
            default:
                throw new InternalException(this, "getReinforcementLocation", "Too many turns");
        }
    }

    public boolean isMissionOver(HulkGameCanvas game) {
        if (game.currentTurn >= 12) {
            return true;
        }
        if (isTerminaFixed(game)) {
            return true;
        }
        for (int i = 0; i < game.marines.length; i++) {
            if (game.marines[i].getType() == Marine.TYPE_TECHMARINE) {
                return false;
            }
        }
        return true;
    }

    public String getBriefing() {
        return "Use one of your Tech Marines to repair bridge's computer console" + " (2 AP) before the end of turn 12.";
    }

    public String getDebriefing(HulkGameCanvas game) {
        if (isTerminaFixed(game)) {
            return "Congratulations! You turned on computer terminal in " + game.currentTurn + " turns.";
        } else {
            if (game.currentTurn >= 12) {
                return "You failed to activate computer terminal within 12 turns.";
            } else {
                return "You failed to protect your Tech Marines until they can repair computer terminal.";
            }
        }
    }

    public Coordinate[] getDormantBlipsToBeConverted(HulkGameCanvas game) {
        return null;
    }

    private boolean isTerminaFixed(HulkGameCanvas game) {
        int item = game.getItem(terminal);
        if (item == ITEM_TERMINAL_OFF) {
            return false;
        } else if (item == ITEM_TERMINAL_ON) {
            return true;
        } else {
            throw new InternalException(this, "isTerminalFixed", "Where is the terminal? " + terminal);
        }
    }
}
