package mearns.douglas.haelauncher.util;

import java.util.ArrayList;
import java.util.List;

public class FloatRange {
    public static Iterable<Float> create(float minimum, int intermediateSteps, float maximum) {
        List<Float> result = new ArrayList<>(intermediateSteps + 2);
        result.add(minimum);
        for (int i = 1; i <= intermediateSteps; i++ ) {
            result.add(minimum + ((float)i)/(intermediateSteps + 1) * (maximum - minimum));
        }
        result.add(maximum);
        return result;
    }
}
