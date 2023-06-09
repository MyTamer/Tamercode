package org.sableccsupport.scclexer.parser;

import org.sableccsupport.scclexer.node.*;
import org.sableccsupport.scclexer.analysis.*;

class TokenIndex extends AnalysisAdapter {

    int index;

    @Override
    public void caseTPkgId(@SuppressWarnings("unused") TPkgId node) {
        this.index = 0;
    }

    @Override
    public void caseTPackage(@SuppressWarnings("unused") TPackage node) {
        this.index = 1;
    }

    @Override
    public void caseTStates(@SuppressWarnings("unused") TStates node) {
        this.index = 2;
    }

    @Override
    public void caseTHelpers(@SuppressWarnings("unused") THelpers node) {
        this.index = 3;
    }

    @Override
    public void caseTTokens(@SuppressWarnings("unused") TTokens node) {
        this.index = 4;
    }

    @Override
    public void caseTIgnored(@SuppressWarnings("unused") TIgnored node) {
        this.index = 5;
    }

    @Override
    public void caseTProductions(@SuppressWarnings("unused") TProductions node) {
        this.index = 6;
    }

    @Override
    public void caseTAbstract(@SuppressWarnings("unused") TAbstract node) {
        this.index = 7;
    }

    @Override
    public void caseTSyntax(@SuppressWarnings("unused") TSyntax node) {
        this.index = 8;
    }

    @Override
    public void caseTTree(@SuppressWarnings("unused") TTree node) {
        this.index = 9;
    }

    @Override
    public void caseTNew(@SuppressWarnings("unused") TNew node) {
        this.index = 10;
    }

    @Override
    public void caseTNull(@SuppressWarnings("unused") TNull node) {
        this.index = 11;
    }

    @Override
    public void caseTTokenSpecifier(@SuppressWarnings("unused") TTokenSpecifier node) {
        this.index = 12;
    }

    @Override
    public void caseTProductionSpecifier(@SuppressWarnings("unused") TProductionSpecifier node) {
        this.index = 13;
    }

    @Override
    public void caseTDot(@SuppressWarnings("unused") TDot node) {
        this.index = 14;
    }

    @Override
    public void caseTDDot(@SuppressWarnings("unused") TDDot node) {
        this.index = 15;
    }

    @Override
    public void caseTSemicolon(@SuppressWarnings("unused") TSemicolon node) {
        this.index = 16;
    }

    @Override
    public void caseTEqual(@SuppressWarnings("unused") TEqual node) {
        this.index = 17;
    }

    @Override
    public void caseTLBkt(@SuppressWarnings("unused") TLBkt node) {
        this.index = 18;
    }

    @Override
    public void caseTRBkt(@SuppressWarnings("unused") TRBkt node) {
        this.index = 19;
    }

    @Override
    public void caseTLPar(@SuppressWarnings("unused") TLPar node) {
        this.index = 20;
    }

    @Override
    public void caseTRPar(@SuppressWarnings("unused") TRPar node) {
        this.index = 21;
    }

    @Override
    public void caseTLBrace(@SuppressWarnings("unused") TLBrace node) {
        this.index = 22;
    }

    @Override
    public void caseTRBrace(@SuppressWarnings("unused") TRBrace node) {
        this.index = 23;
    }

    @Override
    public void caseTPlus(@SuppressWarnings("unused") TPlus node) {
        this.index = 24;
    }

    @Override
    public void caseTMinus(@SuppressWarnings("unused") TMinus node) {
        this.index = 25;
    }

    @Override
    public void caseTQMark(@SuppressWarnings("unused") TQMark node) {
        this.index = 26;
    }

    @Override
    public void caseTStar(@SuppressWarnings("unused") TStar node) {
        this.index = 27;
    }

    @Override
    public void caseTBar(@SuppressWarnings("unused") TBar node) {
        this.index = 28;
    }

    @Override
    public void caseTComma(@SuppressWarnings("unused") TComma node) {
        this.index = 29;
    }

    @Override
    public void caseTSlash(@SuppressWarnings("unused") TSlash node) {
        this.index = 30;
    }

    @Override
    public void caseTArrow(@SuppressWarnings("unused") TArrow node) {
        this.index = 31;
    }

    @Override
    public void caseTColon(@SuppressWarnings("unused") TColon node) {
        this.index = 32;
    }

    @Override
    public void caseTId(@SuppressWarnings("unused") TId node) {
        this.index = 33;
    }

    @Override
    public void caseTChar(@SuppressWarnings("unused") TChar node) {
        this.index = 34;
    }

    @Override
    public void caseTDecChar(@SuppressWarnings("unused") TDecChar node) {
        this.index = 35;
    }

    @Override
    public void caseTHexChar(@SuppressWarnings("unused") THexChar node) {
        this.index = 36;
    }

    @Override
    public void caseTString(@SuppressWarnings("unused") TString node) {
        this.index = 37;
    }

    @Override
    public void caseTBlank(@SuppressWarnings("unused") TBlank node) {
        this.index = 38;
    }

    @Override
    public void caseTComment(@SuppressWarnings("unused") TComment node) {
        this.index = 39;
    }

    @Override
    public void caseEOF(@SuppressWarnings("unused") EOF node) {
        this.index = 40;
    }
}
