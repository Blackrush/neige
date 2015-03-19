package neige.lang.expr.binary.bool;


import neige.lang.InterpContext;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.binary.BinaryExpression;
import neige.lang.expr.literal.BoolExpression;

/**
 * Created by Hawk on 16/03/2015.
 */

public class XorExpression extends BinaryExpression {

    public XorExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    protected Expression evaluate(InterpContext ctx, Expression lhs, Expression rhs) {
        if (lhs instanceof BoolExpression && rhs instanceof BoolExpression) {
            Boolean left = ((BoolExpression) lhs).getValue();
            Boolean right = ((BoolExpression) rhs).getValue();
            return left^right ? BoolExpression.TRUE : BoolExpression.FALSE;
        }

        // TODO AddExpression#evaluate
        throw new Error("TODO AddExpression#evaluate:26");
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitXor(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitXor(new XorExpression(getLhs().traverseVisit(visitor),
                                                  getRhs().traverseVisit(visitor)));
    }

    @Override
    public Token.Static getToken() {
        return Token.Static.XOR;
    }
}