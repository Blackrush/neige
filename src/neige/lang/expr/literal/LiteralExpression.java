package neige.lang.expr.literal;

import neige.lang.InterpContext;
import neige.lang.expr.Expression;

public abstract class LiteralExpression extends Expression {
    @Override
    public Expression evaluate(InterpContext ctx) {
        return this;
    }

    public abstract Object getValue();

    public String show() {
        return getValue().toString();
    }
}
