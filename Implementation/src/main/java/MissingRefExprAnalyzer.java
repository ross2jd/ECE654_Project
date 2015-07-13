import org.clafer.ast.*;
import org.clafer.collection.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * MissingRefExprAnalyzer
 *
 * @author Jordan A. Ross
 */
public class MissingRefExprAnalyzer implements AstExprVisitor<MissingRefHelper, Boolean> {

    AstModel model;

    MissingRefExprAnalyzer(AstModel m) {
        this.model = m;
    }

    public Boolean visit(AstThis ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstGlobal ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstConstant ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstStringConstant ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstJoin ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        AstSetExpr left = ast.getLeft();
        AstSetExpr right = ast.getRight();
        return (left.accept(this, h) || right.accept(this, h));
    }

    public Boolean visit(AstJoinParent ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return ast.getChildren().accept(this, h);
    }

    public Boolean visit(AstJoinRef ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstNot ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstMinus ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstCard ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstSetTest ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        // In the set test expression we want to analyze if the left or right expression are reference clafers.
        AstSetExpr left = ast.getLeft();
        AstSetExpr right = ast.getRight();

        // We first want to make sure the AstSetTest expression can't be further developed into somthing interesting.
        if (isExprThatShouldBeAccepted(left)) {
            left.accept(this, h);
        }
        if (isExprThatShouldBeAccepted(right)) {
            right.accept(this, h);
        }


        AstClafer leftClafer = this.getClaferFromExpression(left, h.constraint.getContext());
        AstClafer rightClafer = this.getClaferFromExpression(right, h.constraint.getContext());
        if (leftClafer != null && rightClafer != null) {
            if (leftClafer.hasRef() && rightClafer.hasRef()) {
                if (!exprHasRef(left)) {
                    System.out.println("Missing .ref in " + left.toString() + " in constraint: " +
                            h.constraint.toString() + " in the context of " + h.constraint.getContext().toString());
                }
                if (!exprHasRef(right)) {
                    System.out.println("Missing .ref in " + right.toString() + " in constraint: " +
                            h.constraint.toString() + " in the context of " + h.constraint.getContext().toString());
                }
            }
        }
        return false;
    }

    public Boolean visit(AstCompare ast, MissingRefHelper h) {
        // When we have implication we have a compare to test that the clafer is greater than or equal to some number
        // (i.e. 1)
//        System.out.println("In visit of: " + ast.toString());
        AstSetExpr left = ast.getLeft();
        AstSetExpr right = ast.getRight();
        if (left != null) {
            left.accept(this, h);
        }
        if (right != null) {
            right.accept(this, h);
        }
        return false;
    }

    public Boolean visit(AstArithm ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        for (AstSetExpr operand : ast.getOperands()) {
            if (operand != null) {
                operand.accept(this, h);
            }
        }
        return false;
    }

    public Boolean visit(AstMod ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstSum ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstProduct ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstBoolArithm ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        AstBoolExpr[] operands = ast.getOperands();
        for (AstBoolExpr operand : operands) {
            if (operand != null) {
                operand.accept(this, h);
            }
        }
        return false;
    }

    public Boolean visit(AstDifference ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstIntersection ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstUnion ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return true;
    }

    public Boolean visit(AstMembership ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstTernary ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstIfThenElse ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstDowncast ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstUpcast ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstLocal ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstQuantify ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstLength ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstConcat ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstPrefix ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstSuffix ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstChildRelation ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstParentRelation ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstRefRelation ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstInverse ast, MissingRefHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstTransitiveClosure ast, MissingRefHelper h) {
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

    private boolean isExprThatShouldBeAccepted(AstSetExpr expr) {
        return expr.getClass() == AstArithm.class || expr.getClass() == AstCard.class;
    }

    private boolean exprHasRef(AstSetExpr expr) {
        String[] splitArray = expr.toString().split("\\s\\.\\s");
        return splitArray[splitArray.length-1].equals("ref");
    }
}
