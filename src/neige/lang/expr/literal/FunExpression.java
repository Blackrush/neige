package neige.lang.expr.literal;

import neige.lang.InterpContext;
import neige.lang.expr.Expression;

import java.util.List;

public abstract class FunExpression extends LiteralExpression {
    public abstract Expression call(InterpContext ctx, List<Expression> arguments);
}
