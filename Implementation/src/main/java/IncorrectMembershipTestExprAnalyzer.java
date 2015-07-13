import org.clafer.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete visitor to detect when we may have restricted variability in an expression.
 */
public class IncorrectMembershipTestExprAnalyzer implements AstExprVisitor<IncorrectMembershipTestHelper, Boolean> {

    AstModel model;

    IncorrectMembershipTestExprAnalyzer(AstModel m) {
        this.model = m;
    }

    public Boolean visit(AstThis ast, IncorrectMembershipTestHelper h) {
        return false;
    }

    public Boolean visit(AstGlobal ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstConstant ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstStringConstant ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstJoin ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        AstSetExpr left = ast.getLeft();
        AstSetExpr right = ast.getRight();
        return (left.accept(this, h) || right.accept(this, h));
    }

    public Boolean visit(AstJoinParent ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return ast.getChildren().accept(this, h);
    }

    public Boolean visit(AstJoinRef ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstNot ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstMinus ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstCard ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstSetTest ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        // In the set test expression we want to see if:
        //  the left expression is a clafer with cardinality 1 or less
        AstSetExpr left = ast.getLeft();
        AstClafer leftClafer = this.getClaferFromExpression(left, h.constraint.getContext());
        //  the right expression is a union of multiple clafer
        boolean rightIsSet = ast.getRight().accept(this, h);
        if (leftClafer != null) {
            if (leftClafer.getClass() == AstConcreteClafer.class) {
                AstConcreteClafer concLeftClafer = (AstConcreteClafer) leftClafer;
                if ((concLeftClafer.getCard().getLow() <= 1) && rightIsSet) {
                    // We have an error
                    System.out.println("Warning: Possible incorrect use of '=' (consider using 'in') in expression " + ast.toString() + " in " +
                            "the constraint: " + h.constraint.toString());
                }
            }
        }


        return false;
    }

    public Boolean visit(AstCompare ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstArithm ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstMod ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstSum ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstProduct ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstBoolArithm ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        AstBoolExpr[] operands = ast.getOperands();
        for (AstBoolExpr operand : operands) {
            operand.accept(this, h);
        }
        return false;
    }

    public Boolean visit(AstDifference ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstIntersection ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstUnion ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return true;
    }

    public Boolean visit(AstMembership ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstTernary ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstIfThenElse ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstDowncast ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstUpcast ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstLocal ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstQuantify ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstLength ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstConcat ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstPrefix ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstSuffix ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstChildRelation ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstParentRelation ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstRefRelation ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstInverse ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
    }

    public Boolean visit(AstTransitiveClosure ast, IncorrectMembershipTestHelper h) {
//        System.out.println("In visit of: " + ast.toString());
        return false;
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
}
