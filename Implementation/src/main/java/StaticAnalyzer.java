import org.clafer.ast.*;
import org.clafer.collection.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * StaticAnalyzer
 *
 * @author Jordan A. Ross
 */
public class StaticAnalyzer {
    private AstModel model;

    public StaticAnalyzer(AstModel m) {
        this.model = m;
    }

    public void analyzeUnconstrainedRefClafers() {
        HashMap<AstAbstractClafer, List<AstConcreteClafer>> abstractMap = new HashMap<>();

        // Find all abstract clafers in the model in the order that all subclafers appear before the super clafers
        List<AstAbstractClafer> abstractClaferList = AstUtil.getAbstractClafersInSubOrder(this.model);
        ListIterator<AstAbstractClafer> li = abstractClaferList.listIterator(abstractClaferList.size());

        // Iterate through the list in reverse (so super clafers come before sub clafer). We are going to build up the
        // abstractMap that holds the list of concreteClafer children that have a reference for an abstract clafer. It
        // will only contain children who have not been constrained at the current abstract clafer or any of its super
        // Clafers.
        while (li.hasPrevious()) {
            AstAbstractClafer curAbstract = li.previous();
//            System.out.println(curAbstract.getName());
            if (curAbstract.getName().equals("#clafer#")) {
                // Skip the root
                continue;
            }
            if (!curAbstract.hasSuperClafer() || curAbstract.getSuperClafer().getName().equals("#clafer#")) {
                // This abstract has a super clafer so we should use get its list of unconstrained concrete clafers.
                List<AstConcreteClafer> tmpList = new ArrayList<>();
                List<AstConcreteClafer> tmpConcretes = findUnconstrainedRefClafersOfAbstract(curAbstract, tmpList);
                abstractMap.put(curAbstract, tmpConcretes);
            } else {
                if (abstractMap.get(curAbstract.getSuperClafer()) == null) {
                    List<AstConcreteClafer> tmpList = new ArrayList<>();
                    List<AstConcreteClafer> tmpConcretes = findUnconstrainedRefClafersOfAbstract(curAbstract, tmpList);
                    abstractMap.put(curAbstract, tmpConcretes);
                } else {
                    List<AstConcreteClafer> tmpList = new ArrayList<>(abstractMap.get(curAbstract.getSuperClafer()));
                    List<AstConcreteClafer> tmpConcretes = findUnconstrainedRefClafersOfAbstract(curAbstract,
                            tmpList);
                    abstractMap.put(curAbstract, tmpConcretes);
                }
            }
        }

        // Now we are going to loop through the list of abstract clafers again but this time we are going
        // to get all concrete clafers that are sub types of the abstract and perform the pattern match and analysis.
        for (AstAbstractClafer abstractClafer : abstractClaferList) {
            if (abstractClafer.getName().equals("#clafer#")) {
                // Skip the root
                continue;
            }
            List<AstConcreteClafer> concreteSubClaferList = AstUtil.getConcreteSubs(abstractClafer);

            // Now we want to perform the analysis on a unconstrained Clafer basis so we first loop over the set
            // of unconstrained Clafers from the abstractClafer
            if (abstractMap.get(abstractClafer) != null) {
                for (AstConcreteClafer unconstrainedClafer : abstractMap.get(abstractClafer)) {
                    // Set up the warning logger to analyze and output the warnings later.
                    WarningLogger log = new WarningLogger(unconstrainedClafer,
                            WarningLogger.WarningType.MISSING_INHERIT_CONSTRAINT);

                    // For each unconstrained Clafer we want to analyze each of the concrete sub types to see if they
                    // constrain the Clafer
                    for (AstConcreteClafer concreteSubClafer : concreteSubClaferList) {
                        if (concreteSubClafer.getSuperClafer().equals(abstractClafer)) {
                            String allConstraint = "";
                            for (AstConstraint constraint : concreteSubClafer.getConstraints()) {
                                //TODO: Fix this... We are naively doing string compare for now.
                                allConstraint = allConstraint.concat(constraint.toString());
                            }
                            if (allConstraint.contains(unconstrainedClafer.getName())) {
                                log.incSuccess();
                            } else {
                                log.logError(concreteSubClafer);
                            }
                        }
                    }

                    log.printErrors();
                }
            }
        }


    }

