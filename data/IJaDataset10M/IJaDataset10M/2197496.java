package org.codecover.instrumentation.xampil.parser;

import org.codecover.instrumentation.xampil.syntaxtree.*;
import java.util.Vector;

public class XampilParser implements XampilParserConstants {

    private InstrumentableItemCounter counter = new InstrumentableItemCounter();

    static interface IntegerContainer {

        public void set(int value);

        public int get();
    }

    static class RealIntegerContainer implements IntegerContainer {

        int value = 0;

        public void set(int value) {
            this.value = value;
        }

        public int get() {
            return this.value;
        }
    }

    static class DummyIntegerContainer implements IntegerContainer {

        public void set(int value) {
        }

        public int get() {
            return 0;
        }
    }

    static final DummyIntegerContainer DUMMY_CONTAINER = new DummyIntegerContainer();

    public final CompilationUnit CompilationUnit(InstrumentableItemCounter counter) throws ParseException {
        Declaration n0;
        Program n1;
        NodeOptional n2 = new NodeOptional();
        NodeToken n3;
        Token n4;
        NodeToken n5;
        Token n6;
        this.counter = counter;
        n0 = Declaration();
        n1 = Program();
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case EOL:
                n4 = jj_consume_token(EOL);
                n3 = JTBToolkit.makeNodeToken(n4);
                n2.addNode(n3);
                break;
            default:
                jj_la1[0] = jj_gen;
                ;
        }
        n6 = jj_consume_token(0);
        n6.beginColumn++;
        n6.endColumn++;
        n5 = JTBToolkit.makeNodeToken(n6);
        this.counter = new InstrumentableItemCounter();
        {
            if (true) return new CompilationUnit(n0, n1, n2, n5);
        }
        throw new Error("Missing return statement in function");
    }

    public final Declaration Declaration() throws ParseException {
        NodeToken n0;
        Token n1;
        NodeToken n2;
        Token n3;
        NodeListOptional n4 = new NodeListOptional();
        SimpleDeclaration n5;
        n1 = jj_consume_token(DECLARATION);
        n0 = JTBToolkit.makeNodeToken(n1);
        n3 = jj_consume_token(EOL);
        n2 = JTBToolkit.makeNodeToken(n3);
        label_1: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case BOOLEAN:
                case INTEGER:
                case STRING:
                    ;
                    break;
                default:
                    jj_la1[1] = jj_gen;
                    break label_1;
            }
            n5 = SimpleDeclaration();
            n4.addNode(n5);
        }
        n4.nodes.trimToSize();
        {
            if (true) return new Declaration(n0, n2, n4);
        }
        throw new Error("Missing return statement in function");
    }

    public final SimpleDeclaration SimpleDeclaration() throws ParseException {
        NodeChoice n0;
        NodeToken n1;
        Token n2;
        NodeToken n3;
        Token n4;
        NodeToken n5;
        Token n6;
        NodeToken n7;
        Token n8;
        NodeOptional n9 = new NodeOptional();
        NodeSequence n10;
        NodeToken n11;
        Token n12;
        NodeChoice n13;
        NodeToken n14;
        Token n15;
        NodeToken n16;
        Token n17;
        NodeToken n18;
        Token n19;
        NodeToken n20;
        Token n21;
        NodeToken n22;
        Token n23;
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case BOOLEAN:
                n2 = jj_consume_token(BOOLEAN);
                n1 = JTBToolkit.makeNodeToken(n2);
                n0 = new NodeChoice(n1, 0);
                break;
            case INTEGER:
                n4 = jj_consume_token(INTEGER);
                n3 = JTBToolkit.makeNodeToken(n4);
                n0 = new NodeChoice(n3, 1);
                break;
            case STRING:
                n6 = jj_consume_token(STRING);
                n5 = JTBToolkit.makeNodeToken(n6);
                n0 = new NodeChoice(n5, 2);
                break;
            default:
                jj_la1[2] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        n8 = jj_consume_token(IDENTIFIER);
        n7 = JTBToolkit.makeNodeToken(n8);
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case ASSIGN:
                n10 = new NodeSequence(2);
                n12 = jj_consume_token(ASSIGN);
                n11 = JTBToolkit.makeNodeToken(n12);
                n10.addNode(n11);
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case INTEGER_LITERAL:
                        n15 = jj_consume_token(INTEGER_LITERAL);
                        n14 = JTBToolkit.makeNodeToken(n15);
                        n13 = new NodeChoice(n14, 0);
                        break;
                    case STRING_LITERAL:
                        n17 = jj_consume_token(STRING_LITERAL);
                        n16 = JTBToolkit.makeNodeToken(n17);
                        n13 = new NodeChoice(n16, 1);
                        break;
                    case TRUE:
                        n19 = jj_consume_token(TRUE);
                        n18 = JTBToolkit.makeNodeToken(n19);
                        n13 = new NodeChoice(n18, 2);
                        break;
                    case FALSE:
                        n21 = jj_consume_token(FALSE);
                        n20 = JTBToolkit.makeNodeToken(n21);
                        n13 = new NodeChoice(n20, 3);
                        break;
                    default:
                        jj_la1[3] = jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                n10.addNode(n13);
                n9.addNode(n10);
                break;
            default:
                jj_la1[4] = jj_gen;
                ;
        }
        n23 = jj_consume_token(EOL);
        n22 = JTBToolkit.makeNodeToken(n23);
        {
            if (true) return new SimpleDeclaration(n0, n7, n9, n22);
        }
        throw new Error("Missing return statement in function");
    }

    public final Program Program() throws ParseException {
        NodeToken n0;
        Token n1;
        NodeToken n2;
        Token n3;
        NodeListOptional n4 = new NodeListOptional();
        Statement n5;
        NodeToken n6;
        Token n7;
        n1 = jj_consume_token(PROGRAM);
        n0 = JTBToolkit.makeNodeToken(n1);
        n3 = jj_consume_token(EOL);
        n2 = JTBToolkit.makeNodeToken(n3);
        label_2: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case IF:
                case WHILE:
                case SWITCH:
                case FILE:
                case IDENTIFIER:
                    ;
                    break;
                default:
                    jj_la1[5] = jj_gen;
                    break label_2;
            }
            n5 = Statement();
            n4.addNode(n5);
        }
        n4.nodes.trimToSize();
        n7 = jj_consume_token(ENDPROGRAM);
        n6 = JTBToolkit.makeNodeToken(n7);
        {
            if (true) return new Program(n0, n2, n4, n6);
        }
        throw new Error("Missing return statement in function");
    }

    public final Statement Statement() throws ParseException {
        NodeChoice n0;
        AssignmentStatement n1;
        IfStatement n2;
        WhileStatement n3;
        SwitchStatement n4;
        FileStatement n5;
        this.counter.incrementStatementCount();
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case IDENTIFIER:
                n1 = AssignmentStatement();
                n0 = new NodeChoice(n1, 0);
                break;
            case IF:
                n2 = IfStatement();
                n0 = new NodeChoice(n2, 1);
                break;
            case WHILE:
                n3 = WhileStatement();
                n0 = new NodeChoice(n3, 2);
                break;
            case SWITCH:
                n4 = SwitchStatement();
                n0 = new NodeChoice(n4, 3);
                break;
            case FILE:
                n5 = FileStatement();
                n0 = new NodeChoice(n5, 4);
                break;
            default:
                jj_la1[6] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        {
            if (true) return new Statement(n0);
        }
        throw new Error("Missing return statement in function");
    }

    public final AssignmentStatement AssignmentStatement() throws ParseException {
        NodeToken n0;
        Token n1;
        NodeToken n2;
        Token n3;
        Expression n4;
        NodeToken n5;
        Token n6;
        n1 = jj_consume_token(IDENTIFIER);
        n0 = JTBToolkit.makeNodeToken(n1);
        n3 = jj_consume_token(ASSIGN);
        n2 = JTBToolkit.makeNodeToken(n3);
        n4 = Expression(DUMMY_CONTAINER);
        n6 = jj_consume_token(EOL);
        n5 = JTBToolkit.makeNodeToken(n6);
        {
            if (true) return new AssignmentStatement(n0, n2, n4, n5);
        }
        throw new Error("Missing return statement in function");
    }

    public final IfStatement IfStatement() throws ParseException {
        NodeToken n0;
        Token n1;
        Expression n2;
        NodeToken n3;
        Token n4;
        NodeToken n5;
        Token n6;
        NodeListOptional n7 = new NodeListOptional();
        Statement n8;
        NodeOptional n9 = new NodeOptional();
        NodeSequence n10;
        NodeToken n11;
        Token n12;
        NodeToken n13;
        Token n14;
        NodeListOptional n15;
        Statement n16;
        NodeToken n17;
        Token n18;
        NodeToken n19;
        Token n20;
        RealIntegerContainer basicBooleanCounter = new RealIntegerContainer();
        n1 = jj_consume_token(IF);
        n0 = JTBToolkit.makeNodeToken(n1);
        n2 = Expression(basicBooleanCounter);
        this.counter.incrementConditionCount(basicBooleanCounter.get());
        n4 = jj_consume_token(THEN);
        n3 = JTBToolkit.makeNodeToken(n4);
        n6 = jj_consume_token(EOL);
        n5 = JTBToolkit.makeNodeToken(n6);
        this.counter.incrementBranchCount();
        label_3: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case IF:
                case WHILE:
                case SWITCH:
                case FILE:
                case IDENTIFIER:
                    ;
                    break;
                default:
                    jj_la1[7] = jj_gen;
                    break label_3;
            }
            n8 = Statement();
            n7.addNode(n8);
        }
        n7.nodes.trimToSize();
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case ELSE:
                n15 = new NodeListOptional();
                n10 = new NodeSequence(4);
                n12 = jj_consume_token(ELSE);
                n11 = JTBToolkit.makeNodeToken(n12);
                n10.addNode(n11);
                n14 = jj_consume_token(EOL);
                n13 = JTBToolkit.makeNodeToken(n14);
                n10.addNode(n13);
                this.counter.incrementBranchCount();
                label_4: while (true) {
                    switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                        case IF:
                        case WHILE:
                        case SWITCH:
                        case FILE:
                        case IDENTIFIER:
                            ;
                            break;
                        default:
                            jj_la1[8] = jj_gen;
                            break label_4;
                    }
                    n16 = Statement();
                    n15.addNode(n16);
                }
                n15.nodes.trimToSize();
                n10.addNode(n15);
                n9.addNode(n10);
                break;
            default:
                jj_la1[9] = jj_gen;
                ;
        }
        n18 = jj_consume_token(ENDIF);
        n17 = JTBToolkit.makeNodeToken(n18);
        n20 = jj_consume_token(EOL);
        n19 = JTBToolkit.makeNodeToken(n20);
        {
            if (true) return new IfStatement(n0, n2, n3, n5, n7, n9, n17, n19);
        }
        throw new Error("Missing return statement in function");
    }

    public final WhileStatement WhileStatement() throws ParseException {
        NodeToken n0;
        Token n1;
        Expression n2;
        NodeToken n3;
        Token n4;
        NodeToken n5;
        Token n6;
        NodeListOptional n7 = new NodeListOptional();
        Statement n8;
        NodeToken n9;
        Token n10;
        NodeToken n11;
        Token n12;
        this.counter.incrementLoopCount();
        RealIntegerContainer basicBooleanCounter = new RealIntegerContainer();
        n1 = jj_consume_token(WHILE);
        n0 = JTBToolkit.makeNodeToken(n1);
        n2 = Expression(basicBooleanCounter);
        this.counter.incrementConditionCount(basicBooleanCounter.get());
        n4 = jj_consume_token(DO);
        n3 = JTBToolkit.makeNodeToken(n4);
        n6 = jj_consume_token(EOL);
        n5 = JTBToolkit.makeNodeToken(n6);
        label_5: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case IF:
                case WHILE:
                case SWITCH:
                case FILE:
                case IDENTIFIER:
                    ;
                    break;
                default:
                    jj_la1[10] = jj_gen;
                    break label_5;
            }
            n8 = Statement();
            n7.addNode(n8);
        }
        n7.nodes.trimToSize();
        n10 = jj_consume_token(ENDWHILE);
        n9 = JTBToolkit.makeNodeToken(n10);
        n12 = jj_consume_token(EOL);
        n11 = JTBToolkit.makeNodeToken(n12);
        {
            if (true) return new WhileStatement(n0, n2, n3, n5, n7, n9, n11);
        }
        throw new Error("Missing return statement in function");
    }

    public final SwitchStatement SwitchStatement() throws ParseException {
        NodeToken n0;
        Token n1;
        NodeToken n2;
        Token n3;
        NodeToken n4;
        Token n5;
        NodeList n6 = new NodeList();
        NodeSequence n7;
        NodeToken n8;
        Token n9;
        Expression n10;
        NodeToken n11;
        Token n12;
        NodeOptional n13;
        NodeToken n14;
        Token n15;
        NodeListOptional n16;
        Statement n17;
        NodeToken n18;
        Token n19;
        NodeToken n20;
        Token n21;
        NodeOptional n22 = new NodeOptional();
        NodeSequence n23;
        NodeToken n24;
        Token n25;
        NodeToken n26;
        Token n27;
        NodeOptional n28;
        NodeToken n29;
        Token n30;
        NodeListOptional n31;
        Statement n32;
        NodeToken n33;
        Token n34;
        NodeToken n35;
        Token n36;
        NodeToken n37;
        Token n38;
        NodeToken n39;
        Token n40;
        n1 = jj_consume_token(SWITCH);
        n0 = JTBToolkit.makeNodeToken(n1);
        n3 = jj_consume_token(IDENTIFIER);
        n2 = JTBToolkit.makeNodeToken(n3);
        n5 = jj_consume_token(EOL);
        n4 = JTBToolkit.makeNodeToken(n5);
        label_6: while (true) {
            n13 = new NodeOptional();
            n16 = new NodeListOptional();
            n7 = new NodeSequence(8);
            n9 = jj_consume_token(CASE);
            n8 = JTBToolkit.makeNodeToken(n9);
            n7.addNode(n8);
            n10 = Expression(DUMMY_CONTAINER);
            n7.addNode(n10);
            n12 = jj_consume_token(COLON);
            n11 = JTBToolkit.makeNodeToken(n12);
            n7.addNode(n11);
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case EOL:
                    n15 = jj_consume_token(EOL);
                    n14 = JTBToolkit.makeNodeToken(n15);
                    n13.addNode(n14);
                    break;
                default:
                    jj_la1[11] = jj_gen;
                    ;
            }
            n7.addNode(n13);
            this.counter.incrementBranchCount();
            label_7: while (true) {
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case IF:
                    case WHILE:
                    case SWITCH:
                    case FILE:
                    case IDENTIFIER:
                        ;
                        break;
                    default:
                        jj_la1[12] = jj_gen;
                        break label_7;
                }
                n17 = Statement();
                n16.addNode(n17);
            }
            n16.nodes.trimToSize();
            n7.addNode(n16);
            n19 = jj_consume_token(ENDCASE);
            n18 = JTBToolkit.makeNodeToken(n19);
            n7.addNode(n18);
            n21 = jj_consume_token(EOL);
            n20 = JTBToolkit.makeNodeToken(n21);
            n7.addNode(n20);
            n6.addNode(n7);
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case CASE:
                    ;
                    break;
                default:
                    jj_la1[13] = jj_gen;
                    break label_6;
            }
        }
        n6.nodes.trimToSize();
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case CASE_DEFAULT:
                n28 = new NodeOptional();
                n31 = new NodeListOptional();
                n23 = new NodeSequence(7);
                n25 = jj_consume_token(CASE_DEFAULT);
                n24 = JTBToolkit.makeNodeToken(n25);
                n23.addNode(n24);
                n27 = jj_consume_token(COLON);
                n26 = JTBToolkit.makeNodeToken(n27);
                n23.addNode(n26);
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case EOL:
                        n30 = jj_consume_token(EOL);
                        n29 = JTBToolkit.makeNodeToken(n30);
                        n28.addNode(n29);
                        break;
                    default:
                        jj_la1[14] = jj_gen;
                        ;
                }
                n23.addNode(n28);
                this.counter.incrementBranchCount();
                label_8: while (true) {
                    switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                        case IF:
                        case WHILE:
                        case SWITCH:
                        case FILE:
                        case IDENTIFIER:
                            ;
                            break;
                        default:
                            jj_la1[15] = jj_gen;
                            break label_8;
                    }
                    n32 = Statement();
                    n31.addNode(n32);
                }
                n31.nodes.trimToSize();
                n23.addNode(n31);
                n34 = jj_consume_token(ENDCASE);
                n33 = JTBToolkit.makeNodeToken(n34);
                n23.addNode(n33);
                n36 = jj_consume_token(EOL);
                n35 = JTBToolkit.makeNodeToken(n36);
                n23.addNode(n35);
                n22.addNode(n23);
                break;
            default:
                jj_la1[16] = jj_gen;
                ;
        }
        n38 = jj_consume_token(ENDSWITCH);
        n37 = JTBToolkit.makeNodeToken(n38);
        n40 = jj_consume_token(EOL);
        n39 = JTBToolkit.makeNodeToken(n40);
        {
            if (true) return new SwitchStatement(n0, n2, n4, n6, n22, n37, n39);
        }
        throw new Error("Missing return statement in function");
    }

    public final FileStatement FileStatement() throws ParseException {
        NodeToken n0;
        Token n1;
        NodeChoice n2;
        NodeToken n3;
        Token n4;
        NodeToken n5;
        Token n6;
        NodeChoice n7;
        NodeToken n8;
        Token n9;
        NodeToken n10;
        Token n11;
        Expression n12;
        NodeToken n13;
        Token n14;
        n1 = jj_consume_token(FILE);
        n0 = JTBToolkit.makeNodeToken(n1);
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case OVERWRITE:
                n4 = jj_consume_token(OVERWRITE);
                n3 = JTBToolkit.makeNodeToken(n4);
                n2 = new NodeChoice(n3, 0);
                break;
            case APPEND:
                n6 = jj_consume_token(APPEND);
                n5 = JTBToolkit.makeNodeToken(n6);
                n2 = new NodeChoice(n5, 1);
                break;
            default:
                jj_la1[17] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case STRING_LITERAL:
                n9 = jj_consume_token(STRING_LITERAL);
                n8 = JTBToolkit.makeNodeToken(n9);
                n7 = new NodeChoice(n8, 0);
                break;
            case IDENTIFIER:
                n11 = jj_consume_token(IDENTIFIER);
                n10 = JTBToolkit.makeNodeToken(n11);
                n7 = new NodeChoice(n10, 1);
                break;
            default:
                jj_la1[18] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        n12 = Expression(DUMMY_CONTAINER);
        n14 = jj_consume_token(EOL);
        n13 = JTBToolkit.makeNodeToken(n14);
        {
            if (true) return new FileStatement(n0, n2, n7, n12, n13);
        }
        throw new Error("Missing return statement in function");
    }

    public final Expression Expression(IntegerContainer basicBooleanCounter) throws ParseException {
        OrExpression n0;
        n0 = OrExpression(basicBooleanCounter);
        {
            if (true) return new Expression(n0);
        }
        throw new Error("Missing return statement in function");
    }

    public final OrExpression OrExpression(IntegerContainer basicBooleanCounter) throws ParseException {
        AndExpression n0;
        NodeListOptional n1 = new NodeListOptional();
        NodeSequence n2;
        NodeToken n3;
        Token n4;
        AndExpression n5;
        n0 = AndExpression(basicBooleanCounter);
        label_9: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case OR:
                    ;
                    break;
                default:
                    jj_la1[19] = jj_gen;
                    break label_9;
            }
            n2 = new NodeSequence(2);
            n4 = jj_consume_token(OR);
            n3 = JTBToolkit.makeNodeToken(n4);
            n2.addNode(n3);
            n5 = AndExpression(basicBooleanCounter);
            n2.addNode(n5);
            n1.addNode(n2);
        }
        n1.nodes.trimToSize();
        {
            if (true) return new OrExpression(n0, n1);
        }
        throw new Error("Missing return statement in function");
    }

    public final AndExpression AndExpression(IntegerContainer basicBooleanCounter) throws ParseException {
        NotExpression n0;
        NodeListOptional n1 = new NodeListOptional();
        NodeSequence n2;
        NodeToken n3;
        Token n4;
        NotExpression n5;
        n0 = NotExpression(basicBooleanCounter);
        label_10: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case AND:
                    ;
                    break;
                default:
                    jj_la1[20] = jj_gen;
                    break label_10;
            }
            n2 = new NodeSequence(2);
            n4 = jj_consume_token(AND);
            n3 = JTBToolkit.makeNodeToken(n4);
            n2.addNode(n3);
            n5 = NotExpression(basicBooleanCounter);
            n2.addNode(n5);
            n1.addNode(n2);
        }
        n1.nodes.trimToSize();
        {
            if (true) return new AndExpression(n0, n1);
        }
        throw new Error("Missing return statement in function");
    }

    public final NotExpression NotExpression(IntegerContainer basicBooleanCounter) throws ParseException {
        NodeOptional n0 = new NodeOptional();
        NodeToken n1;
        Token n2;
        EqualityExpression n3;
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case NOT:
                n2 = jj_consume_token(NOT);
                n1 = JTBToolkit.makeNodeToken(n2);
                n0.addNode(n1);
                break;
            default:
                jj_la1[21] = jj_gen;
                ;
        }
        n3 = EqualityExpression(basicBooleanCounter);
        {
            if (true) return new NotExpression(n0, n3);
        }
        throw new Error("Missing return statement in function");
    }

    public final EqualityExpression EqualityExpression(IntegerContainer basicBooleanCounter) throws ParseException {
        RelationalExpression n0;
        NodeOptional n1 = new NodeOptional();
        NodeSequence n2;
        NodeChoice n3;
        NodeToken n4;
        Token n5;
        NodeToken n6;
        Token n7;
        RelationalExpression n8;
        int basicBooleanCountBefore = basicBooleanCounter.get();
        n0 = RelationalExpression(basicBooleanCounter);
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case EQ:
            case NEQ:
                n2 = new NodeSequence(3);
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case EQ:
                        n5 = jj_consume_token(EQ);
                        n4 = JTBToolkit.makeNodeToken(n5);
                        n3 = new NodeChoice(n4, 0);
                        break;
                    case NEQ:
                        n7 = jj_consume_token(NEQ);
                        n6 = JTBToolkit.makeNodeToken(n7);
                        n3 = new NodeChoice(n6, 1);
                        break;
                    default:
                        jj_la1[22] = jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                n2.addNode(n3);
                n8 = RelationalExpression(basicBooleanCounter);
                n2.addNode(n8);
                basicBooleanCounter.set(basicBooleanCountBefore + 1);
                n1.addNode(n2);
                break;
            default:
                jj_la1[23] = jj_gen;
                ;
        }
        {
            if (true) return new EqualityExpression(n0, n1);
        }
        throw new Error("Missing return statement in function");
    }

    public final RelationalExpression RelationalExpression(IntegerContainer basicBooleanCounter) throws ParseException {
        AdditiveExpression n0;
        NodeOptional n1 = new NodeOptional();
        NodeSequence n2;
        NodeChoice n3;
        NodeToken n4;
        Token n5;
        NodeToken n6;
        Token n7;
        NodeToken n8;
        Token n9;
        NodeToken n10;
        Token n11;
        AdditiveExpression n12;
        int basicBooleanCountBefore = basicBooleanCounter.get();
        n0 = AdditiveExpression(basicBooleanCounter);
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case LT:
            case GT:
            case LE:
            case GE:
                n2 = new NodeSequence(3);
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case LT:
                        n5 = jj_consume_token(LT);
                        n4 = JTBToolkit.makeNodeToken(n5);
                        n3 = new NodeChoice(n4, 0);
                        break;
                    case GT:
                        n7 = jj_consume_token(GT);
                        n6 = JTBToolkit.makeNodeToken(n7);
                        n3 = new NodeChoice(n6, 1);
                        break;
                    case LE:
                        n9 = jj_consume_token(LE);
                        n8 = JTBToolkit.makeNodeToken(n9);
                        n3 = new NodeChoice(n8, 2);
                        break;
                    case GE:
                        n11 = jj_consume_token(GE);
                        n10 = JTBToolkit.makeNodeToken(n11);
                        n3 = new NodeChoice(n10, 3);
                        break;
                    default:
                        jj_la1[24] = jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                n2.addNode(n3);
                n12 = AdditiveExpression(basicBooleanCounter);
                n2.addNode(n12);
                basicBooleanCounter.set(basicBooleanCountBefore + 1);
                n1.addNode(n2);
                break;
            default:
                jj_la1[25] = jj_gen;
                ;
        }
        {
            if (true) return new RelationalExpression(n0, n1);
        }
        throw new Error("Missing return statement in function");
    }

    public final AdditiveExpression AdditiveExpression(IntegerContainer basicBooleanCounter) throws ParseException {
        MultiplicativeExpression n0;
        NodeListOptional n1 = new NodeListOptional();
        NodeSequence n2;
        NodeChoice n3;
        NodeToken n4;
        Token n5;
        NodeToken n6;
        Token n7;
        MultiplicativeExpression n8;
        int basicBooleanCountBefore = basicBooleanCounter.get();
        n0 = MultiplicativeExpression(basicBooleanCounter);
        label_11: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case PLUS:
                case MINUS:
                    ;
                    break;
                default:
                    jj_la1[26] = jj_gen;
                    break label_11;
            }
            n2 = new NodeSequence(3);
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case PLUS:
                    n5 = jj_consume_token(PLUS);
                    n4 = JTBToolkit.makeNodeToken(n5);
                    n3 = new NodeChoice(n4, 0);
                    break;
                case MINUS:
                    n7 = jj_consume_token(MINUS);
                    n6 = JTBToolkit.makeNodeToken(n7);
                    n3 = new NodeChoice(n6, 1);
                    break;
                default:
                    jj_la1[27] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            n2.addNode(n3);
            n8 = MultiplicativeExpression(basicBooleanCounter);
            n2.addNode(n8);
            basicBooleanCounter.set(basicBooleanCountBefore + 1);
            n1.addNode(n2);
        }
        n1.nodes.trimToSize();
        {
            if (true) return new AdditiveExpression(n0, n1);
        }
        throw new Error("Missing return statement in function");
    }

    public final MultiplicativeExpression MultiplicativeExpression(IntegerContainer basicBooleanCounter) throws ParseException {
        BasicExpression n0;
        NodeListOptional n1 = new NodeListOptional();
        NodeSequence n2;
        NodeChoice n3;
        NodeToken n4;
        Token n5;
        NodeToken n6;
        Token n7;
        BasicExpression n8;
        int basicBooleanCountBefore = basicBooleanCounter.get();
        n0 = BasicExpression(basicBooleanCounter);
        label_12: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case STAR:
                case SLASH:
                    ;
                    break;
                default:
                    jj_la1[28] = jj_gen;
                    break label_12;
            }
            n2 = new NodeSequence(3);
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case STAR:
                    n5 = jj_consume_token(STAR);
                    n4 = JTBToolkit.makeNodeToken(n5);
                    n3 = new NodeChoice(n4, 0);
                    break;
                case SLASH:
                    n7 = jj_consume_token(SLASH);
                    n6 = JTBToolkit.makeNodeToken(n7);
                    n3 = new NodeChoice(n6, 1);
                    break;
                default:
                    jj_la1[29] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            n2.addNode(n3);
            n8 = BasicExpression(basicBooleanCounter);
            n2.addNode(n8);
            basicBooleanCounter.set(basicBooleanCountBefore + 1);
            n1.addNode(n2);
        }
        n1.nodes.trimToSize();
        {
            if (true) return new MultiplicativeExpression(n0, n1);
        }
        throw new Error("Missing return statement in function");
    }

    public final BasicExpression BasicExpression(IntegerContainer basicBooleanCounter) throws ParseException {
        NodeChoice n0;
        NodeToken n1;
        Token n2;
        NodeToken n3;
        Token n4;
        NodeToken n5;
        Token n6;
        NodeToken n7;
        Token n8;
        NodeToken n9;
        Token n10;
        NodeSequence n11;
        NodeToken n12;
        Token n13;
        Expression n14;
        NodeToken n15;
        Token n16;
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case IDENTIFIER:
                n2 = jj_consume_token(IDENTIFIER);
                n1 = JTBToolkit.makeNodeToken(n2);
                basicBooleanCounter.set(basicBooleanCounter.get() + 1);
                n0 = new NodeChoice(n1, 0);
                break;
            case INTEGER_LITERAL:
                n4 = jj_consume_token(INTEGER_LITERAL);
                n3 = JTBToolkit.makeNodeToken(n4);
                n0 = new NodeChoice(n3, 1);
                break;
            case STRING_LITERAL:
                n6 = jj_consume_token(STRING_LITERAL);
                n5 = JTBToolkit.makeNodeToken(n6);
                n0 = new NodeChoice(n5, 2);
                break;
            case TRUE:
                n8 = jj_consume_token(TRUE);
                n7 = JTBToolkit.makeNodeToken(n8);
                n0 = new NodeChoice(n7, 3);
                break;
            case FALSE:
                n10 = jj_consume_token(FALSE);
                n9 = JTBToolkit.makeNodeToken(n10);
                n0 = new NodeChoice(n9, 4);
                break;
            case LPAREN:
                n11 = new NodeSequence(3);
                n13 = jj_consume_token(LPAREN);
                n12 = JTBToolkit.makeNodeToken(n13);
                n11.addNode(n12);
                n14 = Expression(basicBooleanCounter);
                n11.addNode(n14);
                n16 = jj_consume_token(RPAREN);
                n15 = JTBToolkit.makeNodeToken(n16);
                n11.addNode(n15);
                n0 = new NodeChoice(n11, 5);
                break;
            default:
                jj_la1[30] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        {
            if (true) return new BasicExpression(n0);
        }
        throw new Error("Missing return statement in function");
    }

    public XampilParserTokenManager token_source;

    public Token token, jj_nt;

    private int jj_ntk;

    private int jj_gen;

    private final int[] jj_la1 = new int[31];

    private static int[] jj_la1_0;

    private static int[] jj_la1_1;

    static {
        jj_la1_0();
        jj_la1_1();
    }

    private static void jj_la1_0() {
        jj_la1_0 = new int[] { 0x8, 0x380, 0x380, 0x30000000, 0x0, 0x424400, 0x424400, 0x424400, 0x424400, 0x1000, 0x424400, 0x8, 0x424400, 0x40000, 0x8, 0x424400, 0x80000, 0x1800000, 0x0, 0x4000000, 0x2000000, 0x8000000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x70000000 };
    }

    private static void jj_la1_1() {
        jj_la1_1 = new int[] { 0x0, 0x0, 0x0, 0x6000, 0x2, 0x1000, 0x1000, 0x1000, 0x1000, 0x0, 0x1000, 0x0, 0x1000, 0x0, 0x0, 0x1000, 0x0, 0x0, 0x5000, 0x0, 0x0, 0x0, 0xc, 0xc, 0xf0, 0xf0, 0x300, 0x300, 0xc00, 0xc00, 0x7000 };
    }

    public XampilParser(CharStream stream) {
        token_source = new XampilParserTokenManager(stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 31; i++) jj_la1[i] = -1;
    }

    public void ReInit(CharStream stream) {
        token_source.ReInit(stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 31; i++) jj_la1[i] = -1;
    }

    public XampilParser(XampilParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 31; i++) jj_la1[i] = -1;
    }

    public void ReInit(XampilParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 31; i++) jj_la1[i] = -1;
    }

    private final Token jj_consume_token(int kind) throws ParseException {
        Token oldToken;
        if ((oldToken = token).next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        if (token.kind == kind) {
            jj_gen++;
            return token;
        }
        token = oldToken;
        jj_kind = kind;
        throw generateParseException();
    }

    public final Token getNextToken() {
        if (token.next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    public final Token getToken(int index) {
        Token t = token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) t = t.next; else t = t.next = token_source.getNextToken();
        }
        return t;
    }

    private final int jj_ntk() {
        if ((jj_nt = token.next) == null) return (jj_ntk = (token.next = token_source.getNextToken()).kind); else return (jj_ntk = jj_nt.kind);
    }

    private java.util.Vector<int[]> jj_expentries = new java.util.Vector<int[]>();

    private int[] jj_expentry;

    private int jj_kind = -1;

    public ParseException generateParseException() {
        jj_expentries.removeAllElements();
        boolean[] la1tokens = new boolean[47];
        for (int i = 0; i < 47; i++) {
            la1tokens[i] = false;
        }
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 31; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                    if ((jj_la1_1[i] & (1 << j)) != 0) {
                        la1tokens[32 + j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 47; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.addElement(jj_expentry);
            }
        }
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = (int[]) jj_expentries.elementAt(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    public final void enable_tracing() {
    }

    public final void disable_tracing() {
    }
}

class JTBToolkit {

    static NodeToken makeNodeToken(Token t) {
        NodeToken node = new NodeToken(t.image.intern(), t.kind, t.beginLine, t.endLine, t.beginColumn, t.endColumn, t.beginOffset, t.endOffset);
        if (t.specialToken == null) return node;
        Vector temp = new Vector();
        Token orig = t;
        while (t.specialToken != null) {
            t = t.specialToken;
            temp.addElement(new NodeToken(t.image.intern(), t.kind, t.beginLine, t.endLine, t.beginColumn, t.endColumn, t.beginOffset, t.endOffset));
        }
        for (int i = temp.size() - 1; i >= 0; --i) node.addSpecial((NodeToken) temp.elementAt(i));
        node.trimSpecials();
        return node;
    }
}
