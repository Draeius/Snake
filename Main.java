public class Main {

    public static void main(String[] args) {
        try {
            Snake snake = new Snake(10, 10, 5);
            System.out.println(snake);
        } catch (NoValidDirectionException e) {
            System.out.println("Snake too long");
        }
    }
}