package neige.lang;

import neige.lang.expr.Expression;

public interface Parser {
    Expression parse(String input);
    String show(Expression exp);
}