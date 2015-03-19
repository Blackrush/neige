package neige.lang;

import neige.lang.expr.Expression;

import java.io.InputStream;
import java.io.PrintStream;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class HashInterpContext implements InterpContext {
    private final MathContext mathContext;
    private final InputStream in;
    private final PrintStream out, err;
    private final Map<String, Expression> map = new HashMap<String, Expression>();
    private final boolean debugging;
    private boolean alive = true;

    public HashInterpContext(MathContext mathContext, InputStream in, PrintStream out, PrintStream err, boolean debugging) {
        this.mathContext = mathContext;
        this.in = in;
        this.out = out;
        this.err = err;
        this.debugging = debugging;
    }

    public static HashInterpContext empty() {
        return new HashInterpContext(MathContext.DECIMAL128, System.in, System.out, System.err, false);
    }

    @Override
    public Expression get(String name) {
        return map.get(name);
    }

    @Override
    public void put(String name, Expression exp) {
        map.put(name, exp);
    }

    @Override
    public boolean containsKey(String name) {
        return map.containsKey(name);
    }

    @Override
    public MathContext getMathContext() {
        return mathContext;
    }

    @Override
    public boolean isDebugging() {
        return debugging;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void kill() {
        this.alive = false;
    }

    @Override
    public InputStream in() {
        return in;
    }

    @Override
    public PrintStream out() {
        return out;
    }

    @Override
    public PrintStream err() {
        return err;
    }

    @Override
    public InterpContext newChild() {
        return new Child(this);
    }

    static class Child implements InterpContext {
        private final InterpContext parent;
        private final Map<String, Expression> underlying = new HashMap<String, Expression>();

        Child(InterpContext parent) {
            this.parent = parent;
        }

        @Override
        public Expression get(String name) {
            Expression value = underlying.get(name);
            if (value == null) {
                return parent.get(name);
            }
            return value;
        }

        @Override
        public void put(String name, Expression exp) {
            parent.put(name, exp);
            underlying.put(name, exp);
        }

        @Override
        public boolean containsKey(String name) {
            return underlying.containsKey(name) || parent.containsKey(name);
        }

        @Override
        public MathContext getMathContext() {
            return parent.getMathContext();
        }

        @Override
        public boolean isDebugging() {
            return parent.isDebugging();
        }

        @Override
        public boolean isAlive() {
            return parent.isAlive();
        }

        @Override
        public void kill() {
            parent.kill();
        }

        @Override
        public InputStream in() {
            return parent.in();
        }

        @Override
        public PrintStream out() {
            return parent.out();
        }

        @Override
        public PrintStream err() {
            return parent.err();
        }

        @Override
        public InterpContext newChild() {
            return new Child(this);
        }
    }
}
