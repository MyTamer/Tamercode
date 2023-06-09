package com.ibm.wala.demandpa.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.ibm.wala.classLoader.IField;
import com.ibm.wala.classLoader.ShrikeCTMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.propagation.HeapModel;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.shrikeBT.IArrayLoadInstruction;
import com.ibm.wala.shrikeBT.IArrayStoreInstruction;
import com.ibm.wala.shrikeBT.IGetInstruction;
import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.shrikeBT.IPutInstruction;
import com.ibm.wala.shrikeBT.NewInstruction;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAArrayLoadInstruction;
import com.ibm.wala.ssa.SSAArrayReferenceInstruction;
import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSAGetInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.collections.CompoundIterator;
import com.ibm.wala.util.collections.HashMapFactory;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.collections.Iterator2Iterable;
import com.ibm.wala.util.collections.MapUtil;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.shrike.ShrikeUtil;

/**
 * @author sfink
 * 
 */
public class SimpleMemoryAccessMap implements MemoryAccessMap {

    private static final boolean DEBUG = false;

    /**
   * if true, always use IR from CGNode when reasoning about method statements; otherwise, try to use bytecodes when possible. Note
   * that current code may not work if set to false.
   */
    private static final boolean ALWAYS_BUILD_IR = true;

    /**
   * Map: IField -> Set<MemoryAccess>
   */
    private final Map<IField, Set<MemoryAccess>> readMap = HashMapFactory.make();

    /**
   * Map: IField -> Set<MemoryAccess>
   */
    private final Map<IField, Set<MemoryAccess>> writeMap = HashMapFactory.make();

    private final Set<MemoryAccess> arrayReads = HashSetFactory.make();

    private final Set<MemoryAccess> arrayWrites = HashSetFactory.make();

    private final IClassHierarchy cha;

    private final boolean includePrimOps;

    private final HeapModel heapModel;

    public SimpleMemoryAccessMap(CallGraph cg, HeapModel heapModel, boolean includePrimOps) {
        if (cg == null) {
            throw new IllegalArgumentException("null cg");
        }
        this.cha = cg.getClassHierarchy();
        this.heapModel = heapModel;
        this.includePrimOps = includePrimOps;
        populate(cg);
    }

    private void populate(CallGraph cg) {
        for (Iterator<CGNode> it = cg.iterator(); it.hasNext(); ) {
            CGNode n = it.next();
            populate(n);
        }
    }

    private void populate(CGNode n) {
        if (ALWAYS_BUILD_IR || n.getMethod().isSynthetic()) {
            if (DEBUG) {
                System.err.println("synthetic method");
            }
            IR ir = n.getIR();
            if (ir == null) {
                return;
            }
            SSAInstruction[] statements = ir.getInstructions();
            SSAMemoryAccessVisitor v = new SSAMemoryAccessVisitor(n);
            for (int i = 0; i < statements.length; i++) {
                SSAInstruction s = statements[i];
                if (s != null) {
                    v.setInstructionIndex(i);
                    s.visit(v);
                }
            }
        } else {
            if (DEBUG) {
                System.err.println("Shrike method");
            }
            ShrikeCTMethod sm = (ShrikeCTMethod) n.getMethod();
            MemoryAccessVisitor v = new MemoryAccessVisitor(n.getMethod().getReference().getDeclaringClass().getClassLoader(), n);
            try {
                IInstruction[] statements = sm.getInstructions();
                if (statements == null) {
                    return;
                }
                if (DEBUG) {
                    for (int i = 0; i < statements.length; i++) {
                        System.err.println(i + ": " + statements[i]);
                    }
                }
                for (int i = 0; i < statements.length; i++) {
                    IInstruction s = statements[i];
                    if (s != null) {
                        v.setInstructionIndex(i);
                        s.visit(v);
                    }
                }
            } catch (InvalidClassFileException e) {
                e.printStackTrace();
                Assertions.UNREACHABLE();
            }
        }
    }

    private class SSAMemoryAccessVisitor extends SSAInstruction.Visitor {

        private final CGNode node;

        private int instructionIndex;

        public SSAMemoryAccessVisitor(CGNode n) {
            this.node = n;
        }

