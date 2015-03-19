package neige.lang.expr.unary;

import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.literal.BoolExpression;

/**
 * Created by Hawk on 16/03/2015.
 */

public class NotExpression extends UnaryExpression {

    public NotExpression(Expression expression) { super(expression);}

    @Override
    protected Expression evaluate(InterpContext ctx, Expression expression) {
        if ( expression instanceof BoolExpression) {
            Boolean exp = ((BoolExpression) expression ).getValue();
            return !exp ? BoolExpression.TRUE : BoolExpression.FALSE;
        }

        // TODO AddExpression#evaluate
        throw new Error("TODO AddExpression#evaluate:26");
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.NOT;
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitNot(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitNot(new NotExpression(getExpression().traverseVisit(visitor)));
    }
}