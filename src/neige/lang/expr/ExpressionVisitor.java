package neige.lang.expr;

import neige.lang.expr.binary.BinaryExpression;
import neige.lang.expr.binary.CatStrExpression;
import neige.lang.expr.binary.DeclVarExpression;
import neige.lang.expr.binary.bool.*;
import neige.lang.expr.binary.num.*;
import neige.lang.expr.cond.ForeachExpression;
import neige.lang.expr.cond.IfExpression;
import neige.lang.expr.cond.WhileExpression;
import neige.lang.expr.literal.*;
import neige.lang.expr.unary.*;

public abstract class ExpressionVisitor<T> {
    protected abstract T otherwise(Expression exp);

    // TODO visitor lifecycle
    protected void before() {}
    protected void after() {}

    // aliases
    protected T visitUnary(UnaryExpression exp) {
        return otherwise(exp);
    }
    protected T visitLiteral(LiteralExpression exp) {
        return otherwise(exp);
    }
    protected T visitBinary(BinaryExpression exp) {
        return otherwise(exp);
    }
    protected T visitBinaryBool(BinaryExpression exp) {
        return visitBinary(exp);
    }
    protected T visitBinaryNum(BinaryExpression exp) {
        return visitBinary(exp);
    }
    protected T visitFun(FunExpression exp) {
        return visitLiteral(exp);
    }

    // specializations
    public T visitBlock(BlockExpression block) {
        return otherwise(block);
    }
    public T visitDeclFun(DeclFunExpression exp) {
        return otherwise(exp);
    }
    public T visitFunCall(FunCallExpression exp) {
        return otherwise(exp);
    }
    public T visitTerm(TermExpression exp) {
        return otherwise(exp);
    }
    public T visitIf(IfExpression exp) {
        return otherwise(exp);
    }
    public T visitWhile(WhileExpression exp) {
        return otherwise(exp);
    }
    public T visitForeach(ForeachExpression exp) {
        return otherwise(exp);
    }
    public T visitInc(IncExpression exp) {
        return visitUnary(exp);
    }
    public T visitDec(DecExpression exp) {
        return visitUnary(exp);
    }
    public T visitNot(NotExpression exp) {
        return visitUnary(exp);
    }
    public T visitUnaryAdd(UnaryAddExpression exp) {
        return visitUnary(exp);
    }
    public T visitUnaryMin(UnaryMinExpression exp) {
        return visitUnary(exp);
    }
    public T visitCatStr(CatStrExpression exp) {
        return visitBinary(exp);
    }
    public T visitDeclVar(DeclVarExpression exp) {
        return visitBinary(exp);
    }
    public T visitAnd(AndExpression exp) {
        return visitBinaryBool(exp);
    }
    public T visitEQ(EQExpression exp) {
        return visitBinaryBool(exp);
    }
    public T visitNotEQ(NotEQExpression exp) {
        return visitBinaryBool(exp);
    }
    public T visitOr(OrExpression exp) {
        return visitBinaryBool(exp);
    }
    public T visitXor(XorExpression exp) {
        return visitBinaryBool(exp);
    }
    public T visitAdd(AddExpression exp) {
        return visitBinaryNum(exp);
    }
    public T visitDiv(DivExpression exp) {
        return visitBinaryNum(exp);
    }
    public T visitLessEQ(LessEQExpression exp) {
        return visitBinaryBool(exp);
    }
    public T visitLess(LessExpression exp) {
        return visitBinaryBool(exp);
    }
    public T visitMod(ModExpression exp) {
        return visitBinaryNum(exp);
    }
    public T visitMore(MoreExpression exp) {
        return visitBinaryBool(exp);
    }
    public T visitMoreEQ(MoreEQExpression exp) {
        return visitBinaryBool(exp);
    }
    public T visitMul(MulExpression exp) {
        return visitBinaryNum(exp);
    }
    public T visitPow(PowExpression exp) {
        return visitBinaryNum(exp);
    }
    public T visitSub(SubExpression exp) {
        return visitBinaryNum(exp);
    }
    public T visitBool(BoolExpression exp) {
        return visitLiteral(exp);
    }
    public T visitError(ErrorExpression exp) {
        return visitLiteral(exp);
    }
    public T visitFloat(FloatExpression exp) {
        return visitLiteral(exp);
    }
    public T visitInt(IntExpression exp) {
        return visitLiteral(exp);
    }
    public T visitList(ListExpression exp) {
        return visitLiteral(exp);
    }
    public T visitNil(NilExpression exp) {
        return visitLiteral(exp);
    }
    public T visitString(StringExpression exp) {
        return visitLiteral(exp);
    }
    public T visitJavaFun(JavaFunExpression exp) {
        return visitFun(exp);
    }
    public T visitNeigeFun(NeigeFunExpression exp) {
        return visitFun(exp);
    }
}
