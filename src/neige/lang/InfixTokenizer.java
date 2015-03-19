package neige.lang;

import neige.lang.expr.*;
import neige.lang.expr.binary.BinaryExpression;
import neige.lang.expr.cond.ForeachExpression;
import neige.lang.expr.cond.IfExpression;
import neige.lang.expr.cond.WhileExpression;
import neige.lang.expr.literal.ListExpression;
import neige.lang.expr.literal.LiteralExpression;
import neige.lang.expr.literal.NilExpression;
import neige.lang.expr.unary.UnaryExpression;

import java.util.LinkedList;

public class InfixTokenizer {
    public static LinkedList<Token> tokenize(String input) {
        Token.Static[] separators = Token.Static.values();
        LinkedList<Token> tokens = new LinkedList<Token>();
        StringBuilder buf = new StringBuilder();
        int cursor = 0;
        outer:  while (cursor < input.length()) {
            if (input.charAt(cursor) == '"') {
                int end = cursor;
                do {
                    end = input.indexOf('"', end + 1);
                    if (end < 0) {
                        throw new ParseException("You must close your quotes.");
                    }
                } while (input.charAt(end - 1) == '\\');

                Token tok = new Token.Dynamic(input.substring(cursor, end + 1));
                tokens.add(tok);
                cursor = end + 1;
                continue;
            }

            if (input.charAt(cursor) == '\n') {
                if (buf.length() > 0) {
                    tokens.add(new Token.Dynamic(buf.toString()));
                    buf.setLength(0);
                }
                tokens.add(Token.Static.NL);
                cursor++;
                continue;
            }

            if (Character.isWhitespace(input.charAt(cursor))) {
                if (buf.length() > 0) {
                    tokens.add(new Token.Dynamic(buf.toString()));
                    buf.setLength(0);
                }
                cursor++;
                continue;
            }

            for (Token.Static separator : separators) {
                if (separator.detect(input, cursor)) {
                    if (buf.length() > 0) {
                        tokens.add(new Token.Dynamic(buf.toString()));
                        buf.setLength(0);
                    }
                    tokens.add(separator);
                    cursor += separator.weight();
                    continue outer;
                }
            }

            buf.append(input.charAt(cursor));
            cursor++;
        }
        if (buf.length() > 0) {
            tokens.add(new Token.Dynamic(buf.toString()));
            buf.setLength(0);
        }
        return tokens;
    }

    public static String show(Expression exp) {
        return exp.visit(new ExpressionVisitor<String>() {
            int depth = 0;

            @Override
            public void before() {
                depth++;
            }

            @Override
            public void after() {
                depth--;
            }

            @Override
            public String otherwise(Expression exp) {
                throw new Error("expression " + exp.getReflectiveName() + " cannot be shown");
            }

            @Override
            public String visitLiteral(LiteralExpression exp) {
                return exp.show();
            }

            @Override
            public String visitBinary(BinaryExpression exp) {
                return "(" + exp.getLhs().visit(this) + " " +
                       exp.getToken().value() + " " +
                       exp.getRhs().visit(this) + ")";
            }

            @Override
            public String visitUnary(UnaryExpression exp) {
                return exp.getToken().value() +
                       exp.getExpression().visit(this);
            }

            @Override
            public String visitTerm(TermExpression exp) {
                return exp.getValue();
            }

            @Override
            public String visitList(ListExpression exp) {
                StringBuilder result = new StringBuilder();
                result.append(Token.Static.LIST_START.value());
                boolean first = true;
                for (Expression expression : exp.getValue()) {
                    if (first) first = false;
                    else result.append(Token.Static.COMMA.value()).append(" ");
                    result.append(expression.visit(this));
                }
                result.append(Token.Static.LIST_END.value());
                return result.toString();
            }

            @Override
            public String visitFunCall(FunCallExpression exp) {
                StringBuilder result = new StringBuilder();
                result.append(exp.getIdentifier().getValue());
                result.append("(");
                boolean first = true;
                for (Expression expression : exp.getArguments()) {
                    if (first) {
                        first = false;
                    } else {
                        result.append(", ");
                    }
                    result.append(show(expression));
                }
                result.append(")");
                return result.toString();
            }

            @Override
            public String visitDeclFun(DeclFunExpression exp) {
                StringBuilder result = new StringBuilder();
                result.append(Token.Static.DECL_FUN.value());
                result.append(" ");
                result.append(exp.getId().getValue());
                result.append(Token.Static.PAREN_START);
                boolean first = true;
                for (TermExpression param : exp.getParams()) {
                    if (first) first = false;
                    else result.append(Token.Static.COMMA).append(" ");
                    result.append(param.getValue()).append(" ");
                }
                result.append(Token.Static.PAREN_END);
                result.append(" ");
                if (exp.getBody() instanceof BlockExpression) {
                    result.append(exp.getBody().visit(this));
                } else {
                    result.append(Token.Static.DECL_VAR.value());
                    result.append(" ");
                    result.append(show(exp.getBody()));
                }
                return result.toString();
            }

            @Override
            public String visitBlock(BlockExpression block) {
                String indent = Strings.indent(depth);
                StringBuilder result = new StringBuilder();
                result.append(Token.Static.BLOCK_START.value());
                for (Expression expression : block.getBody()) {
                    result.append(Token.Static.NL.value());
                    result.append(indent);
                    result.append(show(expression));
                }
                result.append(Token.Static.NL.value());
                result.append(indent);
                result.append(Token.Static.BLOCK_END.value());
                return result.toString();
            }

            @Override
            public String visitIf(IfExpression exp) {
                if (exp.getOtherwise() == NilExpression.i) {
                    return Token.Static.IF_START.value() + " " +
                           show(exp.getTest()) + " " +
                           show(exp.getBody());
                } else {
                    return Token.Static.IF_START.value() + " " +
                           show(exp.getTest()) + " " +
                           show(exp.getBody()) + " " +
                           Token.Static.ELSE.value() + " " +
                           show(exp.getOtherwise());
                }
            }

            @Override
            public String visitWhile(WhileExpression exp) {
                return Token.Static.WHILE_START.value() + " " +
                       exp.getTest().visit(this) + " " +
                       exp.getBody().visit(this);
            }

            @Override
            public String visitForeach(ForeachExpression exp) {
                return Token.Static.EACH_END.value() + " " +
                       exp.getGenerator().visit(this) + " " +
                       exp.getBody().visit(this);
            }
        });
    }
}
