package neige.lang.expr;

import neige.lang.InterpContext;
import neige.lang.expr.literal.NeigeFunExpression;

import java.util.List;

public final class DeclFunExpression extends Expression {
    private final TermExpression id;
    private final List<TermExpression> params;
    private final Expression body;

    public DeclFunExpression(TermExpression id, List<TermExpression> params, Expression body) {
        this.id = id;
        this.params = params;
        this.body = body;
    }

    public TermExpression getId() {
        return id;
    }

    public List<TermExpression> getParams() {
        return params;
    }

    public Expression getBody() {
        return body;
    }

    @Override
    public Expression evaluate(InterpContext ctx) {
        NeigeFunExpression fun = new NeigeFunExpression(id, params, body);
        ctx.put(fun.getId().getValue(), fun);
        return fun.getId();
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitDeclFun(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        return visitor.visitDeclFun(
                new DeclFunExpression(id,
                                      params,
                                      body.traverseVisit(visitor)));
    }
}
