public class GenericAdder {

    public static <T extends Number> double add(T a, T b) {
        return a.doubleValue() + b.doubleValue();
    }

    public static void main(String[] args) {
        System.out.println("Add ints: " + add(5, 10));               // 15.0
        System.out.println("Add doubles: " + add(3.5, 2.2));         // 5.7
        System.out.println("Add floats: " + add(1.2f, 4.3f));        // 5.5
        System.out.println("Add longs: " + add(100L, 200L));         // 300.0
    }
}