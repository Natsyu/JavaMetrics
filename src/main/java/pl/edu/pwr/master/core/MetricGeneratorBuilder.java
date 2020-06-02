package pl.edu.pwr.master.core;

import pl.edu.pwr.master.metrics.ck.*;
import pl.edu.pwr.master.metrics.fe.FefMetric;
import pl.edu.pwr.master.metrics.general.*;
import pl.edu.pwr.master.metrics.martin.CaMetric;
import pl.edu.pwr.master.metrics.martin.CeMetric;
import pl.edu.pwr.master.metrics.qmood.*;
import pl.edu.pwr.master.metrics.visitors.*;

import java.util.ArrayList;
import java.util.List;

public class MetricGeneratorBuilder {

    private static final List<MetricStrategy> STRATEGIES_WITHOUT_DEPENDENCY_RESOLUTION =
            new ArrayList<>(List.of(
                    new CycloMetric(),
                    new LdMetric(),
                    new LocMethodMetric(),
                    new LocClassMetric(),
                    new MrdMetric(),
                    new NoamMetric(),
                    new NolcMetric(),
                    new NolmMetric(),
                    new NomrcMetric(),
                    new NomrmMetric(),
                    new NomMetric(),
                    new NommMetric(),
                    new NopaMetric(),
                    new NopvMetric(),
                    new WmcMetric(),
                    new WmcnammMetric(),
                    new WocMetric(),
                    new NpmMetric(),
                    new FefMetric<FefVisitor>(FefVisitorStandard::new, "FEF_Standard"),
                    new FefMetric<FefVisitor>(FefVisitorWithFields::new, "FEF_Fields"),
                    new FefMetric<FefVisitor>(FefVisitorNoGetSet::new, "FEF_NoGetSet"),
                    new FefMetric<FefVisitor>(FefVisitorAtr::new, "FEF_Atr"),
                    new FefMetric<FefVisitor>(FefVisitorExperimental::new, "FEF_Experimental")
            )
            );

    private static final List<MetricStrategy> STRATEGIES_WITH_DEPENDENCY_RESOLUTION =
            new ArrayList<>(List.of(
                    new AtfdMetric(),
                    new NocMetric(),
                    new DitMetric(),
                    new RfcMetric(),
                    new CboMetric(),
                    new LcomMetric(),
                    new CbomzMetric(),
                    new DamMetric(),
                    new MoaMetric(),
                    new MfaMetric(),
                    new CamMetric(),
                    new CeMetric(),
                    new CaMetric()
            )
            );

    public static List<MetricStrategy> generateFefMetric(){
        List<MetricStrategy> fefMetrics = new ArrayList<>();
        for (int w = 1; w <= 10; w++) {
            for (int x = 1; x <= 10; x++) {
                float W = w / 10f;
                float X = x / 10f;
                fefMetrics.add(new FefMetric<FefVisitor>(FefVisitorStandard::new, "FEF_Standard", W, X));
                fefMetrics.add(new FefMetric<FefVisitor>(FefVisitorWithFields::new, "FEF_Fields", W, X));
                fefMetrics.add(new FefMetric<FefVisitor>(FefVisitorNoGetSet::new, "FEF_NoGetSet", W, X));
                fefMetrics.add(new FefMetric<FefVisitor>(FefVisitorAtr::new, "FEF_Atr", W, X));
                fefMetrics.add(new FefMetric<FefVisitor>(FefVisitorExperimental::new, "FEF_Experimental", W, X));
            }
        }
        return fefMetrics;
    }

    private static boolean FEF_ONLY = false;

    public static void setFefOnly(boolean fefOnly){
        FEF_ONLY = fefOnly;
    }

    private List<MetricStrategy> strategies;

    public MetricGeneratorBuilder() {
        strategies = new ArrayList<>();
    }

    public MetricGeneratorBuilder addMetric(MetricStrategy strategy) {
        if (!containsMetric(strategy))
            strategies.add(strategy);
        return this;
    }

    public MetricGeneratorBuilder addMetricsWithoutDependencyResolution() {
        STRATEGIES_WITHOUT_DEPENDENCY_RESOLUTION.forEach(s -> {
            if (!containsMetric(s))
                strategies.add(s);
        });
        return this;
    }

    public MetricGeneratorBuilder addMetricsWithDependencyResolution() {
        STRATEGIES_WITH_DEPENDENCY_RESOLUTION.forEach(s -> {
            if (!containsMetric(s))
                strategies.add(s);
        });
        return this;
    }

    public MetricGeneratorBuilder addFefMetricWithAllWAndX(){
        generateFefMetric().forEach(s -> {
            if (!containsMetric(s))
                strategies.add(s);
        });
        return this;
    }

    public MetricGenerator build() {
        return new MetricGenerator(strategies);
    }

    private boolean containsMetric(MetricStrategy metricStrategy) {
        return strategies.stream().anyMatch(s -> s.getName().equals(metricStrategy.getName()));
    }

    public static List<String> getAllMetricNames() {
        List<String> list = new ArrayList<>();

        if(FEF_ONLY) {
            generateFefMetric().forEach(s -> list.add(s.getName()));
        } else {
            STRATEGIES_WITHOUT_DEPENDENCY_RESOLUTION.forEach(s -> list.add(s.getName()));
            STRATEGIES_WITH_DEPENDENCY_RESOLUTION.forEach(s -> list.add(s.getName()));
        }

        return list;
    }
}
