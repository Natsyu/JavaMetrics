package pl.edu.pwr.master.metrics.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FefVisitorExperimental extends FefVisitor {
//    private HashMap<Expression, Integer> calls = new HashMap<Expression, Integer>();
//    private int callsCount = 0;
//    private int selfCallsCount = 0;

    @Override
    public void visit(MethodCallExpr n, Void arg) {

        if (n.getScope().isPresent()) {
            // Optional<Node> node = getScopeParentName(n.getScope().get());

//            if (node.isPresent()) {
//                key = getParentClass(node.get()).get().getName().toString();
//
//            }
            String key = null;
            key = getParentClass((Node) n).get().getName().toString();
            if (key != null)
                if (calls.containsKey(key))
                    calls.put(key, calls.get(key) + 1);
                else
                    calls.put(key, 1);
        } else {
            selfCallsCount++;
        }
        System.out.println(n.getScope() + " - " + n.getName());
        super.visit(n, arg);
        callsCount++;

    }

    protected Optional<ClassOrInterfaceDeclaration> getParentClass(Node node) {
        Optional<Node> parent = node.getParentNode();
        Optional<ClassOrInterfaceDeclaration> parentClass = parent
                .filter(p -> p instanceof ClassOrInterfaceDeclaration)
                .flatMap(p -> Optional.of((ClassOrInterfaceDeclaration) p));

        while (parentClass.isEmpty() && parent.isPresent()) {
            parent = parent.get().getParentNode();
            parentClass = parent
                    .filter(p -> p instanceof ClassOrInterfaceDeclaration)
                    .flatMap(p -> Optional.of((ClassOrInterfaceDeclaration) p));
        }

        return parentClass;
    }

//    public Optional<Node> getScopeParentName(Expression scope) {
//        if (scope.getParentNode().isPresent()) {
//            if (scope.getParentNode().get().getParentNode().isPresent()) {
//                return getNodeParentName(scope.getParentNode().get());
//            }
//        }
//        return null;
//    }
//
//    public Optional<Node> getNodeParentName(Node node) {
//        if (node.getParentNode().isPresent()) {
//            if (node.getParentNode().get().getParentNode().isPresent()) {
//                getNodeParentName(node.getParentNode().get());
//            }
//        }
//        return node.getParentNode();
//    }

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

    @Override
    public void visit(FieldAccessExpr n, Void arg) {
        if (n.getScope().isNameExpr()) {
//            Optional<Node> node = getScopeParentName(n.getScope());

            String key = null;
            key = getParentClass((Node) n).get().getName().toString();
            if (key != null)
                if (calls.containsKey(key))
                    calls.put(key, calls.get(key) + 1);
                else
                    calls.put(key, 1);
        } else {
            selfCallsCount++;
        }

        System.out.println(n.getScope() + " - " + n.getName());
        super.visit(n, arg);
        callsCount++;

    }
}