package org.gamenet.application.mm8leveleditor.mm7;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import org.gamenet.application.mm8leveleditor.converter.FormatConverter;
import org.gamenet.application.mm8leveleditor.converter.StrToTextFormatConverter;
import org.gamenet.application.mm8leveleditor.lod.LodEntry;
import org.gamenet.application.mm8leveleditor.lod.LodResource;
import org.gamenet.util.ByteConversions;
import com.mmbreakfast.unlod.lod.LodFile;
import com.mmbreakfast.unlod.lod.Extractor;
import com.mmbreakfast.unlod.lod.PassThroughLodFileExtractor;

public class MM7NewLodEntry extends LodEntry {

    protected Extractor thePassThroughLodFileExtractor = new PassThroughLodFileExtractor();

    protected Extractor theLodFileExtractor = new Extractor();

    protected static final int ENTRY_NAME__ENTRY_HEADER_OFFSET = 0x00;

    protected static final int ENTRY_NAME__ENTRY_HEADER_MAX_LENGTH = 0x0c;

    protected static final int DATA_SEGMENT_OFFSET__ENTRY_HEADER_OFFSET = 0x10;

    protected static final int DATA_SEGMENT_LENGTH__ENTRY_HEADER_OFFSET = 0x14;

    protected static final int MM7_DATA_HEADER_LENGTH = 0x10;

    protected static final int MM7_DATA_CONTENT_ID_NUMBER__DATA_HEADER_OFFSET = 0x08;

    protected static final int MM7_DATA_CONTENT_ID_NAME__DATA_HEADER_OFFSET = 0x08;

    protected static final int MM7_DATA_CONTENT_COMPRESSED_SIZE__DATA_HEADER_OFFSET = 0x08;

    protected static final int MM7_DATA_CONTENT_UNCOMPRESSED_SIZE__DATA_HEADER_OFFSET = 0x0c;

    protected static final long ID_NUMBER = 91969;

    protected static final int ID_NAME_mmvi[] = { 109, 118, 105, 105 };

    protected Extractor theFileExtractor = new Extractor();

    public MM7NewLodEntry(LodFile lodFile, long headerOffset) throws IOException {
        super(lodFile, headerOffset);
    }

    public String getTextDescription() {
        String fileType = getFileType();
        return "Name: " + getName() + "\n" + "EntryName: " + getEntryName() + "\n" + "DataName: " + getDataName() + "\n" + "FileType: " + fileType + "\n" + "Data Length: " + getDataLength() + "\n" + "Decompressed Size: " + getDecompressedSize() + "\n" + "Data Header Offset: " + getDataHeaderOffset() + "\n" + "Data Offset: " + getDataOffset();
    }

    protected void computeEntryBasedOffsets() {
        dataHeader = new byte[getDataHeaderLength()];
    }

    protected void computeDataBasedOffsets() {
    }

    public long getDataOffset() {
        return getDataHeaderOffset() + getDataHeaderLength();
    }

    protected int getDataLength() {
        int length = ByteConversions.getIntegerInByteArrayAtPosition(entryHeader, DATA_SEGMENT_LENGTH__ENTRY_HEADER_OFFSET) - getDataHeaderLength();
        return length;
    }

    protected long getDataHeaderOffset() {
        return getLodFile().getHeaderOffset() + ByteConversions.getIntegerInByteArrayAtPosition(entryHeader, getOffsetInEntryHeaderForDataSegmentOffset());
    }

    public byte[] getData() throws IOException {
        byte[] rawData = readRawData();
        if (0 == getDecompressedSize()) return rawData; else {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            theLodFileExtractor.decompress(null, rawData, byteStream, null, true);
            return byteStream.toByteArray();
        }
    }

    public String getResourceType() {
        return getFileType();
    }

    protected int getOffsetInEntryHeaderForDataSegmentOffset() {
        return DATA_SEGMENT_OFFSET__ENTRY_HEADER_OFFSET;
    }

    protected int getOffsetInEntryHeaderForDataSegmentLength() {
        return DATA_SEGMENT_LENGTH__ENTRY_HEADER_OFFSET;
    }

    protected int getDataHeaderLength() {
        return MM7_DATA_HEADER_LENGTH;
    }

    protected long getOffsetInDataHeaderFortDataContentCompressedLength() {
        return MM7_DATA_CONTENT_COMPRESSED_SIZE__DATA_HEADER_OFFSET;
    }

    protected long getOffsetInDataHeaderFortDataContentUncompressedLength() {
        return MM7_DATA_CONTENT_UNCOMPRESSED_SIZE__DATA_HEADER_OFFSET;
    }

