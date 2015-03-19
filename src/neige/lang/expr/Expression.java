package neige.lang.expr;

import neige.lang.InterpContext;

public abstract class Expression {
    public abstract Expression evaluate(InterpContext ctx);

    public abstract <T> T visit(ExpressionVisitor<T> visitor);

    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visit(visitor);
    }

    public String getReflectiveName() {
        return getClass().getSimpleName();
    }
}
