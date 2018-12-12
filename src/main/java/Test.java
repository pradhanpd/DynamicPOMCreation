import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
//        Map<Integer, String> x = new HashMap<>();
//        x.put(0, "prad");
//        x.put(1, "test");
//        x.put(2, "praj");
//        x.put(3, "shou");
//        Map<Integer, String> y = new HashMap<>();
//        y.put(0, "prad");
//        y.put(1, "prajna");
//        y.put(2, "shou");
//
//        Set<Map.Entry<Integer, String>> origMethodsRemoved = new HashSet<>(x.entrySet());
//        origMethodsRemoved.removeAll(y.entrySet());
//
//        Set<Map.Entry<Integer, String>> newMethodsAdded = new HashSet<>(y.entrySet());
//        newMethodsAdded.removeAll(x.entrySet());

        String[] x = {"prad", "testid", "praj", "shou"};
        String[] y = {"prad", "testname", "prajna", "shou"};
        Set<String> orig = new HashSet<>(Arrays.asList(x));
        Set<String> neww = new HashSet<>(Arrays.asList(y));

        orig.removeAll(new HashSet<>(Arrays.asList(y)));
        neww.removeAll(new HashSet<>(Arrays.asList(x)));
    }
}
