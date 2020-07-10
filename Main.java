public class Main {

    public static void main(String[] args) {
        //DebugLogger.DEBUG = true;
        //DebugLogger.PRINT_NETWORK_FLAG = true;
        Network network = new Network(new int[] { 3, 4 });
        System.out.println("Prediction: " + network.predictDirection(new InputInterface() {

            @Override
            public double[] getInputVector() {
                return new double[] { 1,2,3 };
            }

        }));
    }
}