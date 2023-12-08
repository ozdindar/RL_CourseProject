package rl.methods.functionapprox.tilecoding;

import java.util.ArrayList;
import java.util.List;

public class TileCoding {
    private final int numTilings;
    private final int[] numTiles;
    private final double[] tileWidths;

    public TileCoding(int numTilings, int[] numTiles, double[] tileWidths) {
        this.numTilings = numTilings;
        this.numTiles = numTiles.clone();
        this.tileWidths = tileWidths.clone();
    }

    public List<Integer> tiles(double[] features) {
        List<Integer> tileIndices = new ArrayList<>();

        for (int tiling = 0; tiling < numTilings; tiling++) {
            int[] tileIndicesPerTiling = new int[features.length];
            for (int i = 0; i < features.length; i++) {
                int numTile = numTiles[i];
                double tileWidth = tileWidths[i];

                double shiftedValue = features[i] + (tiling * tileWidth / numTilings);
                int tileIndex = (int) Math.floor(shiftedValue / tileWidth);
                tileIndicesPerTiling[i] = tileIndex;
            }

            int tileIndexOffset = tiling * numTiles.length;
            for (int i = 0; i < tileIndicesPerTiling.length; i++) {
                int tileIndex = tileIndicesPerTiling[i] + tileIndexOffset;
                tileIndices.add(tileIndex);
            }
        }

        return tileIndices;
    }

    public static void main(String[] args) {
        int numTilings = 8;
        int[] numTiles = {8, 8};
        double[] tileWidths = {0.4, 0.4};

        TileCoding tileCoding = new TileCoding(numTilings, numTiles, tileWidths);

        double[] features = {0.2, -0.3};
        List<Integer> tileIndices = tileCoding.tiles(features);

        System.out.println("Tile Indices: " + tileIndices);
    }
}