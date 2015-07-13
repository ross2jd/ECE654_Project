import org.clafer.ast.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Utils
 *
 * @author Jordan A. Ross (Adapted from Alexander Murashkin's code in ClaferChocoIG)
 */
public class Utils {

    public static AstClafer getClaferFromName(String name, AstClafer parent, AstModel model) {
        if (parent == null) {
            // We have a top level clafer
            for (AstClafer clafer : AstUtil.getClafers(model)) {
                if (AstUtil.isTop(clafer)) {
                    if (name.equals(clafer.getName())) {
                        return clafer;
                    }
                }
            }
        } else {
            for (AstClafer clafer : AstUtil.getConcreteClafers(model)) {
                if (name.equals(clafer.getName())) {
                    return clafer;
                }
            }
        }
        return null;
    }
}
