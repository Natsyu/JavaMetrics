package pl.edu.pwr.master.metrics.visitors;


import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public abstract class FefVisitor extends VoidVisitorAdapter<Void> {
    protected HashMap<String, Integer> calls = new HashMap<String, Integer>();
    protected int callsCount = 0;
    protected int selfCallsCount = 0;

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);
    }
    @Override
    public void visit(FieldAccessExpr n, Void arg){
        super.visit(n, arg);
    }

    public int getCallsCount() {
        return callsCount;
    }

    public HashMap<String, Integer> getCalls() {
        return calls;
    }

    public int maxCalls() {
        int max = 0;
        for (Map.Entry<String, Integer> entry : calls.entrySet()) {
            max = max < entry.getValue() ? entry.getValue() : max;
        }
        return max;
    }

    public void clearFields(){
        calls = new HashMap<String, Integer>();
        callsCount = 0;
        selfCallsCount = 0;
    }
}
