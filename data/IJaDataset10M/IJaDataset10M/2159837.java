package net.sourceforge.seqware.pipeline.modules.quantification;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.sourceforge.seqware.common.module.FileMetadata;
import net.sourceforge.seqware.common.module.ReturnValue;
import net.sourceforge.seqware.common.util.filetools.FileTools;
import net.sourceforge.seqware.common.util.runtools.RunTools;
import net.sourceforge.seqware.pipeline.module.Module;
import net.sourceforge.seqware.pipeline.module.ModuleInterface;
import org.openide.util.lookup.ServiceProvider;

/**
 * Calculate read counts, coverage, & RPKM per transcript and gene given a BAM file of aligned reads.
 * 
 * This module determines the raw read count, coverage, and rpkm for each transcript & each gene.
 * Coverage calculation: AlignedBases/Length
 * RPKM calculation:  [AlignedReads*(10^9)]/[TotalReads*Length].
 * In both calculations, the length for a gene is determined by the median/mean/shortest/longest of
 * transcripts associated with that gene in the reference set.
 * 
 * Underlying script:  sw_module_GeneCountsRPKM.pl
 * 
 * Necessary programs:  perl, java, Picard (SamFormatConverter.jar)
 * 
 * Necessary data file:  'TranscriptDB', which is the flat file output of module PrepTranscriptDB.
 * (e.g. ~/seqware-pipeline/data/annotation_reference/hg19_transcripts.hg19.20091027.trmap)
 * 
 * Expected output:  ~.transcript.quantification.txt and ~.gene.quantification.txt
 * Both are tab-delimited files, 4 columns:  ID, raw read count, coverage, rpkm.
 * 
 * LIMITATIONS: The BAM file must contain reads mapped to transcripts.
 * 
 * @author sacheek@med.unc.edu
 *
 */
@ServiceProvider(service = ModuleInterface.class)
public class GeneCountsRPKM extends Module {

    private OptionSet options = null;

    private File tempDir = null;

    /**
   * getOptionParser is an internal method to parse command line args.
   * 
   * @return OptionParser this is used to get command line options
   */
    protected OptionParser getOptionParser() {
        OptionParser parser = new OptionParser();
        parser.accepts("infile", "Input BAM file, expects reads aligned to transcripts.").withRequiredArg();
        parser.accepts("outTR", "Output file for transcript summary (read counts, coverage, & RPKM).").withRequiredArg();
        parser.accepts("outGENE", "Output file for gene summary (read counts, coverage, & RPKM).").withRequiredArg();
        parser.accepts("TranscriptDB", "data file specifying transcript-to-genome mapping, this is the flat file output of the PrepTranscriptDB module; " + "format = each transcript on a new line, tab-delimited, 7 columns: transcript, associated gene, transcript length, genomic coordinates, " + "transcript coordinates, & CDS start and stop in transcript coordinates; example: uc004fvz.2{tab}CDY1|9085{tab}2363{tab}" + "chrY:26194161-26192244,26191823-26191379:-{tab}1-1918,1919-2363{tab}327{tab}1991").withRequiredArg();
        parser.accepts("genelength", "[median,mean,shortest,longest] of transcripts in TranscriptDB; note that the calculated mean & median are rounded " + "to the nearest integer").withRequiredArg();
        parser.accepts("perl", "Path to perl").withRequiredArg();
        parser.accepts("script", "Path to perl script: sw_module_GeneCountsRPKM.pl").withRequiredArg();
        parser.accepts("java", "Path to java").withRequiredArg();
        parser.accepts("PicardConvert", "Path to SamFormatConverter.jar").withRequiredArg();
        return (parser);
    }

