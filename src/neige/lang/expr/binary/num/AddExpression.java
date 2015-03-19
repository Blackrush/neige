package neige.lang.expr.binary.num;

import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.Expressions;
import neige.lang.expr.binary.BinaryExpression;
import neige.lang.expr.literal.FloatExpression;
import neige.lang.expr.literal.IntExpression;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AddExpression extends BinaryExpression {
    public AddExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.ADD;
    }

    @Override
    protected Expression evaluate(InterpContext ctx, Expression lhs, Expression rhs) {
        if (lhs instanceof IntExpression && rhs instanceof IntExpression) {
            BigInteger left = ((IntExpression) lhs).getValue();
            BigInteger right = ((IntExpression) rhs).getValue();
            return new IntExpression(left.add(right));
        }

        BigDecimal left = Expressions.coerceToDecimal(lhs);
        BigDecimal right = Expressions.coerceToDecimal(rhs);
        return new FloatExpression(left.add(right, ctx.getMathContext()));
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitAdd(new AddExpression(getLhs().traverseVisit(visitor),
                                                  getRhs().traverseVisit(visitor)));
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitAdd(this);
    }
}
