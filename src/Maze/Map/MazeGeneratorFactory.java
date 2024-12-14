package Maze.Map;

import java.util.Random;

/**
 * Factory class for creating MazeGenerator instances.
 */
public class MazeGeneratorFactory {

    /**
     * Enumeration of available MazeGenerator types.
     */
    public enum GeneratorType {
        PRIMS,
        KRUSKALS,
        GENETIC
    }

    /**
     * Returns a MazeGenerator instance based on the provided GeneratorType.
     *
     * @param type The type of MazeGenerator desired.
     * @return An instance of MazeGenerator.
     */
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

    /**
     * Returns a randomly selected MazeGenerator instance.
     *
     * @return A randomly chosen MazeGenerator.
     */
    public static MazeGenerator getRandomMazeGenerator() {
        GeneratorType[] types = GeneratorType.values();
        Random rand = new Random();
        GeneratorType selectedType = types[rand.nextInt(types.length)];
        return getMazeGenerator(selectedType);
    }
}
