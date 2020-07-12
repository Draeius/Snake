import java.util.ArrayList;

public class Matrix {

    private double[][][] matrix;
    private boolean isEditing = false;
    private ArrayList<ArrayList<ArrayList<Double>>> editList;

    /**
     * generates a random matrix with the given layer sizes
     * 
     * @param sizes The size of the layers. index 0 is input size, length -1 is
     *              output size
     */
    public Matrix(int[] sizes) {
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
    }

    public Matrix(double[][][] matrix) {
        this.matrix = matrix;
    }

    public double[][][] getMatrix() {
        endEditing();
        return matrix;
    }

    public void addNode(int layer) {
        startEditing();
        if (layer == 0 || layer == editList.size() - 1) {
            return;
        }
        // create new node
        ArrayList<Double> node = new ArrayList<>();
        for (int w = 0; w < editList.get(layer - 1).size() + 1; w++) {
            // fill node with random weights
            node.add(Double.valueOf(Math.random()));
        }
        // add node
        editList.get(layer).add(node);
        correctWeightCount(layer + 1);
    }

    /**
     * TODO: see if it is more effective to delete random node instead of always the
     * last one
     * 
     * @param layer
     */
    public void removeNode(int layer) {
        startEditing();
        if (layer == 0 || layer == editList.size() - 1) {
            return;
        }

        editList.get(layer).remove(editList.get(layer).size() - 1);
        correctWeightCount(layer + 1);
    }

    public void removeLayer(int layer) {
        startEditing();
        if (layer == 0 || layer == editList.size() - 1) {
            System.out.println("test");
            return;
        }

        editList.remove(layer);
        correctWeightCount(layer);
    }

    public void addLayer(int layer, int size) {
        startEditing();
        if (layer == 0 || layer == editList.size()) {
            return;
        }

        editList.add(layer, new ArrayList<>());
        for (int n = 0; n < size; n++) {
            editList.get(layer).add(new ArrayList<>());
            for (int w = 0; w < editList.get(layer - 1).size() + 1; w++) {
                editList.get(layer).get(n).add(Math.random());
            }
        }
        correctWeightCount(layer + 1);
    }

    public void mutateWeights(float mutationRate) {
        startEditing();
        for (int l = 0; l < editList.size(); l++) {
            for (int n = 0; n < editList.get(l).size(); n++) {
                for (int w = 0; w < editList.get(l).get(n).size(); w++) {
                    if (Math.random() <= mutationRate) {
                        editList.get(l).get(n).set(w, Double.valueOf(Math.random()));
                    }
                }
            }
        }
    }

    public String toString() {
        endEditing();
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

    private void correctWeightCount(int layer) {
        // get the size of the previous layer
        int prevLayerSize = editList.get(layer - 1).size();
        // get the size of the layer to correct
        int layerSize = editList.get(layer).size();

        for (int n = 0; n < layerSize; n++) {
            // delete weights that are not needed
            while (editList.get(layer).get(n).size() > prevLayerSize + 1) {
                /*
                 * remove last weight first, since it does not matter if a whole layer mutated
                 * away. If a node was removed, it is the last node in the list, so the weights
                 * for that node are removed
                 */
                editList.get(layer).get(n).remove(editList.get(layer).get(n).size() - 1);
            }
            // add weights if there are not enough
            while (editList.get(layer).get(n).size() < prevLayerSize + 1) {
                /*
                 * add weight at the end, since new nodes are added at the end of the list. This
                 * way, new weights are not added to existing nodes
                 */
                editList.get(layer).get(n).add(Double.valueOf(Math.random()));
            }
        }
    }

    private void startEditing() {
        if (!isEditing) {
            isEditing = true;
            editList = toArrayList(matrix);
        }
    }

    private void endEditing() {
        if (isEditing) {
            isEditing = false;
            matrix = toMatrix(editList);
            editList = null;
        }
    }

    private ArrayList<ArrayList<ArrayList<Double>>> toArrayList(double[][][] matrix) {
        ArrayList<ArrayList<ArrayList<Double>>> list = new ArrayList<>();
        // create dummy input layer
        list.add(new ArrayList<>());
        // fill with dummy input nodes
        for (int n = 0; n < matrix[0][0].length - 1; n++) {
            list.get(0).add(new ArrayList<>());
        }

        // add actual layers, nodes and weights
        for (int l = 0; l < matrix.length; l++) {
            list.add(new ArrayList<>());
            for (int n = 0; n < matrix[l].length; n++) {
                list.get(l + 1).add(new ArrayList<>());
                for (int w = 0; w < matrix[l][n].length; w++) {
                    list.get(l + 1).get(n).add(matrix[l][n][w]);
                }
            }
        }
        return list;
    }

    private double[][][] toMatrix(ArrayList<ArrayList<ArrayList<Double>>> list) {
        double[][][] matrix = new double[list.size() - 1][][];

        // add 1 to l in the list to skip the dummy input layer
        for (int l = 0; l < matrix.length; l++) {
            matrix[l] = new double[list.get(l + 1).size()][];
            for (int n = 0; n < matrix[l].length; n++) {
                matrix[l][n] = new double[list.get(l + 1).get(n).size()];
                for (int w = 0; w < matrix[l][n].length; w++) {
                    matrix[l][n][w] = list.get(l + 1).get(n).get(w);
                }
            }
        }
        return matrix;
    }

}