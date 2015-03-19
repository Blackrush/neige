package neige.lang.expr.binary.bool;

import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.Expressions;
import neige.lang.expr.binary.BinaryExpression;
import neige.lang.expr.literal.BoolExpression;

/**
 * Created by Hawk on 16/03/2015.
 */

public class OrExpression extends BinaryExpression {

    public OrExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }


    @Override
    public Expression evaluate(InterpContext ctx) {
        // || is a late-bound expression

        boolean left = Expressions.coerceToBoolean(getLhs().evaluate(ctx));
        if (left) {
            return BoolExpression.TRUE;
        }

        boolean right = Expressions.coerceToBoolean(getRhs().evaluate(ctx));
        return BoolExpression.of(right);
    }

    @Override
    protected Expression evaluate(InterpContext ctx, Expression lhs, Expression rhs) {
        throw new Error("never reached");
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitOr(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitOr(new OrExpression(getLhs().traverseVisit(visitor),
                                                getRhs().traverseVisit(visitor)));
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.OR;
    }
}