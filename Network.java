
public class Network {

    /**
     * matrix is organized as double[layer][node][weight] the first weight is always
     * for the bias
     */
    private double[][][] matrix;

    private ActivationFunction activationFunction;

    public Network(int[] sizes) {
        this(sizes, ActivationFunction.SIGMOID);
    }

    public Network(int[] sizes, ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        /*
         * input layer has no weights => size -1 because there is less weights than
         * nodes.
         */
        matrix = new double[sizes.length - 1][][];
        // iterate layers
        for (int l = 0; l < matrix.length; l++) {
            // sizes + 1 because of bias
            matrix[l] = new double[sizes[l + 1]][];
            // iterate nodes
            for (int n = 0; n < matrix[l].length; n++) {
                // create new bias array; sizes[l] + 1 because bias needs a weight
                matrix[l][n] = new double[sizes[l] + 1];
                // iterate weights
                for (int w = 0; w < matrix[l][n].length; w++) {
                    // fill with random numbers
                    matrix[l][n][w] = Math.random();
                }
            }
        }
        DebugLogger.PRINT_NETWORK(this);
    }

    public Network(double[][][] matrix, ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        this.matrix = matrix;
        DebugLogger.PRINT_NETWORK(this);
    }

    public Direction predictDirection(InputInterface input) {
        double[] prediction = predict(input);
        double max = 0;
        int maxIndex = 0;
        for (int i = 0; i < prediction.length; i++) {
            if (prediction[i] > max) {
                max = prediction[i];
                maxIndex = i;
            }
        }
        return Direction.values()[maxIndex];
    }

    public double[] predict(InputInterface input) {
        return multiplyAllLayers(input.getInputVector(), 0);
    }

    private double[] multiplyAllLayers(double[] input, int currentLayer) {
        if (currentLayer == matrix.length) {
            return input;
        }
        DebugLogger.LOG("Predicting Layer " + currentLayer);
        return multiplyAllLayers(multiplyLayer(currentLayer, input), currentLayer + 1);
    }

    private double[] multiplyLayer(int layer, double[] inputs) {
        int outputSize = matrix[layer].length;
        DebugLogger.LOG(String.format("Layer %2d outputSize: %3d", layer, outputSize));
        double[] output = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            output[i] = multiplyNode(layer, i, inputs);
        }
        return output;
    }

    private double multiplyNode(int layer, int node, double[] inputs) {
        double[] weights = getNodeWeights(layer, node);

        // first apply bias
        double result = 1 * weights[0];
        // iterate all other weights
        for (int i = 1; i < weights.length; i++) {
            // input is index -1 because of bias
            result += inputs[i - 1] * weights[i];
        }
        DebugLogger.LOG(String.format("Layer %2d node %3d output before activation func: %.5f", layer, node, result));
        DebugLogger.LOG(String.format("Layer %2d node %3d output: %.5f", layer, node, activationFunc(result)));
        return activationFunc(result);
    }

    private double activationFunc(double input) {
        return sigmoid(input);
    }

    private double sigmoid(double input) {
        return 0.5 * (1 + Math.tanh(input / 2));
    }

    private double[] getNodeWeights(int layer, int node) {
        if (layer >= matrix.length) {
            return null;
        }
        if (node >= matrix[layer].length) {
            return null;
        }
        return matrix[layer][node];
    }

    public double[][][] getMatrix(){
        return matrix;
    }

    public ActivationFunction getActivationFunction(){
        return activationFunction;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(String.format("Total layers: %2d\n", matrix.length));
        for (int l = 0; l < matrix.length; l++) {
            buffer.append(String.format("Layer %2d, nodes: %3d\n", l + 1, matrix[l].length));
            for (int i = 0; i < matrix[l].length; i++) {
                buffer.append(String.format("node %3d | ", i + 1));
                for (int j = 0; j < matrix[l][i].length; j++) {
                    buffer.append(String.format("%.5f", matrix[l][i][j]) + " ");
                }
                buffer.append("\n");
            }
            buffer.append("--------------------------------------------------------------\n");
        }
        return buffer.toString();
    }
}