package neige.lang.expr.binary;

import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.literal.LiteralExpression;
import neige.lang.expr.literal.StringExpression;

public class CatStrExpression extends BinaryExpression {
    public CatStrExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    protected Expression evaluate(InterpContext ctx, Expression lhs, Expression rhs) {
        if (!(lhs instanceof LiteralExpression) || !(rhs instanceof LiteralExpression)) {
            // TODO ConcatStrExpression#evaluate
            throw new Error("TODO ConcatStrExpression#evaluate:19");
        }

        String left = ((LiteralExpression) lhs).show();
        String right = ((LiteralExpression) rhs).show();

        return new StringExpression(left + right);
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitCatStr(this);
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.CAT;
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitCatStr(
                new CatStrExpression(getLhs().traverseVisit(visitor),
                                     getRhs().traverseVisit(visitor)));
    }
}
