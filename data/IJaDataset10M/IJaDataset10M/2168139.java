package edu.byu.ece.edif.arch.xilinx.parts;

public class XilinxPackage {

    public XilinxPackage(String pack) {
        _package = pack;
    }

    public String getPackageName() {
        return _package;
    }

    String _package;
}
