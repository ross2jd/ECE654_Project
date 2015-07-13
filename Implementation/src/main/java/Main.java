import org.clafer.ast.AstAbstractClafer;
import org.clafer.collection.Triple;
import org.clafer.javascript.Javascript;
import org.clafer.ast.AstModel;
import org.clafer.objective.Objective;
import org.clafer.scope.Scope;

import java.io.File;
import java.util.List;

/**
 * Main
 *
 * @author Jordan A. Ross
 *
 * The main class for the Clafer Choco Static Analysis tool
 */
public class Main {
    public static void main(String[] args) throws Exception {
        File inputFile = new File(args[0]);
        Triple<AstModel, Scope, Objective[]> modelTriple = Javascript.readModel(inputFile);

        AstModel model = modelTriple.getFst();

        // Create the StaticAnalyzer object
        StaticAnalyzer sa = new StaticAnalyzer(model);

        // Execute pattern 1 analysis
        sa.analyzeUnconstrainedRefClafers();

        // Execute pattern 2 analysis
        sa.analyzeMissingRefInSetTestConstraint();

        // Execute pattern 3 analysis
        sa.analyzeMissingImpliesOnOptionalClafer();

        // Execute pattern 4 analysis
        sa.analyzeIncorrectMembershipTest();

    }
}
