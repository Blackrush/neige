package neige.lang.expr.binary.bool;

import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.binary.BinaryExpression;
import neige.lang.expr.literal.BoolExpression;
import neige.lang.expr.literal.LiteralExpression;

/**
 * Created by Hawk on 16/03/2015.
 */
public class EQExpression extends BinaryExpression {

    public EQExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    protected Expression evaluate(InterpContext ctx, Expression lhs, Expression rhs) {
        if (!(lhs instanceof LiteralExpression) || !(rhs instanceof LiteralExpression)) {
            // TODO EQExpression#evaluate
            throw new Error("TODO EQExpression#evaluate:29");
        }

        Object left = ((LiteralExpression) lhs).getValue();
        Object right = ((LiteralExpression) rhs).getValue();
        return BoolExpression.of(left.equals(right));
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitEQ(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitEQ(new EQExpression(getLhs().traverseVisit(visitor),
                                                getRhs().traverseVisit(visitor)));
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.EQ;
    }
}
