package pl.edu.pwr.master.metrics.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FefVisitorStandard extends FefVisitor {

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);
        if (n.getScope().isPresent()) {
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