package neige.lang.expr.cond;

import neige.lang.InterpContext;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.Expressions;
import neige.lang.expr.literal.ErrorExpression;

public final class IfExpression extends Expression {
    private final Expression test;
    private final Expression body;
    private final Expression otherwise;

    public IfExpression(Expression test, Expression body, Expression otherwise) {
        this.test = test;
        this.body = body;
        this.otherwise = otherwise;
    }

    public Expression getTest() {
        return test;
    }

    public Expression getBody() {
        return body;
    }

    public Expression getOtherwise() {
        return otherwise;
    }

    @Override
    public Expression evaluate(InterpContext ctx) {
        Expression testExp = test.evaluate(ctx);
        if (testExp instanceof ErrorExpression) {
            return testExp;
        }

        if (Expressions.coerceToBoolean(testExp)) {
            return body.evaluate(ctx);
        } else {
            return otherwise.evaluate(ctx);
        }
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitIf(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitIf(new IfExpression(
                test.traverseVisit(visitor),
                body.traverseVisit(visitor),
                otherwise.traverseVisit(visitor)));
    }
}
