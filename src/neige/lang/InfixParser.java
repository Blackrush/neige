package neige.lang;

import neige.lang.expr.*;
import neige.lang.expr.binary.CatStrExpression;
import neige.lang.expr.binary.DeclVarExpression;
import neige.lang.expr.binary.bool.*;
import neige.lang.expr.binary.num.*;
import neige.lang.expr.cond.ForeachExpression;
import neige.lang.expr.cond.IfExpression;
import neige.lang.expr.cond.WhileExpression;
import neige.lang.expr.literal.ListExpression;
import neige.lang.expr.literal.NilExpression;
import neige.lang.expr.unary.*;

import java.util.LinkedList;
import java.util.List;

import static neige.lang.Token.Static.*;

public class InfixParser implements Parser {

    @Override
    public String show(Expression exp) {
        return InfixTokenizer.show(exp);
    }

    @Override
    public Expression parse(String input) {
        LinkedList<Token> tokens = InfixTokenizer.tokenize(input);
        return new Context(tokens).parseBlock();
    }

    static class Context {
        final LinkedList<Token> tokens;
        final int minPrec;

        Context(LinkedList<Token> tokens) {
            this(tokens, 0);
        }

        Context(LinkedList<Token> tokens, int minPrec) {
            this.tokens = tokens;
            this.minPrec = minPrec;
        }

        Expression parse() {
            Expression lhs = parseOne();
            while (!tokens.isEmpty() && lhs != NilExpression.i) {
                Token tok = tokens.getFirst();
                if (!tok.hasPrecedence(minPrec)) {
                    break;
                }
                tokens.removeFirst();

                Expression rhs = new Context(tokens, tok.nextPrecedence()).parse();
                if (rhs == NilExpression.i) {
                    throw new ParseException("unexpected end of input");
                }
                lhs = parseBin(tok, lhs, rhs);
            }
            return lhs;
        }

        boolean isNextToken(Token.Static tok) {
            while (!tokens.isEmpty() && tokens.getFirst() == NL) {
                tokens.removeFirst();
            }
            if (!tokens.isEmpty() && tokens.getFirst() == tok) {
                tokens.removeFirst();
                return true;
            }
            return false;
        }

        Expression parseOne() {
            Token first = popToken();

            if (first instanceof Token.Dynamic) {
                Expression literal = Literals.parse((Token.Dynamic) first);
                if (literal instanceof TermExpression && isNextToken(PAREN_START)) {
                    TermExpression id = (TermExpression) literal;
                    ListExpression args = betweenParens().parseList();
                    return new FunCallExpression(id, args.getValue());
                }
                return literal;
            }

            Token.Static tok = (Token.Static) first;

            switch (tok) {
                case NL:            return NilExpression.i;

                case PAREN_START:   return between(PAREN_START, PAREN_END).parse();
                case BLOCK_START:   return between(BLOCK_START, BLOCK_END).parseBlock();
                case LIST_START:    return between(LIST_START,  LIST_END).parseList();

                case IF_START:      return parseIf();
                case WHILE_START:   return parseWhile();
                case FOR_START:     return parseFor();
                case EACH_START:    return parseForeach();
                case DECL_FUN:      return parseDeclFun();

                default:            return parseUnary(tok, parseOne());
            }
        }

        Token popToken() {
            while (!tokens.isEmpty() && tokens.getFirst() == NL) {
                tokens.removeFirst();
            }
            if (tokens.isEmpty()) {
                return NL;
            }
            return tokens.removeFirst();
        }

        Context between(Token.Static start, Token.Static end) {
            isNextToken(start); // safely remove the starting token, or not

            LinkedList<Token> toks = new LinkedList<Token>();
            int pairs = 1;
            while (pairs > 0) {
                Token cur = popToken();
                if (cur == end) {
                    pairs--;
                }
                if (cur == start) {
                    pairs++;
                }
                toks.addLast(cur);
            }
            toks.removeLast(); // remove end

            return new Context(toks);
        }

        Context takeUntil(Token.Static end) {
            int delta = end.getScoping() == Scoping.CLOSING ? 1 : -1;
            LinkedList<Token> toks = new LinkedList<Token>();
            int remaining = 1;
            while (true) {
                Token tok = tokens.getFirst();

                switch (tok.getScoping()) {
                    case OPENING: remaining += delta; break;
                    case CLOSING: remaining -= delta; break;
                }

                if (remaining <= 1 && tok == end) {
                    break;
                }

                tokens.removeFirst();
                toks.addLast(tok);
            }

            tokens.removeFirst();

            return new Context(toks);
        }

        Context takeUntil(Token.Static... end) {
            LinkedList<Token> toks = new LinkedList<Token>();
            int remaining = 1;
            outer: while (true) {
                Token tok = tokens.getFirst();

                switch (tok.getScoping()) {
                    case OPENING: remaining++; break;
                    case CLOSING: remaining--; break;
                }

                if (remaining <= 1) {
                    for (Static anEnd : end) {
                        if (tok == anEnd) {
                            break outer;
                        }
                    }
                }

                tokens.removeFirst();
                toks.addLast(tok);
            }

            if (end.length == 1) {
                tokens.removeFirst();
            }

            return new Context(toks);
        }

