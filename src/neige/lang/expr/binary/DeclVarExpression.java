package neige.lang.expr.binary;

import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.TermExpression;

public class DeclVarExpression extends BinaryExpression {
    private final TermExpression identifier;

    public DeclVarExpression(TermExpression lhs, Expression rhs) {
        super(lhs, rhs);
        this.identifier = lhs;
    }

    public TermExpression getIdentifier() {
        return identifier;
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.DECL_VAR;
    }

    @Override
    public Expression evaluate(InterpContext ctx) {
        Expression value = getRhs().evaluate(ctx);
        ctx.put(identifier.getValue(), value);
        return value;
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitDeclVar(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitDeclVar(
                new DeclVarExpression(identifier, getRhs().traverseVisit(visitor)));
    }

    @Override
    protected Expression evaluate(InterpContext ctx, Expression lhs, Expression rhs) {
        throw new Error("never reached");
    }
}
