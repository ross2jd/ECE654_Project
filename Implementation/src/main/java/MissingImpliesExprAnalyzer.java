import org.clafer.ast.*;
import org.clafer.collection.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * MissingImpliesExprAnalyzer
 *
 * @author Jordan A. Ross
 */
public class MissingImpliesExprAnalyzer implements AstExprVisitor<MissingImpliesHelper, Boolean> {

    AstModel model;

    MissingImpliesExprAnalyzer(AstModel m) {
        this.model = m;
    }

    public Boolean visit(AstThis ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstGlobal ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstConstant ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstStringConstant ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstJoin ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        AstSetExpr left = ast.getLeft();
        AstSetExpr right = ast.getRight();
        return (left.accept(this, h) || right.accept(this, h));
    }

    public Boolean visit(AstJoinParent ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return ast.getChildren().accept(this, h);
    }

    public Boolean visit(AstJoinRef ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstNot ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstMinus ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstCard ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        AstClafer clafer = this.getClaferFromExpression(ast.getSet(), h.constraint.getContext());
        if (clafer != null) {
            h.addClaferToImplicationMap(clafer);
        }
        return false;
    }

    public Boolean visit(AstSetTest ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        // In the set test expression we want to analyze for missing implies.
        AstSetExpr left = ast.getLeft();
        if (this.isExprThatShouldBeAccepted(left)) {
            left.accept(this, h);
            return false;
        }

        // If its not an expression that should be accepted then we should analyze it for missing implies
        return this.analyzeMissingImpliesOnExpression(left, h);
    }

    public Boolean visit(AstCompare ast, MissingImpliesHelper h) {
        // When we have implication we have a compare to test that the clafer is greater than or equal to some number
        // (i.e. 1)
//        System.out.println("In visit of: " + ast.toString());
        AstSetExpr left = ast.getLeft();
        if (this.isExprThatShouldBeAccepted(left)) {
            left.accept(this, h);
            return false;
        }

        // If its not an expression that should be accepted then we should analyze it for missing implies
        return this.analyzeMissingImpliesOnExpression(left, h);
    }

    public Boolean visit(AstArithm ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        boolean result = false;
        for (AstSetExpr operand : ast.getOperands()) {
            if (isExprThatShouldBeAccepted(operand)) {
                result = result || operand.accept(this, h);
            }
            result = result || this.analyzeMissingImpliesOnExpression(operand, h);
        }
        return result;
    }

    public Boolean visit(AstMod ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstSum ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstProduct ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstBoolArithm ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        AstBoolExpr[] operands = ast.getOperands();
        h.hasSeenBoolArithmExpr = true;
        for (AstBoolExpr operand : operands) {
            operand.accept(this, h);
        }
        return false;
    }

    public Boolean visit(AstDifference ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstIntersection ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstUnion ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return true;
    }

