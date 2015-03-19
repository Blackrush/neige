package neige.lang.expr.binary.num;

import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.Expressions;
import neige.lang.expr.binary.BinaryExpression;
import neige.lang.expr.literal.BoolExpression;
import neige.lang.expr.literal.IntExpression;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Hawk on 16/03/2015.
 */
public class MoreEQExpression extends BinaryExpression {

    public MoreEQExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.MORE_EQ;
    }

    @Override
    protected Expression evaluate(InterpContext ctx, Expression lhs, Expression rhs) {
        int res;

        if (lhs instanceof IntExpression && rhs instanceof IntExpression) {
            BigInteger left = ((IntExpression) lhs).getValue();
            BigInteger right = ((IntExpression) rhs).getValue();
            res = left.compareTo(right);
        } else {
            BigDecimal left = Expressions.coerceToDecimal(lhs);
            BigDecimal right = Expressions.coerceToDecimal(rhs);
            res = left.compareTo(right);
        }

        return BoolExpression.of(res >= 0);
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitMoreEQ(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitMoreEQ(new MoreEQExpression(getLhs().traverseVisit(visitor),
                                                        getRhs().traverseVisit(visitor)));
    }
}