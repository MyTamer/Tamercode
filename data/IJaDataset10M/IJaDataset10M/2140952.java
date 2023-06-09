package dr.geo;

import dr.app.gui.ColorFunction;
import dr.math.distributions.GammaDistribution;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexei Drummond
 */
public class GTOPO30Panel extends JPanel implements Lattice {

    GTOPO30Tile[][] tiles;

    int minLat, maxLat;

    int minLong, maxLong;

    double scale = 0.5;

    public GTOPO30Panel(String[] filenames, ColorFunction colorFunction) throws IOException {
        List<GTOPO30Tile> tileList = new ArrayList<GTOPO30Tile>();
        for (String filename : filenames) {
            GTOPO30Tile tile = new GTOPO30Tile(filename, colorFunction);
            if (tile.getMaxLatitude() > maxLat) maxLat = tile.getMaxLatitude();
            if (tile.getMaxLongitude() > maxLong) maxLong = tile.getMaxLongitude();
            if (tile.getMinLatitude() < minLat) minLat = tile.getMinLatitude();
            if (tile.getMinLongitude() < minLong) minLong = tile.getMinLongitude();
            tileList.add(tile);
        }
        tiles = new GTOPO30Tile[(maxLat - minLat) / 50][(maxLong - minLong) / 40];
        for (GTOPO30Tile tile : tileList) {
            tiles[(maxLat - tile.getMaxLatitude()) / 50][(tile.getMinLongitude() - minLong) / 40] = tile;
        }
        setLayout(new GridLayout(tiles.length, tiles[0].length));
        for (GTOPO30Tile[] tileRow : tiles) {
            for (GTOPO30Tile tile : tileRow) {
                if (tile != null) {
                    add(tile);
                } else {
                    add(new JPanel());
                }
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension((int) Math.round(tiles[0].length * GTOPO30Tile.NCOLS * scale), (int) Math.round(tiles.length * GTOPO30Tile.NROWS * scale));
    }

    /**
     * @param y latitudinal pixel, increasing south from north edge
     * @param x longitudinal pixel index, increasing east from west edge
     * @return the height of the pixel at y,x
     */
    public short getHeight(int y, int x) {
        int tileY = y / GTOPO30Tile.NROWS;
        int tileX = x / GTOPO30Tile.NCOLS;
        int ty = y % GTOPO30Tile.NROWS;
        int tx = x % GTOPO30Tile.NCOLS;
        try {
            short height = tiles[tileY][tileX].getHeight(ty, tx);
            return height;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error at y=" + y + ", x=" + x);
        }
        return -1;
    }

    public Location getLocation(double latitude, double longitude) {
        double relLat = tiles[0][0].getMaxLatitude() - latitude;
        double relLong = longitude - tiles[0][0].getMinLongitude();
        return new Location((int) Math.round(relLong / tiles[0][0].xdim), (int) Math.round(relLat / tiles[0][0].ydim));
    }

    public void setScale(double scale) {
        this.scale = scale;
        setSize(getPreferredSize());
        repaint();
    }

    public int latticeWidth() {
        return tiles[0].length * GTOPO30Tile.NCOLS;
    }

    public int latticeHeight() {
        return tiles.length * GTOPO30Tile.NROWS;
    }

    public int getState(int i, int j) {
        return getHeight(j, i) / 100;
    }

    public void paintLattice(Graphics g) {
        paint(g);
    }

    public RateMatrix getRates(double mu) {
        final double[] toRate = new double[52];
        for (int j = 0; j < toRate.length; j++) {
            int heightStart = j * 100;
            int heightEnd = heightStart + 100;
            double shape = 0.55;
            double scale = 5800.0;
            toRate[j] = mu * (GammaDistribution.cdf(heightEnd, shape, scale) - GammaDistribution.cdf(heightStart, shape, scale));
            System.out.println("toRate[" + j + "] = " + toRate[j]);
        }
        return new RateMatrix() {

            public double getRate(int i, int j) {
                if (j < 0 || j >= toRate.length) return 0;
                return toRate[j];
            }
        };
    }
}
