package neige.lang.expr;

import neige.lang.InterpContext;
import neige.lang.NeigeRuntimeException;

public final class TermExpression extends Expression {
    private final String value;

    public TermExpression(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Expression evaluate(InterpContext ctx) {
        Expression found = ctx.get(value);
        if (found == null) {
            throw new NeigeRuntimeException("Unknown variable `%s'", value);
        }
        return found.evaluate(ctx);
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitTerm(this);
    }
}
