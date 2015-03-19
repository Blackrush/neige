package neige.lang.expr.literal;

import neige.lang.expr.ExpressionVisitor;

public final class StringExpression extends LiteralExpression {
    private final String value;

    public StringExpression(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String show() {
        return value;
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitString(this);
    }
}
