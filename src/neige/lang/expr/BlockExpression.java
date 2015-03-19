package neige.lang.expr;

import neige.lang.InterpContext;
import neige.lang.expr.literal.NilExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class BlockExpression extends Expression {
    private final List<Expression> body;

    public BlockExpression(List<Expression> body) {
        this.body = body;
    }

    public static BlockExpression newEmpty() {
        return new BlockExpression(new LinkedList<Expression>());
    }

    public static BlockExpression newEmpty(int capacity) {
        return new BlockExpression(new ArrayList<Expression>(capacity));
    }

    public static BlockExpression of(Expression... exps) {
        return new BlockExpression(Arrays.asList(exps));
    }

    public static BlockExpression wrapIfNeeded(Expression exp) {
        if (exp instanceof BlockExpression) {
            return (BlockExpression) exp;
        }
        BlockExpression block = BlockExpression.newEmpty();
        block.append(exp);
        return block;
    }

    public List<Expression> getBody() {
        return body;
    }

    public void prepend(Expression exp) {
        body.add(0, exp);
    }

    public void append(Expression exp) {
        if (exp == NilExpression.i) {
            return;
        }
        body.add(exp);
    }

    public Expression unwrapIfNeeded() {
        if (body.isEmpty()) {
            return NilExpression.i;
        }
        if (body.size() == 1) {
            return body.get(0);
        }
        return this;
    }

    @Override
    public Expression evaluate(InterpContext ctx) {
        Expression last = NilExpression.i;
        for (Expression expression : body) {
            last = expression.evaluate(ctx);
        }
        return last;
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitBlock(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        BlockExpression block = newEmpty(body.size());
        for (Expression expression : body) {
            block.append(expression.traverseVisit(visitor));
        }
        return visitor.visitBlock(block);
    }
}
