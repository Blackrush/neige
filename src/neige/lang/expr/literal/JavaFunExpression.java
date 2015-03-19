package neige.lang.expr.literal;

import neige.lang.InterpContext;
import neige.lang.NeigeFun;
import neige.lang.NeigeRuntimeException;
import neige.lang.expr.Expression;
import neige.lang.expr.ExpressionVisitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JavaFunExpression extends FunExpression {
    private final Method method;
    private final Object receiver;

    public JavaFunExpression(Method method, Object receiver) {
        this.method = method;
        this.receiver = receiver;
    }

    public static JavaFunExpression fromStatic(Class<?> klass, String name, Class<?>... paramTypes) {
        try {
            Class<?>[] params = new Class<?>[paramTypes.length + 1];
            params[0] = InterpContext.class;
            System.arraycopy(paramTypes, 0, params, 1, paramTypes.length);
            Method method = klass.getMethod(name, params);
            return new JavaFunExpression(method, null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadAll(InterpContext ctx, Class<?> klass) {
        for (Method method : klass.getDeclaredMethods()) {
            NeigeFun ann = method.getAnnotation(NeigeFun.class);
            if (ann == null) {
                continue;
            }
            String name = ann.value();
            if (name.isEmpty()) {
                name = method.getName();
            }
            ctx.put(name, new JavaFunExpression(method, null));
        }
    }

    @Override
    public Method getValue() {
        return method;
    }

    @Override
    public Expression call(InterpContext ctx, List<Expression> arguments) {
        Object[] args = new Object[arguments.size() + 1];
        args[0] = ctx;
        for (int i = 0; i < arguments.size(); i++) {
            args[i + 1] = arguments.get(i);
        }

        Expression resultExp;
        try {
            Object result = method.invoke(receiver, args);

            if (!(result instanceof Expression)) {
                throw new NeigeRuntimeException("\"%s\" didnt return an expression", method);
            }

            resultExp = (Expression) result;
        } catch (IllegalAccessException e) {
            resultExp = new ErrorExpression(e);
        } catch (InvocationTargetException e) {
            resultExp = new ErrorExpression(e.getTargetException());
        } catch (IllegalArgumentException e) {
            resultExp = new ErrorExpression(e);
        }

        return resultExp;
    }

    @Override
    public <T> T visit(ExpressionVisitor<T> visitor) {
        return visitor.visitJavaFun(this);
    }
}
