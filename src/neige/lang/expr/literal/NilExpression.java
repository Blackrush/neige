package neige.lang.expr.literal;

import neige.lang.expr.ExpressionVisitor;

public final class NilExpression extends LiteralExpression {
    public static final NilExpression i = new NilExpression();

    public static final Object NIL = "Nil";

    @Override
    public Object getValue() {
        return NIL;
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitNil(this);
    }
}
