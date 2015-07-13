import org.clafer.ast.AstConstraint;

/**
 * IncorrectMembershipTestHelper
 *
 * @author Jordan A. Ross
 */
public class IncorrectMembershipTestHelper {
    public boolean hasSeenBoolArithmExpr;
    public AstConstraint constraint;

    IncorrectMembershipTestHelper(AstConstraint c) {
        this.hasSeenBoolArithmExpr = false;
        this.constraint = c;
    }
}
