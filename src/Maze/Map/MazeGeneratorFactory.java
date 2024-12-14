package Maze.Map;

import java.util.Random;
public class MazeGeneratorFactory {
    public enum GeneratorType {
        PRIMS,
        KRUSKALS
    }
    public static MazeGenerator getMazeGenerator(GeneratorType type) {
        switch (type) {
            case PRIMS:
                return new PrimsMazeGenerator();
            case KRUSKALS:
                return new KruskalsMazeGenerator();
            default:
                throw new IllegalArgumentException("Unknown GeneratorType: " + type);
        }
    }
    public static MazeGenerator getRandomMazeGenerator() {
        GeneratorType[] types = GeneratorType.values();
        Random rand = new Random();
        GeneratorType selectedType = types[rand.nextInt(types.length)];
        return getMazeGenerator(selectedType);
    }
}
