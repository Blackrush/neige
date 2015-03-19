package neige.lang.expr.literal;

import neige.lang.NeigeRuntimeException;
import neige.lang.Token;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.TermExpression;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class ListExpression extends LiteralExpression {
    private final List<Expression> expressions;

    public ListExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public static ListExpression newEmpty() {
        return new ListExpression(new LinkedList<Expression>());
    }

    public void addExp(Expression exp) {
        if (exp == NilExpression.i) {
            return;
        }
        expressions.add(exp);
    }

    @SuppressWarnings("unchecked")
    public List<TermExpression> ofTerms() {
        for (Expression expression : expressions) {
            if (!(expression instanceof TermExpression)) {
                throw new NeigeRuntimeException("Not a list of terms");
            }
        }
        return (List) expressions;
    }

    @Override
    public List<Expression> getValue() {
        return expressions;
    }

    @Override
    public String show() {
        StringBuilder result = new StringBuilder();
        result.append(Token.Static.LIST_START.value());
        boolean first = true;
        for (Expression expression : expressions) {
            if (!(expression instanceof LiteralExpression)) {
                throw new NeigeRuntimeException("cannot display %s",
                                                expression.getReflectiveName());
            }
            if (first) first = false;
            else result.append(Token.Static.COMMA.value()).append(" ");
            String repr = ((LiteralExpression) expression).show();
            result.append(repr);
        }
        result.append(Token.Static.LIST_END.value());
        return result.toString();
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitList(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        List<Expression> list = new ArrayList<Expression>(expressions.size());
        for (Expression expression : expressions) {
            list.add(expression.traverseVisit(visitor));
        }
        return visitor.visitList(new ListExpression(list));
    }
}
