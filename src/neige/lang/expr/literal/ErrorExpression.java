package neige.lang.expr.literal;

import neige.lang.expr.ExpressionVisitor;

public final class ErrorExpression extends LiteralExpression {
    private final Throwable error;

    public ErrorExpression(Throwable error) {
        this.error = error;
    }

    @Override
    public Throwable getValue() {
        return error;
    }

    @Override
    public String show() {
        return "/!\\ " + error.getMessage() + " /!\\";
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitError(this);
    }
}