    /**
   * A method used to return the syntax for this module
   * @return a string describing the syntax
   */
    @Override
    public String get_syntax() {
        OptionParser parser = getOptionParser();
        StringWriter output = new StringWriter();
        try {
            parser.printHelpOn(output);
            return (output.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return (e.getMessage());
        }
    }

    /**
   * All necessary setup for the module.
   * Populate the "processing" table in seqware_meta_db. 
   * Create a temporary directory.
   *  
   * @return A ReturnValue object that contains information about the status of init.
   */
    @Override
    public ReturnValue init() {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        ret.setAlgorithm("GeneCountsRPKM");
        ret.setDescription("Calculates read counts, coverage, & RPKM per transcript and per gene for a given BAM file.");
        ret.setVersion("0.7.0");
        try {
            OptionParser parser = getOptionParser();
            options = parser.parse(this.getParameters().toArray(new String[0]));
            tempDir = FileTools.createTempDirectory(new File("."));
            ret.setStdout(ret.getStdout() + "Output: " + (String) options.valueOf("outTR") + "\nOutput: " + (String) options.valueOf("outGENE") + "\n");
        } catch (OptionException e) {
            e.printStackTrace();
            ret.setStderr(e.getMessage());
            ret.setExitStatus(ReturnValue.INVALIDPARAMETERS);
        } catch (IOException e) {
            e.printStackTrace();
            ret.setStderr(e.getMessage());
            ret.setExitStatus(ReturnValue.DIRECTORYNOTWRITABLE);
        }
        return (ret);
    }

    /**
   * Verify that the parameters are defined & make sense.
   * 
   * @return a ReturnValue object
   */
    @Override
    public ReturnValue do_verify_parameters() {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        for (String option : new String[] { "infile", "outTR", "outGENE", "TranscriptDB", "genelength", "perl", "script", "java", "PicardConvert" }) {
            if (!options.has(option)) {
                ret.setExitStatus(ReturnValue.INVALIDPARAMETERS);
                String stdErr = ret.getStderr();
                ret.setStderr(stdErr + "Must include parameter: --" + option + "\n");
            }
        }
        return ret;
    }

    /**
   * Verify anything needed to run the module is ready (e.g. input files exist, etc).
   * 
   * @return a ReturnValue object
   */
    @Override
    public ReturnValue do_verify_input() {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        if (FileTools.fileExistsAndReadable(new File((String) options.valueOf("infile"))).getExitStatus() != ReturnValue.SUCCESS) {
            ret.setExitStatus(ReturnValue.FILENOTREADABLE);
            ret.setStderr("Input file is not readable");
        }
        File output1 = new File((String) options.valueOf("outTR"));
        if (FileTools.dirPathExistsAndWritable(output1.getParentFile()).getExitStatus() != ReturnValue.SUCCESS) {
            ret.setExitStatus(ReturnValue.DIRECTORYNOTWRITABLE);
            ret.setStderr("Can't write to output directory of " + (String) options.valueOf("outTR"));
        }
        File output2 = new File((String) options.valueOf("outGENE"));
        if (FileTools.dirPathExistsAndWritable(output2.getParentFile()).getExitStatus() != ReturnValue.SUCCESS) {
            ret.setExitStatus(ReturnValue.DIRECTORYNOTWRITABLE);
            ret.setStderr("Can't write to output directory of " + (String) options.valueOf("outGENE"));
        }
        if (FileTools.fileExistsAndReadable(new File((String) options.valueOf("TranscriptDB"))).getExitStatus() != ReturnValue.SUCCESS) {
            ret.setExitStatus(ReturnValue.FILENOTREADABLE);
            ret.setStderr("Required data file (TranscriptDB) is not readable");
        }
        if (!"median".equals((String) options.valueOf("genelength")) && !"mean".equals((String) options.valueOf("genelength")) && !"shortest".equals((String) options.valueOf("genelength")) && !"longest".equals((String) options.valueOf("genelength"))) {
            ret.setExitStatus(ReturnValue.INVALIDARGUMENT);
            ret.setStderr("genelength must be median, mean, shortest, or longest");
        }
        if (FileTools.fileExistsAndExecutable(new File((String) options.valueOf("perl"))).getExitStatus() != ReturnValue.SUCCESS) {
            ret.setExitStatus(ReturnValue.FILENOTEXECUTABLE);
            ret.setStderr("Not executable: " + (String) options.valueOf("perl"));
        }
        if (FileTools.fileExistsAndReadable(new File((String) options.valueOf("script"))).getExitStatus() != ReturnValue.SUCCESS) {
            ret.setExitStatus(ReturnValue.FILENOTREADABLE);
            ret.setStderr("Could not find script at " + (String) options.valueOf("script"));
        }
        if (FileTools.fileExistsAndExecutable(new File((String) options.valueOf("java"))).getExitStatus() != ReturnValue.SUCCESS) {
            ret.setExitStatus(ReturnValue.FILENOTEXECUTABLE);
            ret.setStderr("Not executable: " + (String) options.valueOf("java"));
        }
        if (FileTools.fileExistsAndReadable(new File((String) options.valueOf("PicardConvert"))).getExitStatus() != ReturnValue.SUCCESS) {
            ret.setExitStatus(ReturnValue.FILENOTREADABLE);
            ret.setStderr("Could not find SamFormatConverter.jar at " + (String) options.valueOf("PicardConvert"));
        }
        if (FileTools.dirPathExistsAndWritable(tempDir).getExitStatus() != ReturnValue.SUCCESS) {
            ret.setExitStatus(ReturnValue.DIRECTORYNOTWRITABLE);
            ret.setStderr("Can't write to temp directory");
        }
        return (ret);
    }

    /**
   * Optional:  Test program on a known dataset.  Not implemented in this module.
   * 
   * @return a ReturnValue object
   */
    @Override
    public ReturnValue do_test() {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.NOTIMPLEMENTED);
        return (ret);
    }

