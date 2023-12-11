public class Main {
        public static void main(String[] args) {
            int mianji = Integer.parseInt(args[0]);
            int a = 2;
            int b = mianji/2;
            a = a - a/2;
            b = b + b/2;
            System.out.println(a + b);
        }
}