    public String getFileName() {
        String baseName = getEntryName();
        String fileName = null;
        if (baseName.toLowerCase().endsWith("." + getFileType().toLowerCase())) {
            fileName = baseName;
        } else {
            fileName = baseName + "." + getFileType();
        }
        String converterFileType = getFormatConverterFileType();
        if (null != converterFileType) {
            if (false == fileName.toLowerCase().endsWith("." + converterFileType.toLowerCase())) {
                fileName = fileName + "." + converterFileType;
            }
        }
        return fileName;
    }

    protected String getFileTypex() {
        String fileType = getEntryName().toLowerCase();
        int dotPos = fileType.lastIndexOf('.');
        if (-1 == dotPos) return "";
        if ((fileType.length() - 1) == dotPos) return "";
        return fileType.substring(dotPos + 1).toLowerCase();
    }

    protected String getFileType() {
        String fileType = getEntryName().toLowerCase();
        int dotPos = fileType.lastIndexOf('.');
        if (-1 == dotPos) return "";
        if ((fileType.length() - 1) == dotPos) return "";
        return fileType.substring(dotPos + 1).toLowerCase();
    }

    public String getEntryName() {
        int offset = ENTRY_NAME__ENTRY_HEADER_OFFSET;
        int maxLength = ENTRY_NAME__ENTRY_HEADER_MAX_LENGTH;
        int length = 0;
        while ((0 != entryHeader[offset + length]) && (length < maxLength)) length++;
        return new String(entryHeader, offset, length);
    }

    public String getDataName() {
        return null;
    }

    protected long getDecompressedSize() {
        int index = (int) getOffsetInDataHeaderFortDataContentUncompressedLength();
        if (-1 == index) return -1;
        return ByteConversions.getIntegerInByteArrayAtPosition(dataHeader, index);
    }

    protected Extractor getExtractor() {
        if (0 == getDecompressedSize()) return thePassThroughLodFileExtractor; else return theLodFileExtractor;
    }

    public FormatConverter getFormatConverter() {
        if (getFileType().equals("str")) return new StrToTextFormatConverter(); else return super.getFormatConverter();
    }

    public String getFormatConverterFileType() {
        return super.getFormatConverterFileType();
    }

    protected class NullOutputStream extends OutputStream {

        public void write(int b) throws IOException {
        }
    }