        public void setInstructionIndex(int i) {
            this.instructionIndex = i;
        }

        @Override
        public void visitNew(SSANewInstruction instruction) {
            TypeReference declaredType = instruction.getNewSite().getDeclaredType();
            if (declaredType.isArrayType() && declaredType.getArrayElementType().isArrayType()) {
                arrayWrites.add(new MemoryAccess(instructionIndex, node));
            }
        }

        @Override
        public void visitArrayLoad(SSAArrayLoadInstruction instruction) {
            if (!includePrimOps && instruction.typeIsPrimitive()) {
                return;
            }
            arrayReads.add(new MemoryAccess(instructionIndex, node));
        }

        @Override
        public void visitArrayStore(SSAArrayStoreInstruction instruction) {
            if (!includePrimOps && instruction.typeIsPrimitive()) {
                return;
            }
            arrayWrites.add(new MemoryAccess(instructionIndex, node));
        }

        @Override
        public void visitGet(SSAGetInstruction instruction) {
            if (!includePrimOps && instruction.getDeclaredFieldType().isPrimitiveType()) {
                return;
            }
            FieldReference fr = instruction.getDeclaredField();
            IField f = cha.resolveField(fr);
            if (f == null) {
                return;
            }
            Set<MemoryAccess> s = MapUtil.findOrCreateSet(readMap, f);
            MemoryAccess fa = new MemoryAccess(instructionIndex, node);
            s.add(fa);
        }

        @Override
        public void visitPut(SSAPutInstruction instruction) {
            if (!includePrimOps && instruction.getDeclaredFieldType().isPrimitiveType()) {
                return;
            }
            FieldReference fr = instruction.getDeclaredField();
            IField f = cha.resolveField(fr);
            if (f == null) {
                return;
            }
            Set<MemoryAccess> s = MapUtil.findOrCreateSet(writeMap, f);
            MemoryAccess fa = new MemoryAccess(instructionIndex, node);
            s.add(fa);
        }
    }

    private class MemoryAccessVisitor extends IInstruction.Visitor {

        int instructionIndex;

        final ClassLoaderReference loader;

        final CGNode node;

        public MemoryAccessVisitor(ClassLoaderReference loader, CGNode node) {
            super();
            this.loader = loader;
            this.node = node;
        }

        protected int getInstructionIndex() {
            return instructionIndex;
        }

        protected void setInstructionIndex(int instructionIndex) {
            this.instructionIndex = instructionIndex;
        }

        @Override
        public void visitNew(NewInstruction instruction) {
            TypeReference tr = ShrikeUtil.makeTypeReference(loader, instruction.getType());
            if (tr.isArrayType() && tr.getArrayElementType().isArrayType()) {
                if (DEBUG) {
                    System.err.println("found multi-dim array write at " + instructionIndex);
                }
                arrayWrites.add(new MemoryAccess(instructionIndex, node));
            }
        }

        @Override
        public void visitArrayLoad(IArrayLoadInstruction instruction) {
            if (!includePrimOps) {
                TypeReference tr = ShrikeUtil.makeTypeReference(loader, instruction.getType());
                if (tr.isPrimitiveType()) {
                    return;
                }
            }
            arrayReads.add(new MemoryAccess(instructionIndex, node));
        }

        @Override
        public void visitArrayStore(IArrayStoreInstruction instruction) {
            if (!includePrimOps) {
                TypeReference tr = ShrikeUtil.makeTypeReference(loader, instruction.getType());
                if (tr.isPrimitiveType()) {
                    return;
                }
            }
            if (DEBUG) {
                System.err.println("found array write at " + instructionIndex);
            }
            arrayWrites.add(new MemoryAccess(instructionIndex, node));
        }

        @Override
        public void visitGet(IGetInstruction instruction) {
            FieldReference fr = FieldReference.findOrCreate(loader, instruction.getClassType(), instruction.getFieldName(), instruction.getFieldType());
            if (!includePrimOps && fr.getFieldType().isPrimitiveType()) {
                return;
            }
            IField f = cha.resolveField(fr);
            if (f == null) {
                return;
            }
            Set<MemoryAccess> s = MapUtil.findOrCreateSet(readMap, f);
            MemoryAccess fa = new MemoryAccess(instructionIndex, node);
            s.add(fa);
        }

