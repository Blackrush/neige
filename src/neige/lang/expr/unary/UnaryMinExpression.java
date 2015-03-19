package neige.lang.expr.unary;

import neige.lang.InterpContext;
import neige.lang.NeigeRuntimeException;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.literal.FloatExpression;
import neige.lang.expr.literal.IntExpression;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UnaryMinExpression extends UnaryExpression {
    public UnaryMinExpression(Expression expression) {
        super(expression);
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.ADD;
    }

    @Override
    protected Expression evaluate(InterpContext ctx, Expression expression) {
        if (expression instanceof IntExpression) {
            BigInteger value = ((IntExpression) expression).getValue();
            return new IntExpression(value.negate());
        }

        if (expression instanceof FloatExpression) {
            BigDecimal value = ((FloatExpression) expression).getValue();
            return new FloatExpression(value.negate());
        }

        throw new NeigeRuntimeException("you cannot use an unary minus on %s", expression.getReflectiveName());
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitUnaryMin(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitUnaryMin(
                new UnaryMinExpression(
                        getExpression().traverseVisit(visitor)));
    }
}
