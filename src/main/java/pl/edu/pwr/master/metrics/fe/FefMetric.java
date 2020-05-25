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
    private static float w = 0.5f, x = 0.5f;
    private final Supplier<T> supplier;

    public FefMetric(Supplier<T> supplier, String metricName) {
        this.supplier = supplier;
        this.METRIC_NAME = metricName;
    }

    public static void setW(float w) {
        FefMetric.w = w;
    }

    public static void setX(float x) {
        FefMetric.x = x;
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
        return METRIC_NAME;
    }


    /**
     * @param m number of calls on the object
     * @param n total number of calls on any objects defined or visible in the method
     * @return FEF(obj, mtd) = w(m / n) + (1 – w)(1 - x^m)
     */
    private Double calculateEquation(int m, int n) {
        return (w * ((double)m / (n + 1)) + (1 - w) * (1 - Math.pow(x, m)));

    }
}