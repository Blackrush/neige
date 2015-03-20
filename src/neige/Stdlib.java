package neige;

import neige.lang.expr.Expression;
import neige.lang.expr.literal.*;
import neige.lang.InterpContext;
import neige.lang.NeigeFun;
import neige.lang.NeigeRuntimeException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

@SuppressWarnings("UnusedDeclaration")
public final class Stdlib {
    private Stdlib() {}

    public static void load(InterpContext ctx) {
        ctx.put("pi", FloatExpression.of(Math.PI));
        JavaFunExpression.loadAll(ctx, Stdlib.class);
    }

    @NeigeFun
    public static FloatExpression cos(InterpContext ctx, FloatExpression exp) {
        // TODO perte de precision
        double cos = Math.cos(exp.getValue().doubleValue());
        return new FloatExpression(BigDecimal.valueOf(cos));
    }

    @NeigeFun
    public static FloatExpression sin(InterpContext ctx, FloatExpression exp) {
        // TODO perte de precision
        double sin = Math.sin(exp.getValue().doubleValue());
        return new FloatExpression(BigDecimal.valueOf(sin));
    }

    @NeigeFun
    public static FloatExpression tan(InterpContext ctx, FloatExpression exp) {
        // TODO perte de precision
        double tan = Math.tan(exp.getValue().doubleValue());
        return new FloatExpression(BigDecimal.valueOf(tan));
    }

    @NeigeFun
    public static FloatExpression sqrt(InterpContext ctx, FloatExpression exp) {
        // TODO perte de precision
        double sqrt = Math.sqrt(exp.getValue().doubleValue());
        return new FloatExpression(BigDecimal.valueOf(sqrt));
    }

    @NeigeFun
    public static NilExpression quit(InterpContext ctx) {
        ctx.kill();
        return NilExpression.i;
    }

    @NeigeFun
    public static NilExpression ecrire(InterpContext ctx, LiteralExpression exp) {
        ctx.out().println(exp.show());
        return NilExpression.i;
    }

    @NeigeFun
    public static StringExpression lire(InterpContext ctx, StringExpression exp) {
        String var = exp.getValue();

        ctx.out().printf("Entrez %s => ", var);
        Scanner scanner = new Scanner(ctx.in());
        String value = scanner.nextLine();

        StringExpression result = new StringExpression(value);
        ctx.put(var, result);
        return result;
    }

    @NeigeFun("int")
    public static IntExpression to_int(InterpContext ctx, LiteralExpression exp) {
        if (exp instanceof IntExpression) {
            return (IntExpression) exp;
        } else if (exp instanceof FloatExpression) {
            BigDecimal deci = ((FloatExpression) exp).getValue();
            return new IntExpression(deci.toBigInteger());
        } else if (exp instanceof StringExpression) {
            String str = ((StringExpression) exp).getValue();
            return new IntExpression(new BigInteger(str));
        } else if (exp instanceof BoolExpression) {
            return exp == BoolExpression.TRUE
                    ? new IntExpression(BigInteger.ONE)
                    : new IntExpression(BigInteger.ZERO);
        }
        throw new NeigeRuntimeException("You cannot convert a `%s' to an int.", exp.getReflectiveName());
    }

    @NeigeFun("float")
    public static FloatExpression to_float(InterpContext ctx, LiteralExpression exp) {
        if (exp instanceof IntExpression) {
            return new FloatExpression(new BigDecimal(((IntExpression) exp).getValue()));
        } else if (exp instanceof FloatExpression) {
            return (FloatExpression) exp;
        } else if (exp instanceof StringExpression) {
            String str = ((StringExpression) exp).getValue();
            return new FloatExpression(new BigDecimal(str));
        } else if (exp instanceof BoolExpression) {
            return exp == BoolExpression.TRUE
                    ? new FloatExpression(BigDecimal.ZERO)
                    : new FloatExpression(BigDecimal.ONE);
        }
        throw new NeigeRuntimeException("You cannot convert a `%s' to an int.", exp.getReflectiveName());
    }

    @NeigeFun("str")
    public static StringExpression to_str(InterpContext ctx, LiteralExpression exp) {
        if (exp instanceof IntExpression) {
            return new StringExpression(((IntExpression) exp).getValue().toString());
        } else if (exp instanceof FloatExpression) {
            BigDecimal deci = ((FloatExpression) exp).getValue();
            return new StringExpression(deci.toString());
        } else if (exp instanceof StringExpression) {
            return (StringExpression) exp;
        } else if (exp instanceof BoolExpression) {
            return exp == BoolExpression.TRUE
                    ? new StringExpression("true")
                    : new StringExpression("false");
        }
        throw new NeigeRuntimeException("You cannot convert a `%s' to an int.", exp.getReflectiveName());
    }

    @NeigeFun
    public static FunExpression fn(InterpContext ctx, StringExpression exp) {
        Expression fun = ctx.get(exp.getValue());
        if (fun == null) {
            throw new NeigeRuntimeException("Unknown function `%s'", exp.getValue());
        }
        if (!(fun instanceof FunExpression)) {
            throw new NeigeRuntimeException("Variable `%s' is a `%s', not a function", exp.getValue(), fun.getReflectiveName());
        }
        return (FunExpression) fun;
    }

    @NeigeFun
    public static StringExpression what(InterpContext ctx, Expression exp) {
        return new StringExpression(exp.getReflectiveName());
    }

    @NeigeFun
    public static IntExpression len(InterpContext ctx, ListExpression list) {
        return new IntExpression(BigInteger.valueOf(list.getValue().size()));
    }

    @NeigeFun
    public static NilExpression append(InterpContext ctx, ListExpression list, Expression exp) {
        list.addExp(exp);
        return NilExpression.i;
    }

    @NeigeFun
    public static BoolExpression isPrimary(InterpContext ctx, IntExpression exp) {
        return BoolExpression.of(exp.getValue().isProbablePrime(2147483646));
    }
}
