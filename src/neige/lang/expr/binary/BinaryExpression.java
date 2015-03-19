package neige.lang.expr.binary;

import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.literal.ErrorExpression;

public abstract class BinaryExpression extends Expression {
    private final Expression lhs;
    private final Expression rhs;

    public BinaryExpression(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Expression getLhs() {
        return lhs;
    }

    public Expression getRhs() {
        return rhs;
    }

    protected abstract Expression evaluate(InterpContext ctx, Expression lhs, Expression rhs);

    public abstract Token.Static getToken();

    @Override
    public Expression evaluate(InterpContext ctx) {
        Expression left = lhs.evaluate(ctx);
        if (left instanceof ErrorExpression) {
            return left;
        }
        Expression right = rhs.evaluate(ctx);
        if (right instanceof ErrorExpression) {
            return right;
        }
        return evaluate(ctx, left, right);
    }
}
