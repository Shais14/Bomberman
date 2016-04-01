package data;

/**
 * Created by Anand on 4/1/2016.
 */
public class PosNum {
    public int rowIndex;
    public int colIndex;

    public PosNum() {
    }

    public PosNum(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    /**
     * Return the String representaion of a particular tile
     * @return
     */
    public String toString() {
        return rowIndex + " " + colIndex;
    }

    /**
     * Method to fetch reference to the tile represented by a string
     * @param s String representing a particular tile. Should be of the format - "rowIndex colIndex"
     * @param bombermanMap Instance of the map (graph)
     * @return Reference to the Tile
     */
    public static Tile toTile(String s, BombermanMap bombermanMap) {
        String[] indicesStr = s.split(" ");

        int rowIndex = Integer.parseInt(indicesStr[0]);
        int colIndex = Integer.parseInt(indicesStr[1]);

        return bombermanMap.tiles[rowIndex][colIndex];
    }
}
