package neige.lang.expr.literal;

import neige.lang.expr.ExpressionVisitor;

import java.math.BigInteger;

public final class IntExpression extends LiteralExpression {
    private final BigInteger value;

    public IntExpression(BigInteger value) {
        this.value = value;
    }

    @Override
    public BigInteger getValue() {
        return value;
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitInt(this);
    }
}
