public class Main {

    public static void main(String[] args) {
        // DebugLogger.DEBUG = true;
        DebugLogger.PRINT_NETWORK_FLAG = true;
        Matrix m = new Matrix(new int[] { 3, 2, 3 });
        System.out.println(m);
        m.removeNode(1);
        System.out.println(m);
    }
}