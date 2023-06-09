package com.antlersoft.classwriter;

import java.util.EmptyStackException;
import java.util.Stack;

public class Dup2X2Stack implements ProcessStack {

    public Dup2X2Stack() {
    }

    public Stack stackUpdate(Stack old_stack) throws CodeCheckException {
        try {
            Stack new_stack = (Stack) old_stack.clone();
            Object top1 = new_stack.pop();
            if (top1 == ProcessStack.CAT1) {
                Object top2 = new_stack.pop();
                if (top2 != ProcessStack.CAT1) throw new CodeCheckException("dup2_x2; second object is not CAT1");
                Object top3 = new_stack.pop();
                if (top3 == ProcessStack.CAT2) {
                    new_stack.push(top2);
                    new_stack.push(top1);
                    new_stack.push(top3);
                    new_stack.push(top2);
                    new_stack.push(top1);
                } else {
                    Object top4 = new_stack.pop();
                    if (top4 != ProcessStack.CAT1) throw new CodeCheckException("dup2_x2; fourth object is not CAT1");
                    new_stack.push(top2);
                    new_stack.push(top1);
                    new_stack.push(top4);
                    new_stack.push(top3);
                    new_stack.push(top2);
                    new_stack.push(top1);
                }
            } else {
                Object top2 = new_stack.pop();
                if (top2 == ProcessStack.CAT2) {
                    new_stack.push(top1);
                    new_stack.push(top2);
                    new_stack.push(top1);
                } else {
                    Object top3 = new_stack.pop();
                    if (top3 != ProcessStack.CAT1) throw new CodeCheckException("dup2_x2; object under CAT2, CAT1 is not CAT1");
                    new_stack.push(top1);
                    new_stack.push(top3);
                    new_stack.push(top2);
                    new_stack.push(top1);
                }
            }
            return new_stack;
        } catch (EmptyStackException ese) {
            throw new CodeCheckException("dup2_x2; stack not deep enough");
        }
    }
}