        Context betweenParens() {
            return between(PAREN_START, PAREN_END);
        }

        Expression parseBin(Token tok, Expression lhs, Expression rhs) {
            switch ((Token.Static) tok) {
                case CAT:       return new CatStrExpression(lhs, rhs);
                case ADD:       return new AddExpression(lhs, rhs);
                case SUB:       return new SubExpression(lhs, rhs);
                case MUL:       return new MulExpression(lhs, rhs);
                case DIV:       return new DivExpression(lhs, rhs);
                case MOD:       return new ModExpression(lhs, rhs);
                case POW:       return new PowExpression(lhs, rhs);
                case MORE:      return new MoreExpression(lhs, rhs);
                case MORE_EQ:   return new MoreEQExpression(lhs, rhs);
                case LESS:      return new LessExpression(lhs, rhs);
                case LESS_EQ:   return new LessEQExpression(lhs, rhs);
                case EQ:        return new EQExpression(lhs, rhs);
                case NEQ:       return new NotEQExpression(lhs, rhs);
                case AND:       return new AndExpression(lhs, rhs);
                case OR:        return new OrExpression(lhs, rhs);
                case XOR:       return new XorExpression(lhs, rhs);
                case DECL_VAR:  return new DeclVarExpression((TermExpression) lhs, rhs);
            }

            throw new ParseException(tok.value() + " is not binary");
        }

        Expression parseUnary(Token.Static tok, Expression exp) {
            switch (tok) {
                case ADD: return new UnaryAddExpression(exp);
                case SUB: return new UnaryMinExpression(exp);
                case NOT: return new NotExpression(exp);
                case INC: return new IncExpression(exp);
                case DEC: return new DecExpression(exp);

                case ALGORITHME: return parseAlgorithme(exp);
            }

            throw new ParseException(tok.value() + " is not unary");
        }

        BlockExpression parseBlock() {
            isNextToken(BLOCK_START);

            BlockExpression block = BlockExpression.newEmpty();
            while (!tokens.isEmpty()) {
                Expression exp = parse();
                block.append(exp);
            }
            return block;
        }

        ListExpression parseList() {
            ListExpression list = ListExpression.newEmpty();
            while (!tokens.isEmpty()) {
                LinkedList<Token> toks = new LinkedList<Token>();
                while (!tokens.isEmpty()) {
                    Token tok = tokens.removeFirst();
                    if (tok == COMMA) {
                        break;
                    }
                    toks.add(tok);
                }
                Expression exp = new Context(toks).parse();
                list.addExp(exp);
            }
            return list;
        }

        Expression parseIf() {
            isNextToken(IF_START);
            Expression test      = parse();

            Context bodyCtx = takeUntil(ELSE, IF_END);
            Expression otherwise = NilExpression.i;
            if (isNextToken(ELSE)) {
                Context otherwiseCtx = takeUntil(IF_END);
                otherwise = otherwiseCtx.parseBlock();
            }
            Expression body = bodyCtx.parseBlock();

            return new IfExpression(test, body, otherwise);
        }

        Expression parseWhile() {
            isNextToken(WHILE_START);
            Expression test = parse();
            Expression body = takeUntil(WHILE_END).parse();
            return new WhileExpression(test, body);
        }

        Expression parseFor() {
            isNextToken(FOR_START);
            Expression[] args = betweenParens().split(SEMICOLON, 3);
            BlockExpression body = takeUntil(FOR_END).parseBlock();

            Expression init = args[0];
            Expression test = args[1];
            Expression end = args[2];

            body.append(end);

            return BlockExpression.of(init, new WhileExpression(test, body));
        }

        Expression[] split(Token sep, int len) {
            Expression[] result = new Expression[len];
            for (int i = 0; i < len; i++) {
                LinkedList<Token> toks = new LinkedList<Token>();
                while (!tokens.isEmpty()) {
                    Token tok = tokens.removeFirst();
                    if (tok == sep) {
                        break;
                    }
                    toks.add(tok);
                }
                Expression exp = new Context(toks).parse();
                result[i] = exp;
            }
            return result;
        }

        Expression parseForeach() {
            isNextToken(EACH_START);

            Expression exp = betweenParens().parse();
            if (!(exp instanceof DeclVarExpression)) {
                throw new ParseException("expected a %s but got %s", DECL_VAR, exp.getReflectiveName());
            }

            DeclVarExpression generator = (DeclVarExpression) exp;
            Expression body             = takeUntil(EACH_END).parseBlock();

            return new ForeachExpression(generator, body);
        }

        Expression parseDeclFun() {
            isNextToken(DECL_FUN);
            TermExpression id = (TermExpression) Literals.parse((Dynamic) popToken());
            List<TermExpression> args = betweenParens().parseList().ofTerms();
            Expression variables = NilExpression.i;
            if (isNextToken(VARIABLES)) {
                variables = takeUntil(BLOCK_START).parseBlock();
            }
            BlockExpression body = takeUntil(BLOCK_END).parseBlock();
            body.prepend(variables);
            return new DeclFunExpression(id, args, body);
        }

        Expression parseAlgorithme(Expression nomAlgo) {
            return parse();
        }
    }
}
