package fi.tkk.ics.hadoop.bam.custom.samtools;

import net.sf.samtools.SAMBinaryTagAndValue;
import net.sf.samtools.SAMFormatException;
import net.sf.samtools.util.BinaryCodec;
import net.sf.samtools.util.RuntimeEOFException;
import net.sf.samtools.util.SortingCollection;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Class for translating between in-memory and disk representation of BAMRecord.
 */
public class BAMRecordCodec implements SortingCollection.Codec<SAMRecord> {

    static final int FIXED_BLOCK_SIZE = 8 * 4;

    private final BinaryCigarCodec cigarCodec = new BinaryCigarCodec();

    private final SAMFileHeader header;

    private final BinaryCodec binaryCodec = new BinaryCodec();

    private final BinaryTagCodec binaryTagCodec = new BinaryTagCodec(binaryCodec);

    private final SAMRecordFactory samRecordFactory;

    public BAMRecordCodec(final SAMFileHeader header) {
        this(header, new DefaultSAMRecordFactory());
    }

    public BAMRecordCodec(final SAMFileHeader header, final SAMRecordFactory factory) {
        this.header = header;
        this.samRecordFactory = factory;
    }

    public BAMRecordCodec clone() {
        return new BAMRecordCodec(this.header, this.samRecordFactory);
    }

    /** Sets the output stream that records will be written to. */
    public void setOutputStream(final OutputStream os) {
        this.binaryCodec.setOutputStream(os);
    }

    /** Sets the input stream that records will be read from. */
    public void setInputStream(final InputStream is) {
        this.binaryCodec.setInputStream(is);
    }

    /**
     * Write object to OutputStream.
     * The SAMRecord must have a header set into it so reference indices can be resolved.
     *
     * @param alignment Record to be written.
     */
    public void encode(final SAMRecord alignment) {
        final int readLength = alignment.getReadLength();
        final int cigarLength = alignment.getCigarLength();
        int blockSize = FIXED_BLOCK_SIZE + alignment.getReadNameLength() + 1 + cigarLength * 4 + (readLength + 1) / 2 + readLength;
        final int attributesSize = alignment.getAttributesBinarySize();
        if (attributesSize != -1) {
            blockSize += attributesSize;
        } else {
            SAMBinaryTagAndValue attribute = alignment.getBinaryAttributes();
            while (attribute != null) {
                blockSize += (BinaryTagCodec.getTagSize(attribute.value));
                attribute = attribute.getNext();
            }
        }
        int indexBin = 0;
        if (alignment.getReferenceIndex() >= 0) {
            if (alignment.getIndexingBin() != null) {
                indexBin = alignment.getIndexingBin();
            } else {
                indexBin = alignment.computeIndexingBin();
            }
        }
        this.binaryCodec.writeInt(blockSize);
        this.binaryCodec.writeInt(alignment.getReferenceIndex());
        this.binaryCodec.writeInt(alignment.getAlignmentStart() - 1);
        this.binaryCodec.writeUByte((short) (alignment.getReadNameLength() + 1));
        this.binaryCodec.writeUByte((short) alignment.getMappingQuality());
        this.binaryCodec.writeUShort(indexBin);
        this.binaryCodec.writeUShort(cigarLength);
        this.binaryCodec.writeUShort(alignment.getFlags());
        this.binaryCodec.writeInt(alignment.getReadLength());
        this.binaryCodec.writeInt(alignment.getMateReferenceIndex());
        this.binaryCodec.writeInt(alignment.getMateAlignmentStart() - 1);
        this.binaryCodec.writeInt(alignment.getInferredInsertSize());
        final byte[] variableLengthBinaryBlock = alignment.getVariableBinaryRepresentation();
        if (variableLengthBinaryBlock != null) {
            this.binaryCodec.writeBytes(variableLengthBinaryBlock);
        } else {
            if (alignment.getReadLength() != alignment.getBaseQualities().length && alignment.getBaseQualities().length != 0) {
                throw new RuntimeException("Mismatch between read length and quals length writing read " + alignment.getReadName() + "; read length: " + alignment.getReadLength() + "; quals length: " + alignment.getBaseQualities().length);
            }
            this.binaryCodec.writeString(alignment.getReadName(), false, true);
            final int[] binaryCigar = cigarCodec.encode(alignment.getCigar());
            for (final int cigarElement : binaryCigar) {
                this.binaryCodec.writeInt(cigarElement);
            }
            this.binaryCodec.writeBytes(SAMUtils.bytesToCompressedBases(alignment.getReadBases()));
            byte[] qualities = alignment.getBaseQualities();
            if (qualities.length == 0) {
                qualities = new byte[alignment.getReadLength()];
                Arrays.fill(qualities, (byte) 0xFF);
            }
            this.binaryCodec.writeBytes(qualities);
            SAMBinaryTagAndValue attribute = alignment.getBinaryAttributes();
            while (attribute != null) {
                this.binaryTagCodec.writeTag(attribute.tag, attribute.value, attribute.isUnsignedArray());
                attribute = attribute.getNext();
            }
        }
    }

    /**
     * Read the next record from the input stream and convert into a java object.
     *
     * @return null if no more records.  Should throw exception if EOF is encountered in the middle of
     *         a record.
     */
    public SAMRecord decode() {
        int recordLength = 0;
        try {
            recordLength = this.binaryCodec.readInt();
        } catch (RuntimeEOFException e) {
            return null;
        }
        if (recordLength < FIXED_BLOCK_SIZE) {
            throw new SAMFormatException("Invalid record length: " + recordLength);
        }
        final int referenceID = this.binaryCodec.readInt();
        final int coordinate = this.binaryCodec.readInt() + 1;
        final short readNameLength = this.binaryCodec.readUByte();
        final short mappingQuality = this.binaryCodec.readUByte();
        final int bin = this.binaryCodec.readUShort();
        final int cigarLen = this.binaryCodec.readUShort();
        final int flags = this.binaryCodec.readUShort();
        final int readLen = this.binaryCodec.readInt();
        final int mateReferenceID = this.binaryCodec.readInt();
        final int mateCoordinate = this.binaryCodec.readInt() + 1;
        final int insertSize = this.binaryCodec.readInt();
        final byte[] restOfRecord = new byte[recordLength - FIXED_BLOCK_SIZE];
        this.binaryCodec.readBytes(restOfRecord);
        final BAMRecord ret = this.samRecordFactory.createBAMRecord(header, referenceID, coordinate, readNameLength, mappingQuality, bin, cigarLen, flags, readLen, mateReferenceID, mateCoordinate, insertSize, restOfRecord);
        ret.setHeader(header);
        return ret;
    }
}