        @Override
        public void visitPut(IPutInstruction instruction) {
            FieldReference fr = FieldReference.findOrCreate(loader, instruction.getClassType(), instruction.getFieldName(), instruction.getFieldType());
            if (!includePrimOps && fr.getFieldType().isPrimitiveType()) {
                return;
            }
            IField f = cha.resolveField(fr);
            if (f == null) {
                return;
            }
            Set<MemoryAccess> s = MapUtil.findOrCreateSet(writeMap, f);
            MemoryAccess fa = new MemoryAccess(instructionIndex, node);
            s.add(fa);
        }
    }

    public Collection<MemoryAccess> getFieldReads(PointerKey pk, IField field) {
        Collection<MemoryAccess> result = readMap.get(field);
        if (result == null) {
            return Collections.emptySet();
        } else {
            return result;
        }
    }

    public Collection<MemoryAccess> getFieldWrites(PointerKey pk, IField field) {
        Collection<MemoryAccess> result = writeMap.get(field);
        if (result == null) {
            return Collections.emptySet();
        } else {
            return result;
        }
    }

    public Collection<MemoryAccess> getArrayReads(PointerKey pk) {
        return arrayReads;
    }

    public Collection<MemoryAccess> getArrayWrites(PointerKey pk) {
        return arrayWrites;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        Collection<IField> allFields = HashSetFactory.make();
        allFields.addAll(readMap.keySet());
        allFields.addAll(writeMap.keySet());
        for (Iterator<IField> it = allFields.iterator(); it.hasNext(); ) {
            IField f = it.next();
            result.append("FIELD ").append(f).append(":\n");
            Collection<MemoryAccess> reads = getFieldReads(null, f);
            if (!reads.isEmpty()) {
                result.append("  reads:\n");
                for (Iterator<MemoryAccess> it2 = reads.iterator(); it2.hasNext(); ) {
                    result.append("  ").append(it2.next()).append("\n");
                }
            }
            Collection<MemoryAccess> writes = getFieldWrites(null, f);
            if (!writes.isEmpty()) {
                result.append("  writes:\n");
                for (Iterator<MemoryAccess> it2 = writes.iterator(); it2.hasNext(); ) {
                    result.append("  ").append(it2.next()).append("\n");
                }
            }
        }
        result.append("ARRAY CONTENTS:\n");
        if (!arrayReads.isEmpty()) {
            result.append("  reads:\n");
            for (Iterator<MemoryAccess> it2 = arrayReads.iterator(); it2.hasNext(); ) {
                result.append("  ").append(it2.next()).append("\n");
            }
        }
        if (!arrayWrites.isEmpty()) {
            result.append("  writes:\n");
            for (Iterator<MemoryAccess> it2 = arrayWrites.iterator(); it2.hasNext(); ) {
                result.append("  ").append(it2.next()).append("\n");
            }
        }
        return result.toString();
    }

    public Collection<MemoryAccess> getStaticFieldReads(IField field) {
        return getFieldReads(null, field);
    }

    public Collection<MemoryAccess> getStaticFieldWrites(IField field) {
        return getFieldWrites(null, field);
    }

    public HeapModel getHeapModel() {
        return heapModel;
    }

    public void repOk() {
        for (MemoryAccess m : Iterator2Iterable.make(new CompoundIterator<MemoryAccess>(arrayReads.iterator(), arrayWrites.iterator()))) {
            CGNode node = m.getNode();
            IR ir = node.getIR();
            assert ir != null : "null IR for " + node + " but we have a memory access";
            SSAInstruction[] instructions = ir.getInstructions();
            int instructionIndex = m.getInstructionIndex();
            assert instructionIndex >= 0 && instructionIndex < instructions.length : "instruction index " + instructionIndex + " out of range for " + node + ", which has " + instructions.length + " instructions";
            SSAInstruction s = instructions[m.getInstructionIndex()];
            if (s == null) {
                continue;
            }
            assert s instanceof SSAArrayReferenceInstruction || s instanceof SSANewInstruction : "bad type " + s.getClass() + " for array access instruction";
        }
    }
}
