import java.util.ArrayList;

public class Genome {

    /**
     * Probability of mutating each weight
     */
    public static float WEIGHT_MUTATION_RATE = 0.1f;
    /**
     * Probability of mutating the layer count
     */
    public static float LAYER_MUTATION_RATE = 1.1f;
    /**
     * Probability of mutating the node count in a layer
     */
    public static float NODE_COUNT_MUTATION_RATE = 0.1f;

    private Matrix matrix;

    private ActivationFunction activationFunction;

    public Genome(Network network) {
        this(network.getMatrix(), network.getActivationFunction());
    }

    public Genome(double[][][] weights, ActivationFunction activationFunction) {
        this.matrix = new Matrix(weights);
        this.activationFunction = activationFunction;
    }

    public Network buildNetwork() {
        return new Network(matrix.getMatrix(), activationFunction);
    }

    public Genome breed(Genome other) {
        // TODO: remove placeholder
        return new Genome(matrix.getMatrix(), activationFunction);
    }

    public double[][][] mutateWeights(double[][][] matrix) {
        for (int l = 0; l < matrix.length; l++) {
            for (int n = 0; n < matrix[l].length; n++) {
                for (int w = 0; w < matrix[l][n].length; w++) {
                    if (Math.random() <= Genome.WEIGHT_MUTATION_RATE) {
                        matrix[l][n][w] = Math.random();
                    }
                }
            }
        }
        return matrix;
    }

    public double[][][] mutateLayerCount(double[][][] matrix) {
        if (Math.random() > Genome.LAYER_MUTATION_RATE) {
            return matrix;
        }

        ArrayList<ArrayList<ArrayList<Double>>> list = toArrayList(matrix);
        int index = (int) Math.floor(1 + Math.random() * (list.size() - 2));

        if (Math.random() > 0.5) {
            if (index < list.size() - 1) {
                list.remove(index);
            }
        } else {
            int layerSize = (int) Math.round(3 + Math.random() * 7);
            list.add(index, new ArrayList<>());
            for (int n = 0; n < layerSize; n++) {
                list.get(index).add(new ArrayList<>());
                for (int w = 0; w < list.get(index - 1).size() + 1; w++) {
                    list.get(index).get(n).add(Math.random());
                }
            }
        }
        return toMatrix(list);
    }

    private ArrayList<ArrayList<ArrayList<Double>>> toArrayList(double[][][] matrix) {
        ArrayList<ArrayList<ArrayList<Double>>> list = new ArrayList<>();
        for (int l = 0; l < matrix.length; l++) {
            list.add(new ArrayList<>());
            for (int n = 0; n < matrix[l].length; n++) {
                list.get(l).add(new ArrayList<>());
                for (int w = 0; w < matrix[l][n].length; w++) {
                    list.get(l).get(n).add(matrix[l][n][w]);
                }
            }
        }
        return list;
    }

    private double[][][] toMatrix(ArrayList<ArrayList<ArrayList<Double>>> list) {
        double[][][] matrix = new double[list.size()][][];
        for (int l = 0; l < matrix.length; l++) {
            matrix[l] = new double[list.get(l).size()][];
            for (int n = 0; n < matrix[l].length; n++) {
                matrix[l][n] = new double[list.get(l).get(n).size()];
                for (int w = 0; w < matrix[l][n].length; w++) {
                    matrix[l][n][w] = list.get(l).get(n).get(w);
                }
            }
        }
        return matrix;
    }
}