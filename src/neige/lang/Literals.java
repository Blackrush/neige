package neige.lang;

import neige.lang.expr.Expression;
import neige.lang.expr.TermExpression;
import neige.lang.expr.literal.BoolExpression;
import neige.lang.expr.literal.FloatExpression;
import neige.lang.expr.literal.IntExpression;
import neige.lang.expr.literal.StringExpression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Literals {

    private Literals() {}

    public static Expression parse(Token.Dynamic token) {
        String lit = token.value();

        // string
        if (lit.charAt(0) == '"') {
            if (lit.charAt(lit.length() - 1) != '"') {
                throw new ParseException("a string should start and end with \"");
            }
            return new StringExpression(lit.substring(1, lit.length() - 1));
        }

        // int
        if (isInteger(lit)) {
            BigInteger i = new BigInteger(lit, 10);
            return new IntExpression(i);
        }

        // float
        if (isFloat(lit)) {
            BigDecimal d = new BigDecimal(lit, MathContext.UNLIMITED);
            return new FloatExpression(d);
        }

        // bool
        if (lit.equalsIgnoreCase("true")) {
            return BoolExpression.TRUE;
        }
        if (lit.equalsIgnoreCase("false")) {
            return BoolExpression.FALSE;
        }

        // term
        if (isTerm(lit)) {
            return new TermExpression(lit);
        }

        throw new ParseException("invalid literal \"%s\"", lit);
    }

    private static boolean isInteger(String lit) {
        for (int i = 0; i < lit.length(); i++) {
            if (!Character.isDigit(lit.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFloat(String lit) {
        int sep = lit.indexOf('.');

        if (sep < 0) { // we found a separator
            return false;
        }

        if (lit.indexOf('.', sep + 1) > 0) { // we found _another_ separator, fall back to another exp
            return false;
        }

        for (int i = 0; i < lit.length(); i++) {
            if (lit.charAt(i) == '.') continue;
            if (!Character.isDigit(lit.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isTerm(String lit) {
        if (!Character.isLetter(lit.charAt(0))) {
            return false;
        }

        for (int i = 1; i < lit.length(); i++) {
            if (!Character.isLetterOrDigit(lit.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