    /**
   * Run core of module.
   * Based on script sw_module_GeneCountsRPKM.pl
   * 
   * @return a ReturnValue object
   */
    @Override
    public ReturnValue do_run() {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        ret.setRunStartTstmp(new Date());
        String outputC = (String) options.valueOf("outTR");
        String outputR = (String) options.valueOf("outGENE");
        StringBuffer cmd = new StringBuffer();
        cmd.append(options.valueOf("perl") + " " + options.valueOf("script") + " " + options.valueOf("infile") + " ");
        cmd.append(options.valueOf("outTR") + " " + options.valueOf("outGENE") + " " + options.valueOf("TranscriptDB") + " ");
        cmd.append(options.valueOf("genelength") + " " + options.valueOf("java") + " " + options.valueOf("PicardConvert") + " ");
        cmd.append(tempDir.getAbsolutePath());
        RunTools.runCommand(new String[] { "bash", "-c", cmd.toString() });
        FileMetadata fm1 = new FileMetadata();
        fm1.setMetaType("text/transcript-quant");
        fm1.setFilePath(outputC);
        fm1.setType("GeneCountsRPKM-transcript-readct.coverage.rpkm");
        fm1.setDescription("Tab-delimited text file of transcript quantification.");
        ret.getFiles().add(fm1);
        FileMetadata fm2 = new FileMetadata();
        fm2.setMetaType("text/gene-quant");
        fm2.setFilePath(outputR);
        fm2.setType("GeneCountsRPKM-gene-readct.coverage.rpkm");
        fm2.setDescription("Tab-delimited text file of gene quantification.");
        ret.getFiles().add(fm2);
        ret.setRunStopTstmp(new Date());
        return (ret);
    }

    /**
   * Check to make sure the output was created correctly.
   * 
   * @return a ReturnValue object
   */
    @Override
    public ReturnValue do_verify_output() {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        if ((FileTools.fileExistsAndNotEmpty(new File((String) options.valueOf("outTR"))).getExitStatus() != ReturnValue.SUCCESS) || (FileTools.fileExistsAndNotEmpty(new File((String) options.valueOf("outGENE"))).getExitStatus() != ReturnValue.SUCCESS)) {
            ret.setExitStatus(ReturnValue.FILENOTREADABLE);
            ret.setStderr("Expected output is empty or does not exist");
        }
        return (ret);
    }

    /**
   * Optional:  Cleanup.  Remove tempDir.
   * Cleanup files that are outside the current working directory since Pegasus won't do that for you.
   * 
   */
    @Override
    public ReturnValue clean_up() {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        if (!tempDir.delete()) {
            ret.setExitStatus(ReturnValue.DIRECTORYNOTWRITABLE);
            ret.setStderr("Can't delete folder: " + tempDir.getAbsolutePath());
        }
        return (ret);
    }
}
