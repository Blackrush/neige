package neige.lang.expr.unary;

import neige.lang.InterpContext;
import neige.lang.NeigeRuntimeException;
import neige.lang.ParseException;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.TermExpression;
import neige.lang.expr.literal.FloatExpression;
import neige.lang.expr.literal.IntExpression;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DecExpression extends UnaryExpression {
    public DecExpression(Expression expression) {
        super(expression);
        if (!(expression instanceof TermExpression)) {
            throw new ParseException("expected a term but got %s",
                                     expression.getReflectiveName());
        }
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.DEC;
    }

    @Override
    public Expression evaluate(InterpContext ctx) {
        TermExpression var = (TermExpression) getExpression();
        Expression exp = var.evaluate(ctx);

        if (exp instanceof IntExpression) {
            BigInteger val = ((IntExpression) exp).getValue();
            IntExpression result = new IntExpression(val.subtract(BigInteger.ONE));
            ctx.put(var.getValue(), result);
            return result;
        }

        if (exp instanceof FloatExpression) {
            BigDecimal val = ((FloatExpression) exp).getValue();
            FloatExpression result = new FloatExpression(val.subtract(BigDecimal.ONE));
            ctx.put(var.getValue(), result);
            return result;
        }

        throw new NeigeRuntimeException("you cannot decrement a %s", exp.getReflectiveName());
    }

    @Override
    protected Expression evaluate(InterpContext ctx, Expression expression) {
        throw new Error("never reached");
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitDec(this);
    }
}
