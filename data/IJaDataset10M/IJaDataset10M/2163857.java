package org.bbop.expression.parser;

public interface ParserTreeConstants {

    public int JJTJEXLSCRIPT = 0;

    public int JJTBLOCK = 1;

    public int JJTEMPTYFUNCTION = 2;

    public int JJTSIZEFUNCTION = 3;

    public int JJTIDENTIFIER = 4;

    public int JJTEXPRESSION = 5;

    public int JJTASSIGNMENT = 6;

    public int JJTVOID = 7;

    public int JJTORNODE = 8;

    public int JJTANDNODE = 9;

    public int JJTBITWISEORNODE = 10;

    public int JJTBITWISEXORNODE = 11;

    public int JJTBITWISEANDNODE = 12;

    public int JJTEQNODE = 13;

    public int JJTNENODE = 14;

    public int JJTLTNODE = 15;

    public int JJTGTNODE = 16;

    public int JJTLENODE = 17;

    public int JJTGENODE = 18;

    public int JJTADDNODE = 19;

    public int JJTSUBTRACTNODE = 20;

    public int JJTMULNODE = 21;

    public int JJTDIVNODE = 22;

    public int JJTMODNODE = 23;

    public int JJTUNARYMINUSNODE = 24;

    public int JJTBITWISECOMPLNODE = 25;

    public int JJTNOTNODE = 26;

    public int JJTPOSTINCREMENTNODE = 27;

    public int JJTPOSTDECREMENTNODE = 28;

    public int JJTPREINCREMENTNODE = 29;

    public int JJTPREDECREMENTNODE = 30;

    public int JJTNULLLITERAL = 31;

    public int JJTTRUENODE = 32;

    public int JJTFALSENODE = 33;

    public int JJTINTEGERLITERAL = 34;

    public int JJTFLOATLITERAL = 35;

    public int JJTSTRINGLITERAL = 36;

    public int JJTEXTENDEDPARAMIDENTIFIER = 37;

    public int JJTGLOBALTAG = 38;

    public int JJTFUNCTIONDEFINITION = 39;

    public int JJTEXPRESSIONEXPRESSION = 40;

    public int JJTSTATEMENTEXPRESSION = 41;

    public int JJTREFERENCEEXPRESSION = 42;

    public int JJTIFSTATEMENT = 43;

    public int JJTWHILESTATEMENT = 44;

    public int JJTFOREACHSTATEMENT = 45;

    public int JJTFORSTATEMENT = 46;

    public int JJTRETURNSTATEMENT = 47;

    public int JJTBREAKSTATEMENT = 48;

    public int JJTEXPORTSTATEMENT = 49;

    public int JJTIMPORTSTATEMENT = 50;

    public int JJTMETHOD = 51;

    public int JJTARRAYACCESS = 52;

    public int JJTSIZEMETHOD = 53;

    public int JJTREFERENCE = 54;

    public String[] jjtNodeName = { "JexlScript", "Block", "EmptyFunction", "SizeFunction", "Identifier", "Expression", "Assignment", "void", "OrNode", "AndNode", "BitwiseOrNode", "BitwiseXorNode", "BitwiseAndNode", "EQNode", "NENode", "LTNode", "GTNode", "LENode", "GENode", "AddNode", "SubtractNode", "MulNode", "DivNode", "ModNode", "UnaryMinusNode", "BitwiseComplNode", "NotNode", "PostIncrementNode", "PostDecrementNode", "PreIncrementNode", "PreDecrementNode", "NullLiteral", "TrueNode", "FalseNode", "IntegerLiteral", "FloatLiteral", "StringLiteral", "ExtendedParamIdentifier", "GlobalTag", "FunctionDefinition", "ExpressionExpression", "StatementExpression", "ReferenceExpression", "IfStatement", "WhileStatement", "ForeachStatement", "ForStatement", "ReturnStatement", "BreakStatement", "ExportStatement", "ImportStatement", "Method", "ArrayAccess", "SizeMethod", "Reference" };
}
