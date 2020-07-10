public class DebugLogger {

    public static boolean DEBUG = false;

    public static boolean PRINT_NETWORK_FLAG = false;

    public static void PRINT_NETWORK(Network network){
        if(DebugLogger.PRINT_NETWORK_FLAG){
            System.out.println(network);
        }
    }

    public static void LOG(double d){
        DebugLogger.LOG(Double.toString(d));
    }

    public static void LOG(String str) {
        if (DebugLogger.DEBUG) {
            System.out.println("DEBUG: " + str);
        }
    }

}