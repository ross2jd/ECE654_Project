import org.clafer.ast.AstConcreteClafer;

import java.util.ArrayList;
import java.util.List;

/**
 * WarningLogger
 */
public class WarningLogger {

    public enum WarningType {MISSING_INHERIT_CONSTRAINT}

    private Integer successCount;
    private Integer errorCount;
    private AstConcreteClafer context;
    private List<AstConcreteClafer> violators;
    private WarningType type;

    public WarningLogger(AstConcreteClafer ctx, WarningType t) {
        this.successCount = 0;
        this.errorCount = 0;
        this.violators = new ArrayList<>();
        this.context = ctx;
        this.type = t;
    }

    public void incSuccess() {
        this.successCount++;
    }

    public void logError(AstConcreteClafer clafer) {
        this.violators.add(clafer);
        this.errorCount++;
    }

    public void printErrors() {
        if (this.successCount > 0) {
            switch (this.type) {
                case MISSING_INHERIT_CONSTRAINT:
                    for (AstConcreteClafer v : violators) {
                        System.out.println("Possible missing constraint on " + this.context.getName() +
                                " from inherited Clafer in " + v.getName());
                    }
            }
        }

    }
}
