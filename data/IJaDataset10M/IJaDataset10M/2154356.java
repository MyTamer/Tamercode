package org.openscience.cdk.qsar.descriptors.molecular;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.config.Elements;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.nonotify.NNAtomContainer;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * TestSuite that runs all QSAR tests.
 *
 * @cdk.module test-qsarmolecular
 */
public class RotatableBondsCountDescriptorTest extends MolecularDescriptorTest {

    public RotatableBondsCountDescriptorTest() {
    }

    @Before
    public void setUp() throws Exception {
        setDescriptor(RotatableBondsCountDescriptor.class);
    }

    @Test
    public void testRotatableBondsCount() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Object[] params = { new Boolean(true) };
        descriptor.setParameters(params);
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("CC2CCC(C1CCCCC1)CC2");
        Assert.assertEquals(2, ((IntegerResult) descriptor.calculate(mol).getValue()).intValue());
    }

    private IAtomContainer makeEthane() {
        IAtomContainer container = new NNAtomContainer();
        container.addAtom(container.getBuilder().newAtom(Elements.CARBON));
        container.addAtom(container.getBuilder().newAtom(Elements.CARBON));
        container.addBond(0, 1, IBond.Order.SINGLE);
        return container;
    }

    private IAtomContainer makeButane() {
        IAtomContainer container = makeEthane();
        container.addAtom(container.getBuilder().newAtom(Elements.CARBON));
        container.addAtom(container.getBuilder().newAtom(Elements.CARBON));
        container.addBond(1, 2, IBond.Order.SINGLE);
        container.addBond(2, 3, IBond.Order.SINGLE);
        return container;
    }

    @Test
    public void testEthaneIncludeTerminals() throws Exception {
        IAtomContainer container = makeEthane();
        IMolecularDescriptor descriptor = new RotatableBondsCountDescriptor();
        descriptor.setParameters(new Object[] { Boolean.TRUE });
        DescriptorValue result = descriptor.calculate(container);
        Assert.assertEquals(1, ((IntegerResult) result.getValue()).intValue());
    }

    @Test
    public void testEthane() throws Exception {
        IAtomContainer container = makeEthane();
        IMolecularDescriptor descriptor = new RotatableBondsCountDescriptor();
        descriptor.setParameters(new Object[] { Boolean.FALSE });
        DescriptorValue result = descriptor.calculate(container);
        Assert.assertEquals(0, ((IntegerResult) result.getValue()).intValue());
    }

    @Test
    public void testButaneIncludeTerminals() throws Exception {
        IAtomContainer container = makeButane();
        IMolecularDescriptor descriptor = new RotatableBondsCountDescriptor();
        descriptor.setParameters(new Object[] { Boolean.TRUE });
        DescriptorValue result = descriptor.calculate(container);
        Assert.assertEquals(3, ((IntegerResult) result.getValue()).intValue());
    }

    @Test
    public void testButane() throws Exception {
        IAtomContainer container = makeButane();
        IMolecularDescriptor descriptor = new RotatableBondsCountDescriptor();
        descriptor.setParameters(new Object[] { Boolean.FALSE });
        DescriptorValue result = descriptor.calculate(container);
        Assert.assertEquals(1, ((IntegerResult) result.getValue()).intValue());
    }

    /**
     * @cdk.bug 2449257
     */
    @Test
    public void testEthaneIncludeTerminalsExplicitH() throws Exception {
        IAtomContainer container = makeEthane();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(container);
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(container.getBuilder());
        adder.addImplicitHydrogens(container);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(container);
        IMolecularDescriptor descriptor = new RotatableBondsCountDescriptor();
        descriptor.setParameters(new Object[] { Boolean.TRUE });
        DescriptorValue result = descriptor.calculate(container);
        Assert.assertEquals(1, ((IntegerResult) result.getValue()).intValue());
    }

    /**
     * @cdk.bug 2449257
     */
    @Test
    public void testEthaneExplicitH() throws Exception {
        IAtomContainer container = makeEthane();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(container);
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(container.getBuilder());
        adder.addImplicitHydrogens(container);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(container);
        IMolecularDescriptor descriptor = new RotatableBondsCountDescriptor();
        descriptor.setParameters(new Object[] { Boolean.FALSE });
        DescriptorValue result = descriptor.calculate(container);
        Assert.assertEquals(0, ((IntegerResult) result.getValue()).intValue());
    }

    /**
     * @cdk.bug 2449257
     */
    @Test
    public void testButaneIncludeTerminalsExplicitH() throws Exception {
        IAtomContainer container = makeButane();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(container);
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(container.getBuilder());
        adder.addImplicitHydrogens(container);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(container);
        IMolecularDescriptor descriptor = new RotatableBondsCountDescriptor();
        descriptor.setParameters(new Object[] { Boolean.TRUE });
        DescriptorValue result = descriptor.calculate(container);
        Assert.assertEquals(3, ((IntegerResult) result.getValue()).intValue());
    }

    /**
     * @cdk.bug 2449257
     */
    @Test
    public void testButaneExplicitH() throws Exception {
        IAtomContainer container = makeButane();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(container);
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(container.getBuilder());
        adder.addImplicitHydrogens(container);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(container);
        IMolecularDescriptor descriptor = new RotatableBondsCountDescriptor();
        descriptor.setParameters(new Object[] { Boolean.FALSE });
        DescriptorValue result = descriptor.calculate(container);
        Assert.assertEquals(1, ((IntegerResult) result.getValue()).intValue());
    }
}