    public void analyzeMissingRefInSetTestConstraint() {
        List<AstConstraint> constraintList = AstUtil.getNestedConstraints(this.model);
        MissingRefExprAnalyzer analyzer = new MissingRefExprAnalyzer(this.model);
        for (AstConstraint constraint : constraintList) {
            MissingRefHelper helper = new MissingRefHelper(constraint);
            constraint.getExpr().accept(analyzer, helper);
        }
//        List<AstConstraint> constraintList = AstUtil.getNestedConstraints(this.model);
//        // Get all AstSetTest expressions
//        List<Pair<AstSetTest, AstConstraint>> setExpressionConstraintList = new ArrayList<>();
//        for (AstConstraint constraint : constraintList) {
//            AstExpr expression = constraint.getExpr();
//            if (expression instanceof AstSetTest) {
//                setExpressionConstraintList.add(new Pair<>((AstSetTest) expression, constraint));
//            } else if (expression instanceof AstBoolArithm) {
//                AstBoolExpr[] operands = ((AstBoolArithm) expression).getOperands();
//                for (AstBoolExpr operand : operands) {
//                    if (operand instanceof AstSetTest) { //TODO: This does not seem robust..
//                        setExpressionConstraintList.add(new Pair<>((AstSetTest) operand, constraint));
//                    }
//                }
//            }
//        }
//
//        // Now that we have all the AstSetTest expressions we want to check if the left and right expressions have
//        // Clafers with references and have the .ref
//        for (Pair<AstSetTest, AstConstraint> setExpressionConstraint : setExpressionConstraintList) {
//            AstSetTest expr = setExpressionConstraint.getFst();
//            AstConstraint constraint = setExpressionConstraint.getSnd();
//            AstSetExpr left = expr.getLeft();
//            AstSetExpr right = expr.getRight();
////            System.out.println("Getting left Clafer of " + left.toString() + " ...");
//            AstClafer leftClafer = getClaferFromExpression(left, setExpressionConstraint.getSnd().getContext());
////            System.out.println("Getting right Clafer of " + right.toString() + " ...");
//            AstClafer rightClafer = getClaferFromExpression(right, setExpressionConstraint.getSnd().getContext());
//            if (leftClafer != null && rightClafer != null) {
//                if (leftClafer.hasRef() && rightClafer.hasRef()) {
//                    if (!exprHasRef(left)) {
//                        System.out.println("Missing .ref in " + left.toString() + " in constraint: " +
//                                constraint.toString() + " in the context of " + constraint.getContext().toString());
//                    }
//                    if (!exprHasRef(right)) {
//                        System.out.println("Missing .ref in " + right.toString() + " in constraint: " +
//                                constraint.toString() + " in the context of " + constraint.getContext().toString());
//                    }
//                }
//            }
//        }
    }

    public void analyzeMissingImpliesOnOptionalClafer() {
        List<AstConstraint> constraintList = AstUtil.getNestedConstraints(this.model);
        MissingImpliesExprAnalyzer analyzer = new MissingImpliesExprAnalyzer(this.model);
        for (AstConstraint constraint : constraintList) {
            MissingImpliesHelper helper = new MissingImpliesHelper(constraint);
            constraint.getExpr().accept(analyzer, helper);
        }
    }

    public void analyzeIncorrectMembershipTest() {
        List<AstConstraint> constraintList = AstUtil.getNestedConstraints(this.model);
        IncorrectMembershipTestExprAnalyzer analyzer = new IncorrectMembershipTestExprAnalyzer(this.model);
        for (AstConstraint constraint : constraintList) {
            IncorrectMembershipTestHelper helper = new IncorrectMembershipTestHelper(constraint);
            constraint.getExpr().accept(analyzer, helper);
        }
    }

    private List<AstConcreteClafer> findUnconstrainedRefClafersOfAbstract(AstAbstractClafer abstractClafer,
                                                                          List<AstConcreteClafer> S) {
        String allConstraint = "";
        for (AstConstraint constraint : abstractClafer.getConstraints()) {
            //TODO: Fix this... We are naively doing string compare for now.
            allConstraint = allConstraint.concat(constraint.toString());
        }
        // Remove any s in S in which there is a constraint in abstract Clafer.
        ListIterator<AstConcreteClafer> li = S.listIterator();
        while (li.hasNext()) {
            // if there exists a constraint on concreteClafer in abstractClafer
            if (allConstraint.contains(li.next().getName())) {
                // remove concreteClafer from unconstrainedClaferList
                li.remove();
            }
        }

        // Add any child of abstractClafer that is not constrained and has a reference in the abstract
        for (AstConcreteClafer child : abstractClafer.getChildren()) {
            if (!allConstraint.contains(child.getName()) && child.hasRef()) {
                S.add(child);
            }
        }

        return S;
    }
}
