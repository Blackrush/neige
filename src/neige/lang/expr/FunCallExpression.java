package neige.lang.expr;

import neige.lang.InterpContext;
import neige.lang.NeigeRuntimeException;
import neige.lang.expr.literal.ErrorExpression;
import neige.lang.expr.literal.FunExpression;

import java.util.ArrayList;
import java.util.List;

public final class FunCallExpression extends Expression {
    private final TermExpression identifier;
    private final List<Expression> arguments;

    public FunCallExpression(TermExpression identifier, List<Expression> arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }

    public TermExpression getIdentifier() {
        return identifier;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public Expression evaluate(InterpContext ctx) {
        // retrieve the function
        Expression expression = ctx.get(identifier.getValue());
        if (!(expression instanceof FunExpression)) {
            return new ErrorExpression(new NeigeRuntimeException(
                    "undefined function \"%s\"", identifier.getValue()));
        }
        FunExpression fun = (FunExpression) expression;

        // evaluate its parameters
        List<Expression> args = new ArrayList<Expression>(arguments.size());
        for (Expression argument : arguments) {
            args.add(argument.evaluate(ctx));
        }

        // call the function
        return fun.call(ctx, args);
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitFunCall(this);
    }

    @Override
    public Expression traverseVisit(ExpressionVisitor<Expression> visitor) {
        List<Expression> arguments = new ArrayList<Expression>(this.arguments.size());
        for (Expression argument : this.arguments) {
            arguments.add(argument.traverseVisit(visitor));
        }
        return visitor.visitFunCall(new FunCallExpression(identifier, arguments));
    }
}
