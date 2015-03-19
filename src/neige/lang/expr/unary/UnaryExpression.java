package neige.lang.expr.unary;

import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.literal.ErrorExpression;

public abstract class UnaryExpression extends Expression {
    private final Expression expression;

    protected UnaryExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    protected abstract Expression evaluate(InterpContext ctx, Expression expression);

    public abstract Token.Static getToken();

    @Override
    public Expression evaluate(InterpContext ctx) {
        Expression exp = expression.evaluate(ctx);
        if (exp instanceof ErrorExpression) {
            return exp;
        }
        return evaluate(ctx, exp);
    }
}
