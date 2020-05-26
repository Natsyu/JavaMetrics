package pl.edu.pwr.master.metrics.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FefVisitorExperimental extends FefVisitor {

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);
        if (n.getScope().isPresent()) {
            String key = n.getScope().get().toString();

            AddMethodArguments(n.getArguments());
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

    @Override
    public void visit(ObjectCreationExpr n, Void arg){
        AddMethodArguments(n.getArguments());
        callsCount++;
    }

    @Override
    public void visit(FieldAccessExpr n, Void arg) {
        super.visit(n, arg);
        if (n.getScope().isNameExpr()) {
            String key = n.getScope().toString();
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

    private void AddMethodArguments(NodeList<Expression> arguments) {
        List<Optional<NameExpr>> args = arguments.stream().map(Expression::toNameExpr).collect(Collectors.toList());
        if (args.size() > 0) {
            for (Optional<NameExpr> name : args) {
                String obj = name.map(NodeWithSimpleName::getNameAsString).orElse(null);
                if (obj != null) {
                    if (calls.containsKey(obj))
                        calls.put(obj, calls.get(obj) + 1);
                    else
                        calls.put(obj, 1);
                }
            }
        }
    }
}