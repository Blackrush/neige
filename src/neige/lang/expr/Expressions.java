package neige.lang.expr;

import neige.lang.NeigeRuntimeException;
import neige.lang.expr.literal.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public final class Expressions {
    private Expressions() {}

    public static BigDecimal coerceToDecimal(Expression exp) {
        if (exp instanceof FloatExpression) {
            return ((FloatExpression) exp).getValue();
        } else if (exp instanceof IntExpression) {
            return new BigDecimal(((IntExpression) exp).getValue());
        } else {
            throw new NeigeRuntimeException("cannot coerce `%s' to float", exp.getReflectiveName());
        }
    }

    public static boolean coerceToBoolean(Expression exp) {
        if (exp instanceof BoolExpression) {
            return ((BoolExpression) exp).getValue();
        } else if (exp == NilExpression.i) {
            return false;
        } else if (exp instanceof StringExpression) {
            return ((StringExpression) exp).getValue().length() > 0;
        } else if (exp instanceof IntExpression) {
            return ((IntExpression) exp).getValue().compareTo(BigInteger.ZERO) != 0;
        } else if (exp instanceof FloatExpression) {
            return ((FloatExpression) exp).getValue().compareTo(BigDecimal.ZERO) != 0;
        }
        throw new NeigeRuntimeException("cannot coerce `%s' to boolean", exp.getReflectiveName());
    }

    public static List<Expression> coerceToList(Expression exp) {
        if (exp instanceof ListExpression) {
            return ((ListExpression) exp).getValue();
        } else if (exp == NilExpression.i) {
            return Collections.emptyList();
        }
        return Collections.singletonList(exp);
    }

    public static Expression unwrapList(List<Expression> exps) {
        if (exps.isEmpty()) {
            return NilExpression.i;
        } else if (exps.size() == 1) {
            return exps.get(0);
        }
        return new ListExpression(exps);
    }
}
