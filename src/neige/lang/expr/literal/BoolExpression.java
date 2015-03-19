package neige.lang.expr.literal;

import neige.lang.expr.ExpressionVisitor;

public abstract class BoolExpression extends LiteralExpression {
    private BoolExpression() {}

    public abstract Boolean getValue();

    public static final BoolExpression TRUE = new BoolExpression() {
        @Override
        public Boolean getValue() {
            return Boolean.TRUE;
        }
    };

    public static final BoolExpression FALSE = new BoolExpression() {
        @Override
        public Boolean getValue() {
            return Boolean.FALSE;
        }
    };

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitBool(this);
    }

    public static BoolExpression of(boolean b) {
        return b ? TRUE : FALSE;
    }
}