    public Boolean visit(AstMembership ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstTernary ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstIfThenElse ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstDowncast ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstUpcast ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstLocal ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstQuantify ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstLength ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstConcat ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstPrefix ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstSuffix ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstChildRelation ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstParentRelation ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstRefRelation ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstInverse ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstTransitiveClosure ast, MissingImpliesHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    private Pair<AstClafer,AstClafer> getParentChildClaferFromExpression(AstSetExpr expr, AstClafer context) {
        // Get the array of Clafer names in order of top to lowest level clafer.
        String[] claferNames = expr.toString().split("\\s\\.\\s");
        List<AstClafer> claferList = new ArrayList<>();
        int index = 0;
        for (String claferName : claferNames) {
            if (claferName.equals("ref")) {
                // Skip ".ref" but increment
                index++;
                continue;
            }
            if (claferName.equals("this")) {
                if (index+1 < claferNames.length) {
                    if (claferNames[index+1].equals("ref")) {
                        // If we are getting the reference of the context then retrieve it
                        claferList.add(context.getRef().getTargetType());
                    } else {
                        // Replace "this" with the context
                        claferList.add(context);
                    }
                } else {
                    // Replace "this" with the context
                    claferList.add(context);
                }
            } else {
                AstClafer clafer;
                if (claferList.size() > 0) {
                    clafer = Utils.getClaferFromName(claferName, claferList.get(claferList.size() - 1),
                            this.model);
                } else {
                    clafer = Utils.getClaferFromName(claferName, null, this.model);
                }
                if (index+1 < claferNames.length && clafer != null) {
                    if (claferNames[index+1].equals("ref")) {
                        // If we are getting the reference of the clafer then retrieve it
                        if (clafer.getRef() != null) {
                            claferList.add(clafer.getRef().getTargetType());
                        }
                    } else {
                        // If we are not getting the reference then just add it
                        claferList.add(clafer);
                    }
                } else {
                    // If this is the last clafer then just add it
                    claferList.add(clafer);
                }
            }
            index++;
        }
        if (claferList.get(claferList.size()-1) == null) {
            return null;
        }
        if (claferList.size() - 2 > 0) {
            if (claferList.get(claferList.size() - 2) == null || claferList.get(claferList.size() - 1) == null) {
                return null;
            } else {
                return new Pair<>(claferList.get(claferList.size() - 2), claferList.get(claferList.size() - 1));
            }
        } else {
            if (claferList.get(claferList.size() - 1) == null) {
                return new Pair<>(claferList.get(claferList.size() - 1), claferList.get(claferList.size() - 1));
            } else {
                return null;
            }
        }

    }

    private AstClafer getClaferFromExpression(AstSetExpr expr, AstClafer context) {
        // Get the array of Clafer names in order of top to lowest level clafer.
        String[] claferNames = expr.toString().split("\\s\\.\\s");
        List<AstClafer> claferList = new ArrayList<>();
        int index = 0;
        for (String claferName : claferNames) {
            if (claferName.equals("ref")) {
                // Skip ".ref" but increment
                index++;
                continue;
            }
            if (claferName.equals("this")) {
                if (index+1 < claferNames.length) {
                    if (claferNames[index+1].equals("ref")) {
                        // If we are getting the reference of the context then retrieve it
                        claferList.add(context.getRef().getTargetType());
                    } else {
                        // Replace "this" with the context
                        claferList.add(context);
                    }
                } else {
                    // Replace "this" with the context
                    claferList.add(context);
                }
            } else {
                AstClafer clafer;
                if (claferList.size() > 0) {
                    clafer = Utils.getClaferFromName(claferName, claferList.get(claferList.size() - 1),
                            this.model);
                } else {
                    clafer = Utils.getClaferFromName(claferName, null, this.model);
                }
                if (index+1 < claferNames.length && clafer != null) {
                    if (claferNames[index+1].equals("ref")) {
                        // If we are getting the reference of the clafer then retrieve it
                        if (clafer.getRef() != null) {
                            claferList.add(clafer.getRef().getTargetType());
                        }
                    } else {
                        // If we are not getting the reference then just add it
                        claferList.add(clafer);
                    }
                } else {
                    // If this is the last clafer then just add it
                    claferList.add(clafer);
                }
            }
            index++;
        }
        return  claferList.get(claferList.size()-1);
    }

    private AstConcreteClafer getClosestParentWithOptionalCard(AstConcreteClafer child) {
        // We should return the Clafer if it is possibly optional.
        if (!child.getCard().hasLow()) {
            return child;
        }

        if (child.hasParent()) {
            if (child.getParent().getClass() == AstConcreteClafer.class) {
                return getClosestParentWithOptionalCard((AstConcreteClafer) child.getParent());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private boolean analyzeMissingImpliesOnExpression(AstSetExpr expr, MissingImpliesHelper h) {
        // We want to see if:
        //  the expression is clafer that has a parent with optional cardinality (i.e.
        //  the lower bound of the card is 0.
        // We also want to know if we have had a boolean implies on that parent clafer.
        Pair<AstClafer, AstClafer> parentChildPair = this.getParentChildClaferFromExpression(expr, h.constraint.getContext());
        if (parentChildPair == null) {
            return false;
        }
        AstClafer parentClafer = parentChildPair.getFst();
        AstClafer childClafer = parentChildPair.getSnd();
        AstConcreteClafer optionalParentClafer;
        if (childClafer.isPrimitive()) {
            // If the child clafer is primitive we want to look at the parent (if the parent is not the child).
            if (!parentClafer.equals(childClafer) || (parentClafer.getClass() == AstConcreteClafer.class)) {
                // First check to see if this parent is optional. If it is not then we will try to find the next closest
                // one.
                optionalParentClafer = (AstConcreteClafer) parentClafer;
                if (optionalParentClafer.getCard().hasLow())
                    optionalParentClafer = this.getClosestParentWithOptionalCard((AstConcreteClafer) parentClafer);
                else {
                    optionalParentClafer = (AstConcreteClafer) parentClafer;
                }
            } else {
                return false;
            }
        } else {
            if (childClafer.getClass() == AstConcreteClafer.class) {
                optionalParentClafer = this.getClosestParentWithOptionalCard((AstConcreteClafer) childClafer);
            } else {
                return false;
            }
        }
        if (optionalParentClafer == null) {
            return false;
        } else {
            // We have found a parent with optional cardinality so we should see if we have an corresponding implies.
            if (!h.hasSeenBoolArithmExpr) {
                // If we haven't see a boolean expression then throw an error because we for sure have not checked
                System.out.println("Warning: Possible missing implies in constraint: " + h.constraint.toString() + " in" +
                        " the context of " + h.constraint.getContext().toString() + ". An implies should exist on " +
                        "Clafer " + optionalParentClafer.toString() + " to keep variability unrestricted.");
                return true;
            } else {
                // Now lets check to see if we have an implies on this optional parent.
                if (h.constraintContainsImplicationOnClafer(optionalParentClafer)) {
                    return false;
                } else {
                    System.out.println("Warning: Possible missing implies in constraint: " + h.constraint.toString() + " in" +
                            " the context of " + h.constraint.getContext().toString() + ". An implies should exist on " +
                            "Clafer " + optionalParentClafer.toString() + " to keep variability unrestricted.");
                    return true;
                }
            }
        }
    }

    private boolean isExprThatShouldBeAccepted(AstSetExpr expr) {
        return expr.getClass() == AstArithm.class || expr.getClass() == AstCard.class;
    }
}
