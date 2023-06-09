package org.openscience.cdk.geometry.cip.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.CDKTestCase;
import org.openscience.cdk.geometry.cip.ILigand;
import org.openscience.cdk.geometry.cip.ImplicitHydrogenLigand;
import org.openscience.cdk.geometry.cip.Ligand;
import org.openscience.cdk.geometry.cip.VisitedAtoms;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

/**
 * @cdk.module test-cip
 */
public class AtomicNumberRuleTest extends CDKTestCase {

    static SmilesParser smiles = new SmilesParser(SilentChemObjectBuilder.getInstance());

    static IAtomContainer molecule;

    @BeforeClass
    public static void setup() throws Exception {
        molecule = smiles.parseSmiles("ClC(Br)(I)[H]");
    }

    @Test
    public void testCompare_Identity() {
        ILigand ligand = new Ligand(molecule, new VisitedAtoms(), molecule.getAtom(1), molecule.getAtom(0));
        ISequenceSubRule<ILigand> rule = new AtomicNumberRule();
        Assert.assertEquals(0, rule.compare(ligand, ligand));
    }

    @Test
    public void testCompare() {
        ILigand ligand1 = new Ligand(molecule, new VisitedAtoms(), molecule.getAtom(1), molecule.getAtom(0));
        ILigand ligand2 = new Ligand(molecule, new VisitedAtoms(), molecule.getAtom(1), molecule.getAtom(2));
        ISequenceSubRule<ILigand> rule = new AtomicNumberRule();
        Assert.assertEquals(-1, rule.compare(ligand1, ligand2));
        Assert.assertEquals(1, rule.compare(ligand2, ligand1));
    }

    @Test
    public void testOrder() {
        VisitedAtoms visitedAtoms = new VisitedAtoms();
        ILigand ligand1 = new Ligand(molecule, visitedAtoms, molecule.getAtom(1), molecule.getAtom(4));
        ILigand ligand2 = new Ligand(molecule, visitedAtoms, molecule.getAtom(1), molecule.getAtom(3));
        ILigand ligand3 = new Ligand(molecule, visitedAtoms, molecule.getAtom(1), molecule.getAtom(2));
        ILigand ligand4 = new Ligand(molecule, visitedAtoms, molecule.getAtom(1), molecule.getAtom(0));
        List<ILigand> ligands = new ArrayList<ILigand>();
        ligands.add(ligand1);
        ligands.add(ligand2);
        ligands.add(ligand3);
        ligands.add(ligand4);
        Collections.sort(ligands, new AtomicNumberRule());
        Assert.assertEquals("H", ligands.get(0).getLigandAtom().getSymbol());
        Assert.assertEquals("Cl", ligands.get(1).getLigandAtom().getSymbol());
        Assert.assertEquals("Br", ligands.get(2).getLigandAtom().getSymbol());
        Assert.assertEquals("I", ligands.get(3).getLigandAtom().getSymbol());
    }

    @Test
    public void testImplicitHydrogen_Same() {
        ILigand ligand1 = new ImplicitHydrogenLigand(molecule, new VisitedAtoms(), molecule.getAtom(1));
        ILigand ligand2 = new Ligand(molecule, new VisitedAtoms(), molecule.getAtom(1), molecule.getAtom(4));
        ISequenceSubRule<ILigand> rule = new AtomicNumberRule();
        Assert.assertEquals(0, rule.compare(ligand1, ligand2));
        Assert.assertEquals(0, rule.compare(ligand2, ligand1));
    }

    @Test
    public void testImplicitHydrogen() {
        ILigand ligand1 = new ImplicitHydrogenLigand(molecule, new VisitedAtoms(), molecule.getAtom(1));
        ILigand ligand2 = new Ligand(molecule, new VisitedAtoms(), molecule.getAtom(1), molecule.getAtom(3));
        ISequenceSubRule<ILigand> rule = new AtomicNumberRule();
        Assert.assertEquals(-1, rule.compare(ligand1, ligand2));
        Assert.assertEquals(1, rule.compare(ligand2, ligand1));
    }
}
