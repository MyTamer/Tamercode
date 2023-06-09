package org.openscience.cdk.reaction.mechanism;

import java.util.ArrayList;
import java.util.List;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.SingleElectron;
import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMapping;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.ISingleElectron;
import org.openscience.cdk.reaction.IReactionMechanism;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.BondManipulator;

/**
 * <p>This mechanism extracts an atom because of the stabilization of a radical. 
 * It returns the reaction mechanism which has been cloned the IMolecule.</p>
 * <p>This reaction could be represented as Y-B-[C*] => [Y*] + B=C</p>
 * 
 * @author         miguelrojasch
 * @cdk.created    2008-02-10
 * @cdk.module     reaction
 *
 */
@TestClass(value = "org.openscience.cdk.reaction.mechanism.RadicalSiteIonizationMechanismTest")
public class RadicalSiteIonizationMechanism implements IReactionMechanism {

    /** 
     * Initiates the process for the given mechanism. The atoms to apply are mapped between
     * reactants and products. 
     *
     * @param moleculeSet The IMolecule to apply the mechanism
     * @param atomList    The list of atoms taking part in the mechanism. Only allowed two atoms.
     *                    The first atom is the atom which contains the ISingleElectron and the second 
     *                    third is the atom which will be removed 
     *                    the first atom
     * @param bondList    The list of bonds taking part in the mechanism. Only allowed one bond.
     * 					  It is the bond which is moved
     * @return            The Reaction mechanism
     * 
	 */
    @TestMethod(value = "testInitiate_IMoleculeSet_ArrayList_ArrayList")
    public IReaction initiate(IMoleculeSet moleculeSet, ArrayList<IAtom> atomList, ArrayList<IBond> bondList) throws CDKException {
        CDKAtomTypeMatcher atMatcher = CDKAtomTypeMatcher.getInstance(moleculeSet.getBuilder());
        if (moleculeSet.getMoleculeCount() != 1) {
            throw new CDKException("RadicalSiteIonizationMechanism only expects one IMolecule");
        }
        if (atomList.size() != 3) {
            throw new CDKException("RadicalSiteIonizationMechanism expects three atoms in the ArrayList");
        }
        if (bondList.size() != 2) {
            throw new CDKException("RadicalSiteIonizationMechanism only expect one bond in the ArrayList");
        }
        IMolecule molecule = moleculeSet.getMolecule(0);
        IMolecule reactantCloned;
        try {
            reactantCloned = (IMolecule) molecule.clone();
        } catch (CloneNotSupportedException e) {
            throw new CDKException("Could not clone IMolecule!", e);
        }
        IAtom atom1 = atomList.get(0);
        IAtom atom1C = reactantCloned.getAtom(molecule.getAtomNumber(atom1));
        IAtom atom2 = atomList.get(1);
        IAtom atom2C = reactantCloned.getAtom(molecule.getAtomNumber(atom2));
        IAtom atom3 = atomList.get(2);
        IAtom atom3C = reactantCloned.getAtom(molecule.getAtomNumber(atom3));
        IBond bond1 = bondList.get(0);
        int posBond1 = molecule.getBondNumber(bond1);
        IBond bond2 = bondList.get(1);
        int posBond2 = molecule.getBondNumber(bond2);
        BondManipulator.increaseBondOrder(reactantCloned.getBond(posBond1));
        reactantCloned.removeBond(reactantCloned.getBond(posBond2));
        List<ISingleElectron> selectron = reactantCloned.getConnectedSingleElectronsList(atom1C);
        reactantCloned.removeSingleElectron(selectron.get(selectron.size() - 1));
        atom1C.setHybridization(null);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(reactantCloned);
        IAtomType type = atMatcher.findMatchingAtomType(reactantCloned, atom1C);
        if (type == null) return null;
        atom2C.setHybridization(null);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(reactantCloned);
        type = atMatcher.findMatchingAtomType(reactantCloned, atom2C);
        if (type == null) return null;
        reactantCloned.addSingleElectron(new SingleElectron(atom3C));
        atom3C.setHybridization(null);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(reactantCloned);
        type = atMatcher.findMatchingAtomType(reactantCloned, atom3C);
        if (type == null) return null;
        IReaction reaction = DefaultChemObjectBuilder.getInstance().newInstance(IReaction.class);
        reaction.addReactant(molecule);
        for (IAtom atom : molecule.atoms()) {
            IMapping mapping = DefaultChemObjectBuilder.getInstance().newInstance(IMapping.class, atom, reactantCloned.getAtom(molecule.getAtomNumber(atom)));
            reaction.addMapping(mapping);
        }
        IMoleculeSet moleculeSetP = ConnectivityChecker.partitionIntoMolecules(reactantCloned);
        for (int z = 0; z < moleculeSetP.getAtomContainerCount(); z++) reaction.addProduct(moleculeSetP.getMolecule(z));
        return reaction;
    }
}
