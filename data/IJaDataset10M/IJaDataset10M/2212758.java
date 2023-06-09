package edu.byu.ece.edif.tools.sterilize.lutreplace.logicLutRam.RAM;

import byucc.jhdl.Logic.Logic;
import byucc.jhdl.Xilinx.Virtex.fdcpe;
import byucc.jhdl.Xilinx.Virtex.fdpe;
import byucc.jhdl.base.CellInterface;
import byucc.jhdl.base.Node;
import byucc.jhdl.base.Wire;

/**
 * Logic implementation of a RAM32X2D
 * 
 * @author Nathan Rollins
 * @version $Id: RAM32X2D_logic.java 41 2008-05-10 13:28:22Z mrspud $
 */
public class RAM32X2D_logic extends Logic {

    public static CellInterface cell_interface[] = { in("WE", 1), in("D0", 1), in("D1", 1), in("WCLK", 1), in("A0", 1), in("A1", 1), in("A2", 1), in("A3", 1), in("A4", 1), in("DPRA0", 1), in("DPRA1", 1), in("DPRA2", 1), in("DPRA3", 1), in("DPRA4", 1), out("SPO0", 1), out("SPO1", 1), out("DPO0", 1), out("DPO1", 1) };

    protected boolean cellInterfaceDeterminesUniqueNetlistStructure() {
        return true;
    }

    public RAM32X2D_logic(Node parent, Wire we, Wire d0, Wire d1, Wire wclk, Wire a0, Wire a1, Wire a2, Wire a3, Wire a4, Wire dpra0, Wire dpra1, Wire dpra2, Wire dpra3, Wire dpra4, Wire spo0, Wire spo1, Wire dpo0, Wire dpo1, int init1, int init2) {
        super(parent);
        connect("WE", we);
        connect("D0", d0);
        connect("D1", d1);
        connect("WCLK", wclk);
        connect("A0", a0);
        connect("A1", a1);
        connect("A2", a2);
        connect("A3", a3);
        connect("A4", a4);
        connect("DPRA0", dpra0);
        connect("DPRA1", dpra1);
        connect("DPRA2", dpra2);
        connect("DPRA3", dpra3);
        connect("DPRA4", dpra4);
        connect("SPO0", spo0);
        connect("SPO1", spo1);
        connect("DPO0", dpo0);
        connect("DPO1", dpo1);
        int MIN = 32;
        int WIDTH = 32;
        int[] inits = { init1, init2 };
        Wire abus = concat(a4, a3, a2, a1, a0);
        Wire dbus = concat(d1, d0);
        Wire dprabus = concat(dpra4, dpra3, dpra2, dpra1, dpra0);
        Wire rdec = wire(WIDTH, "rdec");
        Wire wdec = wire(WIDTH, "wdec");
        Wire regout[] = new Wire[2];
        Wire tspo[] = new Wire[2];
        Wire tdpo[] = new Wire[2];
        new decode5x32(this, abus, vcc(), wdec);
        new decode5x32(this, dprabus, vcc(), rdec);
        for (int i = 0; i < 2; i++) {
            regout[i] = wire(WIDTH, "regout_" + i);
            tspo[i] = wire(WIDTH, "tspo_" + i);
            tdpo[i] = wire(WIDTH, "tdpo_" + i);
        }
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < WIDTH; i++) {
                if (((inits[(j * WIDTH / MIN) + (i / MIN)] >> i % MIN) & 1) == 1) {
                    new fdpe(this, wclk, dbus.gw(j), and(we, wdec.gw(i)), gnd(), regout[j].gw(i));
                } else {
                    new fdcpe(this, wclk, dbus.gw(j), and(we, wdec.gw(i)), gnd(), gnd(), regout[j].gw(i));
                }
                and_o(regout[j].gw(i), wdec.gw(i), tspo[j].gw(i));
                and_o(regout[j].gw(i), rdec.gw(i), tdpo[j].gw(i));
            }
        }
        or_o(tspo[0], spo0);
        or_o(tspo[1], spo1);
        or_o(tdpo[0], dpo0);
        or_o(tdpo[1], dpo1);
    }
}
