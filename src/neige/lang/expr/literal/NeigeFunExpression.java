package neige.lang.expr.literal;

import neige.lang.InterpContext;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;
import neige.lang.expr.TermExpression;

import java.util.List;

public class NeigeFunExpression extends FunExpression {
    private final TermExpression id;
    private final List<TermExpression> params;
    private final Expression body;

    public NeigeFunExpression(TermExpression id, List<TermExpression> params, Expression body) {
        this.id = id;
        this.params = params;
        this.body = body;
    }

    public TermExpression getId() {
        return id;
    }

    @Override
    public String getValue() {
        return id.getValue();
    }

    @Override
    public Expression call(InterpContext outerCtx, List<Expression> arguments) {
        InterpContext funCtx = outerCtx.newChild();
        for (int i = 0; i < params.size(); i++) {
            TermExpression name = params.get(i);
            Expression value = arguments.get(i);
            funCtx.put(name.getValue(), value.evaluate(outerCtx));
        }
        return body.evaluate(funCtx);
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitNeigeFun(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitNeigeFun(
                new NeigeFunExpression(id,
                                       params,
                                       body.traverseVisit(visitor)));
    }
}
