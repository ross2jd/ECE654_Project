import org.clafer.ast.AstClafer;
import org.clafer.ast.AstConstraint;

import java.util.HashMap;

/**
 * MissingImpliesHelper
 *
 * @author Jordan A. Ross
 */
public class MissingImpliesHelper {
    public boolean hasSeenBoolArithmExpr;
    private HashMap<String, AstClafer> clafersWithImplication;
    public AstConstraint constraint;

    MissingImpliesHelper(AstConstraint c) {
        this.hasSeenBoolArithmExpr = false;
        this.constraint = c;
        this.clafersWithImplication = new HashMap<>();
    }

    public void addClaferToImplicationMap(AstClafer clafer) {
        clafersWithImplication.put(clafer.getName(), clafer);
    }

    public boolean constraintContainsImplicationOnClafer(AstClafer clafer) {
        return clafersWithImplication.containsKey(clafer.getName());
    }
}
