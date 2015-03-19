package neige.lang;

import neige.lang.expr.Expression;

import java.io.InputStream;
import java.io.PrintStream;
import java.math.MathContext;

public interface InterpContext {
    Expression get(String name);
    void put(String name, Expression exp);
    boolean containsKey(String name);

    MathContext getMathContext();

    boolean isDebugging();
    boolean isAlive();
    void kill();

    InputStream in();
    PrintStream out();
    PrintStream err();

    InterpContext newChild();
}
