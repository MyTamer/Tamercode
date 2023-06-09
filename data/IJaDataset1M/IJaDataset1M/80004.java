package com.lmax.disruptor;

import com.lmax.disruptor.support.StubEvent;
import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public final class BatchPublisherTest {

    private final RingBuffer<StubEvent> ringBuffer = new RingBuffer<StubEvent>(StubEvent.EVENT_FACTORY, 32);

    private final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

    {
        ringBuffer.setGatingSequences(new NoOpEventProcessor(ringBuffer).getSequence());
    }

    @Test
    public void shouldClaimBatchAndPublishBack() throws Exception {
        final int batchSize = 5;
        final BatchDescriptor batchDescriptor = ringBuffer.newBatchDescriptor(batchSize);
        ringBuffer.next(batchDescriptor);
        assertThat(Long.valueOf(batchDescriptor.getStart()), is(Long.valueOf(0L)));
        assertThat(Long.valueOf(batchDescriptor.getEnd()), is(Long.valueOf(4L)));
        assertThat(Long.valueOf(ringBuffer.getCursor()), is(Long.valueOf(Sequencer.INITIAL_CURSOR_VALUE)));
        ringBuffer.publish(batchDescriptor);
        assertThat(Long.valueOf(ringBuffer.getCursor()), is(Long.valueOf(batchSize - 1L)));
        assertThat(Long.valueOf(sequenceBarrier.waitFor(0L)), is(Long.valueOf(batchSize - 1L)));
    }
}
