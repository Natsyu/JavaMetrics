package pl.edu.pwr.master.metrics.fe;

import com.github.javaparser.ast.CompilationUnit;
import pl.edu.pwr.master.core.MethodMetric;
import pl.edu.pwr.master.core.MethodMetricStrategy;
import pl.edu.pwr.master.core.model.Metric;
import pl.edu.pwr.master.metrics.visitors.FefVisitor;

import java.util.List;
import java.util.function.Supplier;

public class FefMetric<T extends FefVisitor> extends MethodMetricStrategy<Double> {
    private final String METRIC_NAME;
    private final float w, x;
    private static float W = 0.5f, X = 0.5f;
    private final Supplier<T> supplier;

    public FefMetric(Supplier<T> supplier, String metricName) {
        this.supplier = supplier;
        this.METRIC_NAME = metricName;
        w = W;
        x = X;
    }

    public FefMetric(Supplier<T> supplier, String metricName, float w, float x) {
        this.supplier = supplier;
        this.METRIC_NAME = metricName;
        this.w = w;
        this.x = x;
    }

    public static void setW(float w) {
        FefMetric.W = w;
    }

    public static void setX(float x) {
        FefMetric.X = x;
    }

    @Override
    public List<Metric<Double>> compute(CompilationUnit compilationUnit) {

        int max = 0;
        MethodMetric<Double> fef = l -> {
            T fefVisitor = supplier.get();
            fefVisitor.visit(l, null);
            int m = fefVisitor.maxCalls();
            int n = fefVisitor.getCallsCount();
            return calculateEquation(m, n);
        };
        return getMetricForMethod(fef, compilationUnit);
    }

    @Override
    public String getName() {
        return METRIC_NAME + "_" + w + "_" + x;
    }


    /**
     * @param m number of calls on the object
     * @param n total number of calls on any objects defined or visible in the method
     * @return FEF(obj, mtd) = w(m / n) + (1 – w)(1 - x^m)
     */
    private Double calculateEquation(int m, int n) {
        return (w * ((double)m / (n + 0.1)) + (1 - w) * (1.d - Math.pow(x, m)));

    }
}