package org.openscience.cdk.reaction.mechanism;

import java.util.ArrayList;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMapping;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.reaction.IReactionMechanism;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.BondManipulator;

/**
 * <p>This mechanism adduct together two fragments due to a double bond. 
 * The second fragment will be deficient in charge.
 * It returns the reaction mechanism which has been cloned the IMolecule.</p>
 * <p>This reaction could be represented as A=B + [C+] => [A+]-B-C</p>
 * 
 * 
 * @author         miguelrojasch
 * @cdk.created    2008-02-10
 * @cdk.module     reaction
 *
 */
@TestClass(value = "org.openscience.cdk.reaction.mechanism.AdductionPBMechanismTest")
public class AdductionPBMechanism implements IReactionMechanism {

    /** 
     * Initiates the process for the given mechanism. The atoms and bonds to apply are mapped between
     * reactants and products. 
     *
     * @param moleculeSet The IMolecule to apply the mechanism
     * @param atomList    The list of atoms taking part in the mechanism. Only allowed three atoms
     * @param bondList    The list of bonds taking part in the mechanism. Only allowed one bond
     * 
     * @return            The Reaction mechanism
     * 
	 */
    @TestMethod(value = "testInitiate_IMoleculeSet_ArrayList_ArrayList")
    public IReaction initiate(IMoleculeSet moleculeSet, ArrayList<IAtom> atomList, ArrayList<IBond> bondList) throws CDKException {
        CDKAtomTypeMatcher atMatcher = CDKAtomTypeMatcher.getInstance(moleculeSet.getBuilder());
        if (moleculeSet.getMoleculeCount() != 2) {
            throw new CDKException("AdductionPBMechanism expects two IMolecule's");
        }
        if (atomList.size() != 3) {
            throw new CDKException("AdductionPBMechanism expects two atoms in the ArrayList");
        }
        if (bondList.size() != 1) {
            throw new CDKException("AdductionPBMechanism don't expect bonds in the ArrayList");
        }
        IMolecule molecule1 = moleculeSet.getMolecule(0);
        IMolecule molecule2 = moleculeSet.getMolecule(1);
        IMolecule reactantCloned;
        try {
            reactantCloned = (IMolecule) moleculeSet.getMolecule(0).clone();
            reactantCloned.add((IAtomContainer) moleculeSet.getMolecule(1).clone());
        } catch (CloneNotSupportedException e) {
            throw new CDKException("Could not clone IMolecule!", e);
        }
        IAtom atom1 = atomList.get(0);
        IAtom atom1C = reactantCloned.getAtom(molecule1.getAtomNumber(atom1));
        IAtom atom2 = atomList.get(1);
        IAtom atom2C = reactantCloned.getAtom(molecule1.getAtomNumber(atom2));
        IAtom atom3 = atomList.get(2);
        IAtom atom3C = reactantCloned.getAtom(molecule1.getAtomCount() + molecule2.getAtomNumber(atom3));
        IBond bond1 = bondList.get(0);
        int posBond1 = moleculeSet.getMolecule(0).getBondNumber(bond1);
        BondManipulator.decreaseBondOrder(reactantCloned.getBond(posBond1));
        IBond newBond = molecule1.getBuilder().newInstance(IBond.class, atom2C, atom3C, IBond.Order.SINGLE);
        reactantCloned.addBond(newBond);
        int charge = atom1C.getFormalCharge();
        atom1C.setFormalCharge(charge + 1);
        atom1C.setHybridization(null);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(reactantCloned);
        IAtomType type = atMatcher.findMatchingAtomType(reactantCloned, atom1C);
        if (type == null) return null;
        atom2C.setHybridization(null);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(reactantCloned);
        type = atMatcher.findMatchingAtomType(reactantCloned, atom2C);
        if (type == null) return null;
        charge = atom3C.getFormalCharge();
        atom3C.setFormalCharge(charge - 1);
        atom3C.setHybridization(null);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(reactantCloned);
        type = atMatcher.findMatchingAtomType(reactantCloned, atom3C);
        if (type == null) return null;
        IReaction reaction = DefaultChemObjectBuilder.getInstance().newInstance(IReaction.class);
        reaction.addReactant(molecule1);
        for (IAtom atom : molecule1.atoms()) {
            IMapping mapping = DefaultChemObjectBuilder.getInstance().newInstance(IMapping.class, atom, reactantCloned.getAtom(molecule1.getAtomNumber(atom)));
            reaction.addMapping(mapping);
        }
        for (IAtom atom : molecule2.atoms()) {
            IMapping mapping = DefaultChemObjectBuilder.getInstance().newInstance(IMapping.class, atom, reactantCloned.getAtom(molecule2.getAtomNumber(atom)));
            reaction.addMapping(mapping);
        }
        reaction.addProduct(reactantCloned);
        return reaction;
    }
}
