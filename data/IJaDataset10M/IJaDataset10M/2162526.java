package org.waveprotocol.wave.client.paging;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Validates the invariants of an active region with respect to a block
 * structure.
 *
 */
public final class ActiveRegionValidator {

    private final FakeBlock root;

    private final ActiveRegion target;

    private ActiveRegionValidator(FakeBlock root, ActiveRegion target) {
        this.root = root;
        this.target = target;
    }

    /**
   * Checks invariants of the active region. In particular, the ancestor chain
   * of all points inclusively between its start and end are paged in.
   */
    public static void validate(FakeBlock root, ActiveRegion target) {
        new ActiveRegionValidator(root, target).checkActiveRegion();
    }

    private void checkActiveRegion() {
        Set<FakeBlock> unchecked = new HashSet<FakeBlock>(root.collect());
        SimpleMoveablePoint active = SimpleMoveablePoint.at(target.getStart());
        checkPagedIn((FakeBlock) active.block, unchecked);
        while (!active.equals(target.getEnd())) {
            active.next();
            checkPagedIn((FakeBlock) active.block, unchecked);
        }
        for (FakeBlock block : unchecked) {
            assertFalse(block.isPagedIn());
        }
    }

    private void checkPagedIn(FakeBlock block, Collection<FakeBlock> unchecked) {
        if (unchecked.contains(block)) {
            assertTrue(block.isPagedIn());
            unchecked.remove(block);
            checkPagedIn(block.getParent(), unchecked);
        }
    }
}
