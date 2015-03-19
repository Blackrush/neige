package neige.lang.expr.cond;

import neige.lang.InterpContext;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.Expressions;
import neige.lang.expr.TermExpression;
import neige.lang.expr.binary.DeclVarExpression;
import neige.lang.expr.literal.ErrorExpression;
import neige.lang.expr.literal.ListExpression;

import java.util.List;

public final class ForeachExpression extends Expression {
    private final DeclVarExpression generator;
    private final Expression body;

    public ForeachExpression(DeclVarExpression generator, Expression body) {
        this.generator = generator;
        this.body = body;
    }

    public DeclVarExpression getGenerator() {
        return generator;
    }

    public Expression getBody() {
        return body;
    }

    @Override
    public Expression evaluate(InterpContext ctx) {
        TermExpression iterator = generator.getIdentifier();
        Expression iteratee = generator.getRhs();

        List<Expression> list = Expressions.coerceToList(iteratee.evaluate(ctx));

        ListExpression acc = ListExpression.newEmpty();
        for (Expression exp : list) {
            InterpContext scope = ctx.newChild();

            Expression value = exp.evaluate(ctx);
            if (value instanceof ErrorExpression) {
                return value;
            }

            scope.put(iterator.getValue(), value);

            Expression result = body.evaluate(scope);
            acc.addExp(result);
        }
        return acc;
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitForeach(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        // TODO ForeachExpression#traverseVisit
        throw new Error("TODO ForeachExpression#traverseVisit:45");
    }
}
