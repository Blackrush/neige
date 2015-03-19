package neige.lang.expr.cond;

import neige.lang.InterpContext;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.Expressions;
import neige.lang.expr.literal.ErrorExpression;
import neige.lang.expr.literal.NilExpression;

public final class WhileExpression extends Expression {
    private final Expression test;
    private final Expression body;

    public WhileExpression(Expression test, Expression body) {
        this.test = test;
        this.body = body;
    }

    public Expression getTest() {
        return test;
    }

    public Expression getBody() {
        return body;
    }

    @Override
    public Expression evaluate(InterpContext ctx) {
        InterpContext scope = ctx.newChild();

        Expression last = NilExpression.i;
        while (true) {
            Expression testExp = test.evaluate(scope);
            if (testExp instanceof ErrorExpression) {
                return testExp;
            }
            if (!Expressions.coerceToBoolean(testExp)) {
                break;
            }
            last = body.evaluate(scope);

            if (last instanceof ErrorExpression) {
                break;
            }
        }
        return last;
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitWhile(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitWhile(
                new WhileExpression(test.traverseVisit(visitor),
                                    body.traverseVisit(visitor)));
    }
}
