package neige.lang.expr.literal;

import neige.lang.expr.ExpressionVisitor;

import java.math.BigDecimal;
import java.math.MathContext;

public final class FloatExpression extends LiteralExpression {
    private final BigDecimal value;

    public FloatExpression(BigDecimal value) {
        this.value = value;
    }

    public static FloatExpression of(double d) {
        return new FloatExpression(BigDecimal.valueOf(d));
    }

    public static FloatExpression of(String lit, MathContext ctx) {
        return new FloatExpression(new BigDecimal(lit, ctx));
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitFloat(this);
    }
}
