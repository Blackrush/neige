package neige.lang.expr.binary.num;

import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.Expressions;
import neige.lang.expr.binary.BinaryExpression;
import neige.lang.expr.literal.FloatExpression;

import java.math.BigDecimal;

public class DivExpression extends BinaryExpression {
    public DivExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.DIV;
    }

    @Override
    protected Expression evaluate(InterpContext ctx, Expression lhs, Expression rhs) {
        BigDecimal left = Expressions.coerceToDecimal(lhs);
        BigDecimal right = Expressions.coerceToDecimal(rhs);
        return new FloatExpression(left.divide(right, ctx.getMathContext()));
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitDiv(new DivExpression(getLhs().traverseVisit(visitor),
                                                  getRhs().traverseVisit(visitor)));
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitDiv(this);
    }
}
