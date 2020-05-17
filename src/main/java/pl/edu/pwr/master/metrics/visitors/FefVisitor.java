package pl.edu.pwr.master.metrics.visitors;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public class FefVisitor extends VoidVisitorAdapter<Void> {
    private HashMap<Expression, Integer> calls = new HashMap<Expression, Integer>();
    private int callsCount = 0;
    private int selfCallsCount = 0;

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);

        if (n.getScope().isPresent()) {
            Expression key = n.getScope().get();
            if (calls.containsKey(key))
                calls.put(key, calls.get(key) + 1);
            else
                calls.put(key, 1);
        } else {
            selfCallsCount++;
        }
        System.out.println(n.getScope() + " - " + n.getName());
        callsCount++;
    }

    @Override
    public void visit(FieldAccessExpr n, Void arg){
        super.visit(n, arg);
        
        if(n.getScope().isNameExpr()){
            Expression key = n.getScope();
            if (calls.containsKey(key))
                calls.put(key, calls.get(key) + 1);
            else
                calls.put(key, 1);
        } else {
            selfCallsCount++;
        }

        System.out.println(n.getScope() + " - " + n.getName());
        callsCount++;
    }

    public int getCallsCount() {
        return callsCount;
    }

    public HashMap<Expression, Integer> getCalls() {
        return calls;
    }

    public int maxCalls() {
        int max = 0;
        for (Map.Entry<Expression, Integer> entry : calls.entrySet()) {
            max = max < entry.getValue() ? entry.getValue() : max;
        }
        return max;
    }
}