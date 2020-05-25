package pl.edu.pwr.master.metrics.visitors;

import com.github.javaparser.ast.expr.MethodCallExpr;

public class FefVisitorNoGetSet extends FefVisitor {

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);
        if (n.getScope().isPresent()
                && !n.getScope().toString().toLowerCase().contains("get")
                && !n.getScope().toString().toLowerCase().contains("set")) {
            String key = n.getScope().get().toString();
            if (key != null)
                if (calls.containsKey(key))
                    calls.put(key, calls.get(key) + 1);
                else
                    calls.put(key, 1);
        } else {
            selfCallsCount++;
        }
        callsCount++;
    }
}