    protected int getNewWrittenDataContentLength(LodResource lodResourceDataSource) {
        try {
            return (int) writeNewData(lodResourceDataSource, new NullOutputStream(), 0);
        } catch (IOException exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    protected int getUpdatedWrittenDataContentLength(LodResource lodResourceDataSource) {
        try {
            return (int) updateData(lodResourceDataSource, new NullOutputStream(), 0);
        } catch (IOException exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    private long writeEntry(String name, long writtenDataLength, OutputStream outputStream, long entryListOffset, long entryOffset, long dataOffset) throws IOException {
        byte originalEntryHeader[] = getEntryHeader();
        byte newEntryHeader[] = new byte[originalEntryHeader.length];
        System.arraycopy(originalEntryHeader, 0, newEntryHeader, 0, originalEntryHeader.length);
        if (null != name) {
            Arrays.fill(newEntryHeader, 0, ENTRY_NAME__ENTRY_HEADER_MAX_LENGTH, (byte) 0);
            byte[] entryNameBytes = name.getBytes();
            System.arraycopy(entryNameBytes, 0, newEntryHeader, 0, Math.min(ENTRY_NAME__ENTRY_HEADER_MAX_LENGTH, entryNameBytes.length));
            if (entryNameBytes.length < ENTRY_NAME__ENTRY_HEADER_MAX_LENGTH) newEntryHeader[entryNameBytes.length] = 0;
        }
        ByteConversions.setIntegerInByteArrayAtPosition((dataOffset - entryListOffset), newEntryHeader, getOffsetInEntryHeaderForDataSegmentOffset());
        ByteConversions.setIntegerInByteArrayAtPosition(writtenDataLength, newEntryHeader, (int) this.getOffsetInEntryHeaderForDataSegmentLength());
        outputStream.write(newEntryHeader);
        return dataOffset + writtenDataLength;
    }

    public long rewriteEntry(OutputStream outputStream, long entryListOffset, long entryOffset, long dataOffset) throws IOException {
        byte[] rawData = readRawData();
        long dataLength = getDataHeaderLength() + rawData.length;
        long newDataOffset = writeEntry(null, dataLength, outputStream, entryListOffset, entryOffset, dataOffset);
        return newDataOffset;
    }

    public long writeNewEntry(LodResource lodResourceDataSource, OutputStream outputStream, long entryListOffset, long entryOffset, long dataOffset) throws IOException {
        return writeEntry(this.getEntryName(), getNewWrittenDataContentLength(lodResourceDataSource), outputStream, entryListOffset, entryOffset, dataOffset);
    }

    public long updateEntry(LodResource lodResourceDataSource, OutputStream outputStream, long entryListOffset, long entryOffset, long dataOffset) throws IOException {
        return writeEntry(null, getUpdatedWrittenDataContentLength(lodResourceDataSource), outputStream, entryListOffset, entryOffset, dataOffset);
    }

    /**
    * @param outputStream
    * @param dataOffset
    * @return
    */
    protected long writeNewDataHeader(LodResource lodResourceDataSource, OutputStream outputStream, long dataOffset, long compressedDataLength, long uncompressedDataLength) throws IOException {
        byte originalDataHeader[] = this.getDataHeader();
        byte newDataHeader[] = new byte[originalDataHeader.length];
        System.arraycopy(originalDataHeader, 0, newDataHeader, 0, originalDataHeader.length);
        if (-1 != getOffsetInDataHeaderFortDataContentCompressedLength()) ByteConversions.setIntegerInByteArrayAtPosition(compressedDataLength, newDataHeader, (int) this.getOffsetInDataHeaderFortDataContentCompressedLength());
        if (-1 != getOffsetInDataHeaderFortDataContentUncompressedLength()) ByteConversions.setIntegerInByteArrayAtPosition(uncompressedDataLength, newDataHeader, (int) this.getOffsetInDataHeaderFortDataContentUncompressedLength());
        long newDataOffset = dataOffset + newDataHeader.length;
        outputStream.write(newDataHeader);
        return newDataOffset;
    }

    protected long updateDataHeader(LodResource lodResourceDataSource, OutputStream outputStream, long dataOffset, long compressedDataLength, long uncompressedDataLength) throws IOException {
        byte originalDataHeader[] = this.getDataHeader();
        byte newDataHeader[] = new byte[originalDataHeader.length];
        System.arraycopy(originalDataHeader, 0, newDataHeader, 0, originalDataHeader.length);
        if (-1 != getOffsetInDataHeaderFortDataContentCompressedLength()) ByteConversions.setIntegerInByteArrayAtPosition(compressedDataLength, newDataHeader, (int) this.getOffsetInDataHeaderFortDataContentCompressedLength());
        if (-1 != getOffsetInDataHeaderFortDataContentUncompressedLength()) ByteConversions.setIntegerInByteArrayAtPosition(uncompressedDataLength, newDataHeader, (int) this.getOffsetInDataHeaderFortDataContentUncompressedLength());
        long newDataOffset = dataOffset + newDataHeader.length;
        outputStream.write(newDataHeader);
        return newDataOffset;
    }

    public long writeNewData(LodResource lodResourceDataSource, OutputStream outputStream, long dataOffset) throws IOException {
        byte uncompressedData[] = lodResourceDataSource.getData();
        byte compressedData[] = theLodFileExtractor.compress(uncompressedData);
        byte smallestData[] = null;
        int uncompressedDataLengthSpecifier;
        if (uncompressedData.length > compressedData.length) {
            smallestData = compressedData;
            uncompressedDataLengthSpecifier = uncompressedData.length;
        } else {
            smallestData = uncompressedData;
            uncompressedDataLengthSpecifier = 0;
        }
        long newDataOffset = writeNewDataHeader(lodResourceDataSource, outputStream, dataOffset, smallestData.length, uncompressedDataLengthSpecifier);
        outputStream.write(smallestData);
        long newOffset = newDataOffset + smallestData.length;
        return newOffset;
    }

    public long updateData(LodResource lodResourceDataSource, OutputStream outputStream, long dataOffset) throws IOException {
        byte uncompressedData[] = lodResourceDataSource.getData();
        byte compressedData[] = theLodFileExtractor.compress(uncompressedData);
        byte smallestData[] = null;
        int uncompressedDataLengthSpecifier;
        if (uncompressedData.length > compressedData.length) {
            smallestData = compressedData;
            uncompressedDataLengthSpecifier = uncompressedData.length;
        } else {
            smallestData = uncompressedData;
            uncompressedDataLengthSpecifier = 0;
        }
        long newDataOffset = updateDataHeader(lodResourceDataSource, outputStream, dataOffset, smallestData.length, uncompressedDataLengthSpecifier);
        outputStream.write(smallestData);
        long newOffset = newDataOffset + smallestData.length;
        return newOffset;
    }

    public long rewriteData(OutputStream outputStream, long dataOffset) throws IOException {
        outputStream.write(getDataHeader());
        byte[] rawData = readRawData();
        outputStream.write(rawData);
        long newOffset = dataOffset + getDataHeaderLength() + rawData.length;
        return newOffset;
    }
}
