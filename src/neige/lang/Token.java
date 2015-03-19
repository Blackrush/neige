package neige.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface Token {
    String value();
    int weight();
    boolean hasPrecedence(int prec);
    int nextPrecedence();
    boolean isLineSeparator();

    public final class Dynamic implements Token {
        private final String value;

        public Dynamic(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public int weight() {
            return value.length();
        }

        @Override
        public boolean hasPrecedence(int prec) {
            return false;
        }

        @Override
        public int nextPrecedence() {
            return 0;
        }

        @Override
        public boolean isLineSeparator() {
            return false;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static final int UTMOST = Integer.MAX_VALUE,
                            HIGHER = 5,
                            HIGH   = 4,
                            NORMAL = 3,
                            LOW    = 2,
                            LOWER  = 1,
                            ZERO   = 0;
    public static final Boolean LEFT_ASSOC = true,
                                RIGHT_ASSOC = false,
                                NO_ASSOC = null;

    public enum Static implements Token {
        NL("\n", UTMOST, NO_ASSOC) {
            @Override
            public boolean isLineSeparator() {
                return true;
            }
        },
        SEMICOLON(";", UTMOST, NO_ASSOC) {
            @Override
            public boolean isLineSeparator() {
                return false;
            }
        },

        PAREN_START("(",       UTMOST,      NO_ASSOC),
        PAREN_END  (")",       UTMOST,      NO_ASSOC),
        BLOCK_START("{",       UTMOST,      NO_ASSOC),
        BLOCK_END  ("}",       UTMOST,      NO_ASSOC),
        LIST_START ("[",       UTMOST,      NO_ASSOC),
        LIST_END   ("]",       UTMOST,      NO_ASSOC),
        COMMA      (",",       UTMOST,      NO_ASSOC),
        DECL_VAR   ("<-",      ZERO,        RIGHT_ASSOC),
        DECL_FUN   ("fun",     UTMOST,      NO_ASSOC),
        INC        ("++",      HIGHER,      NO_ASSOC),
        DEC        ("--",      HIGHER,      NO_ASSOC),
        MOD        ("%",       HIGH,        RIGHT_ASSOC),
        POW        ("**",      HIGH,        RIGHT_ASSOC),
        ADD        ("+",       LOW,         LEFT_ASSOC),
        SUB        ("-",       LOW,         LEFT_ASSOC),
        MUL        ("*",       NORMAL,      LEFT_ASSOC),
        DIV        ("/",       NORMAL,      LEFT_ASSOC),
        MORE_EQ    (">=",      LOWER,       LEFT_ASSOC),
        MORE       (">",       LOWER,       LEFT_ASSOC),
        LESS_EQ    ("<=",      LOWER,       LEFT_ASSOC),
        LESS       ("<",       LOWER,       LEFT_ASSOC),
        EQ         ("=",       LOWER,       LEFT_ASSOC),
        NEQ        ("!=",      LOWER,       LEFT_ASSOC),
        AND        ("&&",      LOWER,       LEFT_ASSOC),
        OR         ("||",      LOWER,       LEFT_ASSOC),
        XOR        ("^",       LOWER,       LEFT_ASSOC),
        NOT        ("!",       LOWER,       NO_ASSOC),
        CAT        ("&",       LOWER,       LEFT_ASSOC),
        ELSE       ("else",    UTMOST,      NO_ASSOC),
        IF         ("if",      UTMOST,      NO_ASSOC),
        WHILE      ("while",   UTMOST,      NO_ASSOC),
        FOREACH    ("foreach", UTMOST,      NO_ASSOC),
        FOR        ("for",     UTMOST,      NO_ASSOC),
        ;

        @Override
        public String value() {
            return value;
        }

        @Override
        public int weight() {
            return value.length();
        }

        @Override
        public boolean hasPrecedence(int prec) {
            return leftAssoc != null && precedence >= prec;
        }

        @Override
        public int nextPrecedence() {
            if (isLeftAssoc()) {
                return precedence + 1;
            }
            return precedence;
        }

        public boolean isLineSeparator() {
            return false;
        }

        public boolean isLeftAssoc() {
            return leftAssoc != null && leftAssoc;
        }

        public boolean detect(String input, int start) {
            if (input.length() - start < value.length()) {
                return false;
            }
            String sliced = input.substring(start, start + value.length());
            return value.equals(sliced);
        }

        private final String value;
        private final int precedence;
        private final Boolean leftAssoc;
        Static(String value, int precedence, Boolean leftAssoc) {
            this.value = value;
            this.precedence = precedence;
            this.leftAssoc = leftAssoc;
        }
        private static final Map<String, Token> tokens;
        static {
            Map<String, Token> tt = new HashMap<String, Token>();
            for (Token token : values()) {
                tt.put(token.value(), token);
            }
            tokens = Collections.unmodifiableMap(tt);
        }
        public static Token get(String value) {
            return tokens.get(value);
        }
    }
}
