package edu.mit.wi.haploview;

import java.util.*;

public class EM implements Constants {

    private int[][] haplotypes;

    private double[] frequencies;

    private Vector obsT, obsU;

    private Vector controlCounts, caseCounts;

    private Vector discordantCounts;

    final int MISSINGLIMIT = 4;

    final int MAXLOCI = 500;

    final int MAXLN = 1000;

    final double PSEUDOCOUNT = 0.1;

    OBS[] data;

    SUPER_OBS[] superdata;

    static int[] two_n = new int[32];

    static {
        two_n[0] = 1;
        for (int i = 1; i < 31; i++) {
            two_n[i] = 2 * two_n[i - 1];
        }
    }

    int[][] ambighet;

    private Vector chromosomes;

    private int numTrios;

    private int numFilteredTrios;

    private boolean[][][] kidConsistentCache;

    private Vector realAffectedStatus, realKidAffectedStatus;

    private MapWrap fullProbMap;

    private boolean[] haploid;

    private HaploData theData;

    private int extraTrioCount;

    private int updatedExtraTrioCount;

    EM(Vector chromosomes, int numTrios, HaploData theData, Vector extraInds) {
        this.theData = theData;
        this.chromosomes = new Vector();
        if (extraInds != null) {
            for (int i = 0; i < extraInds.size(); i++) {
                this.chromosomes.add(extraInds.elementAt(i));
                if (((Chromosome) this.chromosomes.lastElement()).isHaploid()) {
                    this.chromosomes.add(extraInds.elementAt(i));
                }
            }
        }
        extraTrioCount = this.chromosomes.size() / 4;
        for (int i = 0; i < chromosomes.size(); i++) {
            this.chromosomes.add(chromosomes.elementAt(i));
            if (((Chromosome) this.chromosomes.lastElement()).isHaploid()) {
                this.chromosomes.add(chromosomes.elementAt(i));
            }
        }
        this.numTrios = numTrios + extraTrioCount;
    }

    class MapWrap {

        private HashMap theMap;

        private Double defaultVal;

        MapWrap(double d) {
            theMap = new HashMap();
            defaultVal = new Double(d);
        }

        double get(Object key) {
            if (theMap.containsKey(key)) {
                return ((Double) theMap.get(key)).doubleValue();
            } else {
                return defaultVal.doubleValue();
            }
        }

        void put(Object key, double val) {
            theMap.put(key, new Double(val));
        }

        void setDefaultVal(double val) {
            defaultVal = new Double(val);
        }

        void normalize(double normalizer) {
            Iterator itr = theMap.keySet().iterator();
            while (itr.hasNext()) {
                Object key = itr.next();
                put(key, (get(key) / normalizer));
            }
            defaultVal = new Double(defaultVal.doubleValue() / normalizer);
        }

        Set getKeySet() {
            return theMap.keySet();
        }
    }

    class DiscordantTally {

        private double[] counts = new double[9];

        private long theAllele;

        public DiscordantTally(long a) {
            theAllele = a;
        }

        public void tally(long affectedHap1, long affectedHap2, long unaffectedHap1, long unaffectedHap2, double freq) {
            int offset = 0;
            if (affectedHap1 == affectedHap2 && affectedHap1 == theAllele) {
                offset = 0;
            } else if (affectedHap1 == theAllele || affectedHap2 == theAllele) {
                offset = 1;
            } else {
                offset = 2;
            }
            if (unaffectedHap1 == theAllele) {
                if (unaffectedHap1 == unaffectedHap2) {
                    counts[0 + offset] += freq;
                } else {
                    counts[3 + offset] += freq;
                }
            } else if (unaffectedHap2 == theAllele) {
                counts[3 + offset] += freq;
            } else {
                counts[6 + offset] += freq;
            }
        }

        public void combine(DiscordantTally dt) {
            for (int i = 0; i < 9; i++) {
                counts[i] += dt.counts[i];
            }
        }

        public void normalize(double d) {
            for (int i = 0; i < 9; i++) {
                counts[i] /= d;
            }
        }

        public double[] getCounts() {
            return counts;
        }
    }

    class Recovery {

        long h1;

        long h2;

        float p;

        public Recovery() {
            h1 = 0;
            h2 = 0;
            p = 0.0f;
        }
    }

    class OBS {

        int nposs;

        Vector poss;

        public OBS() {
            poss = new Vector(20, 8);
        }
    }

    class SUPER_OBS {

        int nblocks;

        int[] nposs;

        Recovery[][] poss;

        int nsuper;

        Recovery[] superposs;

        public SUPER_OBS(int size) {
            poss = new Recovery[size][];
            nposs = new int[size];
        }
    }

    public void doEM(int[] theBlock) throws HaploViewException {
        int[] block_size;
        if (theBlock.length < 9) {
            block_size = new int[1];
            block_size[0] = theBlock.length;
        } else {
            int ones = theBlock.length % 8;
            int eights = (theBlock.length - ones) / 8;
            if (ones == 0) {
                block_size = new int[eights];
                for (int i = 0; i < eights; i++) {
                    block_size[i] = 8;
                }
            } else {
                block_size = new int[eights + 1];
                for (int i = 0; i < eights - 1; i++) {
                    block_size[i] = 8;
                }
                block_size[eights - 1] = (8 + ones) / 2;
                block_size[eights] = 8 + ones - block_size[eights - 1];
            }
        }
        byte[] thisHap;
        Vector inputHaploSingletons = new Vector();
        Vector inputHaploTrios = new Vector();
        Vector affSingletons = new Vector();
        Vector affTrios = new Vector();
        Vector affKids = new Vector();
        Vector haploidTrios = new Vector();
        Vector haploidSingletons = new Vector();
        int[] whichVector = new int[chromosomes.size()];
        updatedExtraTrioCount = 0;
        for (int i = 0; i < numTrios * 4; i += 4) {
            Chromosome parentAFirst = (Chromosome) chromosomes.elementAt(i);
            Chromosome parentASecond = (Chromosome) chromosomes.elementAt(i + 1);
            Chromosome parentBFirst = (Chromosome) chromosomes.elementAt(i + 2);
            Chromosome parentBSecond = (Chromosome) chromosomes.elementAt(i + 3);
            boolean tooManyMissingInASegmentA = false;
            boolean tooManyMissingInASegmentB = false;
            int totalMissingA = 0;
            int totalMissingB = 0;
            int segmentShift = 0;
            for (int n = 0; n < block_size.length; n++) {
                int missingA = 0;
                int missingB = 0;
                for (int j = 0; j < block_size[n]; j++) {
                    byte AFirstGeno = parentAFirst.getGenotype(theBlock[segmentShift + j]);
                    byte ASecondGeno = parentASecond.getGenotype(theBlock[segmentShift + j]);
                    byte BFirstGeno = parentBFirst.getGenotype(theBlock[segmentShift + j]);
                    byte BSecondGeno = parentBSecond.getGenotype(theBlock[segmentShift + j]);
                    if (AFirstGeno == 0 || ASecondGeno == 0) missingA++;
                    if (BFirstGeno == 0 || BSecondGeno == 0) missingB++;
                }
                segmentShift += block_size[n];
                if (missingA >= MISSINGLIMIT) {
                    tooManyMissingInASegmentA = true;
                }
                if (missingB >= MISSINGLIMIT) {
                    tooManyMissingInASegmentB = true;
                }
                totalMissingA += missingA;
                totalMissingB += missingB;
            }
            if (!tooManyMissingInASegmentA && totalMissingA <= 1 + theBlock.length / 3 && !tooManyMissingInASegmentB && totalMissingB <= 1 + theBlock.length / 3) {
                whichVector[i] = 2;
                whichVector[i + 1] = 2;
                whichVector[i + 2] = 2;
                whichVector[i + 3] = 2;
                if (i / 4 < extraTrioCount) {
                    updatedExtraTrioCount++;
                }
            } else if ((!tooManyMissingInASegmentA && totalMissingA <= 1 + theBlock.length / 3) && i / 4 >= extraTrioCount) {
                whichVector[i] = 3;
                whichVector[i + 1] = 3;
                whichVector[i + 2] = 0;
                whichVector[i + 3] = 0;
            } else if (!tooManyMissingInASegmentB && totalMissingB <= 1 + theBlock.length / 3 && i / 4 >= extraTrioCount) {
                whichVector[i] = 0;
                whichVector[i + 1] = 0;
                whichVector[i + 2] = 3;
                whichVector[i + 3] = 3;
            } else {
                whichVector[i] = 0;
                whichVector[i + 1] = 0;
                whichVector[i + 2] = 0;
                whichVector[i + 3] = 0;
            }
        }
        for (int i = numTrios * 4; i < chromosomes.size(); i++) {
            Chromosome thisChrom = (Chromosome) chromosomes.elementAt(i);
            Chromosome nextChrom = (Chromosome) chromosomes.elementAt(++i);
            boolean tooManyMissingInASegment = false;
            int totalMissing = 0;
            int segmentShift = 0;
            for (int n = 0; n < block_size.length; n++) {
                int missing = 0;
                for (int j = 0; j < block_size[n]; j++) {
                    byte theGeno = thisChrom.getGenotype(theBlock[segmentShift + j]);
                    byte nextGeno = nextChrom.getGenotype(theBlock[segmentShift + j]);
                    if (theGeno == 0 || nextGeno == 0) missing++;
                }
                segmentShift += block_size[n];
                if (missing >= MISSINGLIMIT) {
                    tooManyMissingInASegment = true;
                }
                totalMissing += missing;
            }
            if (!tooManyMissingInASegment && totalMissing <= 1 + theBlock.length / 3) {
                whichVector[i - 1] = 1;
                whichVector[i] = 1;
            }
        }
        boolean addAff = true;
        for (int i = 0; i < chromosomes.size(); i++) {
            Chromosome thisChrom = (Chromosome) chromosomes.elementAt(i);
            if (whichVector[i] > 0) {
                thisHap = new byte[theBlock.length];
                for (int j = 0; j < theBlock.length; j++) {
                    byte a1 = theData.getMarker(theBlock[j]).getMajor();
                    byte a2 = theData.getMarker(theBlock[j]).getMinor();
                    byte theGeno = thisChrom.getGenotype(theBlock[j]);
                    if (theGeno >= 5) {
                        thisHap[j] = 'h';
                    } else {
                        if (theGeno == 0) {
                            thisHap[j] = '0';
                        } else if (theGeno == a1) {
                            thisHap[j] = '1';
                        } else if (theGeno == a2) {
                            thisHap[j] = '2';
                        } else {
                            throw new HaploViewException("Marker with > 2 alleles: " + theData.getMarker(theBlock[j]).getDisplayName());
                        }
                    }
                }
                if (whichVector[i] == 1) {
                    inputHaploSingletons.add(thisHap);
                    if (addAff) {
                        affSingletons.add(new Integer(thisChrom.getAffected()));
                        haploidSingletons.add(new Boolean(thisChrom.isHaploid()));
                    }
                } else if (whichVector[i] == 2) {
                    inputHaploTrios.add(thisHap);
                    if (addAff) {
                        affTrios.add(new Integer(thisChrom.getAffected()));
                        affKids.add(thisChrom.getKidAffected());
                        haploidTrios.add(new Boolean(thisChrom.isHaploid()));
                    }
                } else if (whichVector[i] == 3) {
                    inputHaploSingletons.add(thisHap);
                    if (addAff) {
                        affSingletons.add(new Integer(0));
                        haploidSingletons.add(new Boolean(thisChrom.isHaploid()));
                    }
                }
                if (addAff) {
                    addAff = false;
                } else {
                    addAff = true;
                }
            }
        }
        numFilteredTrios = inputHaploTrios.size() / 4;
        inputHaploTrios.addAll(inputHaploSingletons);
        affTrios.addAll(affSingletons);
        byte[][] input_haplos = (byte[][]) inputHaploTrios.toArray(new byte[0][0]);
        haploidTrios.addAll(haploidSingletons);
        haploid = new boolean[haploidTrios.size()];
        for (int i = 0; i < haploidTrios.size(); i++) {
            haploid[i] = ((Boolean) haploidTrios.elementAt(i)).booleanValue();
        }
        full_em_breakup(input_haplos, block_size, affTrios, affKids);
    }

    private void full_em_breakup(byte[][] input_haplos, int[] block_size, Vector affStatus, Vector kidAffStatus) throws HaploViewException {
        int num_poss, iter;
        double total = 0;
        int block, start_locus, end_locus, biggest_block_size;
        int num_indivs = 0;
        int num_blocks = block_size.length;
        int num_haplos = input_haplos.length;
        int num_loci = input_haplos[0].length;
        Recovery tempRec;
        if (num_loci > MAXLOCI) {
            throw new HaploViewException("Too many loci in a single block (> " + MAXLOCI + " non-redundant)");
        }
        biggest_block_size = block_size[0];
        for (int i = 1; i < num_blocks; i++) {
            if (block_size[i] > biggest_block_size) biggest_block_size = block_size[i];
        }
        num_poss = two_n[biggest_block_size];
        data = new OBS[num_haplos / 2];
        for (int i = 0; i < num_haplos / 2; i++) data[i] = new OBS();
        superdata = new SUPER_OBS[num_haplos / 2];
        for (int i = 0; i < num_haplos / 2; i++) superdata[i] = new SUPER_OBS(num_blocks);
        double[][] hprob = new double[num_blocks][num_poss];
        int[][] hlist = new int[num_blocks][num_poss];
        int[] num_hlist = new int[num_blocks];
        int[] hint = new int[num_poss];
        MapWrap probMap = new MapWrap(PSEUDOCOUNT);
        if (Options.getAssocTest() == ASSOC_TRIO) {
            ambighet = new int[(num_haplos / 4)][num_loci];
            store_dhet_status(num_haplos, num_loci, input_haplos);
        }
        end_locus = -1;
        for (block = 0; block < num_blocks; block++) {
            start_locus = end_locus + 1;
            end_locus = start_locus + block_size[block] - 1;
            num_poss = two_n[block_size[block]];
            num_indivs = read_observations(num_haplos, num_loci, input_haplos, start_locus, end_locus);
            total = (double) num_poss;
            total *= PSEUDOCOUNT;
            for (int i = 0; i < num_indivs; i++) {
                if (data[i].nposs == 1 && i >= updatedExtraTrioCount * 2) {
                    tempRec = (Recovery) data[i].poss.elementAt(0);
                    probMap.put(new Long(tempRec.h1), probMap.get(new Long(tempRec.h1)) + 1.0);
                    if (!haploid[i]) {
                        probMap.put(new Long(tempRec.h2), probMap.get(new Long(tempRec.h2)) + 1.0);
                        total += 2.0;
                    } else {
                        total += 1.0;
                    }
                }
            }
            probMap.normalize(total);
            iter = 0;
            while (iter < 20) {
                for (int i = 0; i < num_indivs; i++) {
                    total = 0.0;
                    for (int k = 0; k < data[i].nposs; k++) {
                        tempRec = (Recovery) data[i].poss.elementAt(k);
                        if (haploid[i]) {
                            if (tempRec.h1 == tempRec.h2) {
                                tempRec.p = (float) (probMap.get(new Long(tempRec.h1)));
                            } else {
                                tempRec.p = 0;
                            }
                        } else {
                            tempRec.p = (float) (probMap.get(new Long(tempRec.h1)) * probMap.get(new Long(tempRec.h2)));
                        }
                        total += tempRec.p;
                    }
                    for (int k = 0; k < data[i].nposs; k++) {
                        tempRec = (Recovery) data[i].poss.elementAt(k);
                        tempRec.p /= total;
                    }
                }
                probMap = new MapWrap(1e-10);
                total = num_poss * 1e-10;
                for (int i = 0; i < num_indivs; i++) {
                    if (i >= updatedExtraTrioCount * 2) {
                        for (int k = 0; k < data[i].nposs; k++) {
                            tempRec = (Recovery) data[i].poss.elementAt(k);
                            probMap.put(new Long(tempRec.h1), probMap.get(new Long(tempRec.h1)) + tempRec.p);
                            if (!haploid[i]) {
                                probMap.put(new Long(tempRec.h2), probMap.get(new Long(tempRec.h2)) + tempRec.p);
                                total += (2.0 * (tempRec.p));
                            } else {
                                total += tempRec.p;
                            }
                        }
                    }
                }
                probMap.normalize(total);
                iter++;
            }
            int m = 0;
            for (long j = 0; j < num_poss; j++) {
                hint[(int) j] = -1;
                if (probMap.get(new Long(j)) > .001) {
                    hlist[block][m] = (int) j;
                    hprob[block][m] = probMap.get(new Long(j));
                    hint[(int) j] = m;
                    m++;
                }
            }
            num_hlist[block] = m;
            store_block_haplos(hlist, hprob, hint, block, num_indivs);
        }
        double poss_full = 1;
        for (block = 0; block < num_blocks; block++) {
            poss_full *= num_hlist[block];
        }
        fullProbMap = new MapWrap(PSEUDOCOUNT);
        create_super_haplos(num_indivs, num_blocks, num_hlist);
        total = poss_full * PSEUDOCOUNT;
        for (int i = 0; i < num_indivs; i++) {
            if (superdata[i].nsuper == 1 && i >= updatedExtraTrioCount * 2) {
                Long h1 = new Long(superdata[i].superposs[0].h1);
                Long h2 = new Long(superdata[i].superposs[0].h2);
                fullProbMap.put(h1, fullProbMap.get(h1) + 1.0);
                if (!haploid[i]) {
                    fullProbMap.put(h2, fullProbMap.get(h2) + 1.0);
                    total += 2.0;
                } else {
                    total += 1.0;
                }
            }
        }
        fullProbMap.normalize(total);
        iter = 0;
        while (iter < 20) {
            for (int i = 0; i < num_indivs; i++) {
                total = 0.0;
                for (int k = 0; k < superdata[i].nsuper; k++) {
                    if (haploid[i]) {
                        if (superdata[i].superposs[k].h1 == superdata[i].superposs[k].h2) {
                            superdata[i].superposs[k].p = (float) (fullProbMap.get(new Long(superdata[i].superposs[k].h1)));
                        } else {
                            superdata[i].superposs[k].p = 0;
                        }
                    } else {
                        superdata[i].superposs[k].p = (float) (fullProbMap.get(new Long(superdata[i].superposs[k].h1)) * fullProbMap.get(new Long(superdata[i].superposs[k].h2)));
                    }
                    total += superdata[i].superposs[k].p;
                }
                for (int k = 0; k < superdata[i].nsuper; k++) {
                    superdata[i].superposs[k].p /= total;
                }
            }
            fullProbMap = new MapWrap(1e-10);
            total = poss_full * 1e-10;
            for (int i = 0; i < num_indivs; i++) {
                if (i >= updatedExtraTrioCount * 2) {
                    for (int k = 0; k < superdata[i].nsuper; k++) {
                        fullProbMap.put(new Long(superdata[i].superposs[k].h1), fullProbMap.get(new Long(superdata[i].superposs[k].h1)) + superdata[i].superposs[k].p);
                        if (!haploid[i]) {
                            fullProbMap.put(new Long(superdata[i].superposs[k].h2), fullProbMap.get(new Long(superdata[i].superposs[k].h2)) + superdata[i].superposs[k].p);
                            total += (2.0 * superdata[i].superposs[k].p);
                        } else {
                            total += superdata[i].superposs[k].p;
                        }
                    }
                }
            }
            fullProbMap.normalize(total);
            iter++;
        }
        if (Options.getAssocTest() == ASSOC_TRIO) {
            kidConsistentCache = new boolean[numFilteredTrios][][];
            for (int i = 0; i < numFilteredTrios * 2; i += 2) {
                if (((Integer) kidAffStatus.elementAt(i)).intValue() == 2) {
                    kidConsistentCache[i / 2] = new boolean[superdata[i].nsuper][];
                    for (int n = 0; n < superdata[i].nsuper; n++) {
                        kidConsistentCache[i / 2][n] = new boolean[superdata[i + 1].nsuper];
                        for (int m = 0; m < superdata[i + 1].nsuper; m++) {
                            kidConsistentCache[i / 2][n][m] = kid_consistent(superdata[i].superposs[n].h1, superdata[i + 1].superposs[m].h1, num_blocks, block_size, hlist, num_hlist, i / 2, num_loci);
                        }
                    }
                }
            }
        }
        realAffectedStatus = affStatus;
        realKidAffectedStatus = kidAffStatus;
        doAssociationTests(affStatus, null, null, kidAffStatus);
        Vector haplos_present = new Vector();
        Vector haplo_freq = new Vector();
        ArrayList keys = new ArrayList(fullProbMap.theMap.keySet());
        Collections.sort(keys);
        Iterator kitr = keys.iterator();
        while (kitr.hasNext()) {
            Object key = kitr.next();
            long keyLong = ((Long) key).longValue();
            if (fullProbMap.get(key) > .001) {
                haplos_present.addElement(decode_haplo_str(keyLong, num_blocks, block_size, hlist, num_hlist));
                haplo_freq.addElement(new Double(fullProbMap.get(key)));
            }
        }
        double[] freqs = new double[haplo_freq.size()];
        for (int j = 0; j < haplo_freq.size(); j++) {
            freqs[j] = ((Double) haplo_freq.elementAt(j)).doubleValue();
        }
        this.haplotypes = (int[][]) haplos_present.toArray(new int[0][0]);
        this.frequencies = freqs;
    }

    public void doAssociationTests(Vector affStatus, Vector permuteInd, Vector permuteDiscPar, Vector kidAffStatus) {
        if (fullProbMap == null || superdata == null || realAffectedStatus == null || realKidAffectedStatus == null) {
            return;
        }
        if (affStatus == null) {
            affStatus = realAffectedStatus;
        }
        if (kidAffStatus == null) {
            kidAffStatus = realKidAffectedStatus;
        }
        if (permuteInd == null) {
            permuteInd = new Vector();
            for (int i = 0; i < superdata.length; i++) {
                permuteInd.add(new Boolean(false));
            }
        }
        if (permuteDiscPar == null) {
            permuteDiscPar = new Vector();
            for (int i = 0; i < superdata.length; i++) {
                permuteDiscPar.add(new Boolean(false));
            }
        }
        Vector caseCounts = new Vector();
        Vector controlCounts = new Vector();
        if (Options.getAssocTest() == ASSOC_CC) {
            MapWrap totalCase = new MapWrap(0);
            MapWrap totalControl = new MapWrap(0);
            for (int i = numFilteredTrios * 2; i < superdata.length; i++) {
                MapWrap tempCase = new MapWrap(0);
                MapWrap tempControl = new MapWrap(0);
                double tempnorm = 0;
                for (int n = 0; n < superdata[i].nsuper; n++) {
                    Long long1 = new Long(superdata[i].superposs[n].h1);
                    Long long2 = new Long(superdata[i].superposs[n].h2);
                    if (((Integer) affStatus.elementAt(i)).intValue() == 1) {
                        tempControl.put(long1, tempControl.get(long1) + superdata[i].superposs[n].p);
                        if (!haploid[i]) {
                            tempControl.put(long2, tempControl.get(long2) + superdata[i].superposs[n].p);
                        }
                    } else if (((Integer) affStatus.elementAt(i)).intValue() == 2) {
                        tempCase.put(long1, tempCase.get(long1) + superdata[i].superposs[n].p);
                        if (!haploid[i]) {
                            tempCase.put(long2, tempCase.get(long2) + superdata[i].superposs[n].p);
                        }
                    }
                    tempnorm += superdata[i].superposs[n].p;
                }
                if (tempnorm > 0.00) {
                    Iterator itr = fullProbMap.getKeySet().iterator();
                    while (itr.hasNext()) {
                        Long curHap = (Long) itr.next();
                        if (tempCase.get(curHap) > 0.0000 || tempControl.get(curHap) > 0.0000) {
                            totalCase.put(curHap, totalCase.get(curHap) + (tempCase.get(curHap) / tempnorm));
                            totalControl.put(curHap, totalControl.get(curHap) + (tempControl.get(curHap) / tempnorm));
                        }
                    }
                }
            }
            ArrayList sortedKeySet = new ArrayList(fullProbMap.getKeySet());
            Collections.sort(sortedKeySet);
            for (int j = 0; j < sortedKeySet.size(); j++) {
                if (fullProbMap.get(sortedKeySet.get(j)) > .001) {
                    caseCounts.add(new Double(totalCase.get(sortedKeySet.get(j))));
                    controlCounts.add(new Double(totalControl.get(sortedKeySet.get(j))));
                }
            }
        }
        Vector obsT = new Vector();
        Vector obsU = new Vector();
        if (Options.getAssocTest() == ASSOC_TRIO) {
            double product;
            MapWrap totalT = new MapWrap(0);
            MapWrap totalU = new MapWrap(0);
            discordantCounts = new Vector();
            HashMap totalDiscordantCounts = new HashMap();
            for (int i = 0; i < numFilteredTrios * 2; i += 2) {
                MapWrap tempT = new MapWrap(0);
                MapWrap tempU = new MapWrap(0);
                HashMap tempDiscordantCounts = new HashMap();
                double tempnorm = 0;
                if (((Integer) kidAffStatus.elementAt(i)).intValue() == 2) {
                    boolean discordantParentPhenos = false;
                    if (Options.getTdtType() == TDT_PAREN && ((Integer) affStatus.elementAt(i)).intValue() != ((Integer) affStatus.elementAt(i + 1)).intValue()) {
                        discordantParentPhenos = true;
                    }
                    for (int n = 0; n < superdata[i].nsuper; n++) {
                        for (int m = 0; m < superdata[i + 1].nsuper; m++) {
                            if (kidConsistentCache[i / 2][n][m]) {
                                product = superdata[i].superposs[n].p * superdata[i + 1].superposs[m].p;
                                Long h1 = new Long(superdata[i].superposs[n].h1);
                                Long h2 = new Long(superdata[i].superposs[n].h2);
                                Long h3 = new Long(superdata[i + 1].superposs[m].h1);
                                Long h4 = new Long(superdata[i + 1].superposs[m].h2);
                                if (((Boolean) permuteInd.get(i)).booleanValue()) {
                                    if (superdata[i].superposs[n].h1 != superdata[i].superposs[n].h2) {
                                        tempU.put(h1, tempU.get(h1) + product);
                                        tempT.put(h2, tempT.get(h2) + product);
                                    }
                                    if (superdata[i + 1].superposs[m].h1 != superdata[i + 1].superposs[m].h2) {
                                        tempU.put(h3, tempU.get(h3) + product);
                                        tempT.put(h4, tempT.get(h4) + product);
                                    }
                                } else {
                                    if (superdata[i].superposs[n].h1 != superdata[i].superposs[n].h2) {
                                        tempT.put(h1, tempT.get(h1) + product);
                                        tempU.put(h2, tempU.get(h2) + product);
                                    }
                                    if (superdata[i + 1].superposs[m].h1 != superdata[i + 1].superposs[m].h2) {
                                        tempT.put(h3, tempT.get(h3) + product);
                                        tempU.put(h4, tempU.get(h4) + product);
                                    }
                                }
                                tempnorm += product;
                                if (discordantParentPhenos) {
                                    Long aff1, aff2, unaff1, unaff2;
                                    if (((Integer) affStatus.elementAt(i)).intValue() == 2) {
                                        aff1 = h1;
                                        aff2 = h2;
                                        unaff1 = h3;
                                        unaff2 = h4;
                                    } else {
                                        unaff1 = h1;
                                        unaff2 = h2;
                                        aff1 = h3;
                                        aff2 = h4;
                                    }
                                    if (((Boolean) permuteDiscPar.get(i)).booleanValue()) {
                                        Long temp1 = aff1;
                                        Long temp2 = aff2;
                                        aff1 = unaff1;
                                        aff2 = unaff2;
                                        unaff1 = temp1;
                                        unaff2 = temp2;
                                    }
                                    DiscordantTally dt = getTally(aff1, tempDiscordantCounts);
                                    dt.tally(aff1.longValue(), aff2.longValue(), unaff1.longValue(), unaff2.longValue(), product);
                                    if (!aff2.equals(aff1)) {
                                        dt = getTally(aff2, tempDiscordantCounts);
                                        dt.tally(aff1.longValue(), aff2.longValue(), unaff1.longValue(), unaff2.longValue(), product);
                                    }
                                    if (!unaff1.equals(aff1) && !unaff1.equals(aff2)) {
                                        dt = getTally(unaff1, tempDiscordantCounts);
                                        dt.tally(aff1.longValue(), aff2.longValue(), unaff1.longValue(), unaff2.longValue(), product);
                                    }
                                    if (!unaff2.equals(aff1) && !unaff2.equals(aff2) && !unaff2.equals(unaff1)) {
                                        dt = getTally(unaff2, tempDiscordantCounts);
                                        dt.tally(aff1.longValue(), aff2.longValue(), unaff1.longValue(), unaff2.longValue(), product);
                                    }
                                }
                            }
                        }
                    }
                    if (tempnorm > 0.00) {
                        Iterator itr = fullProbMap.getKeySet().iterator();
                        while (itr.hasNext()) {
                            Long curHap = (Long) itr.next();
                            if (tempT.get(curHap) > 0.0000 || tempU.get(curHap) > 0.0000) {
                                totalT.put(curHap, totalT.get(curHap) + tempT.get(curHap) / tempnorm);
                                totalU.put(curHap, totalU.get(curHap) + tempU.get(curHap) / tempnorm);
                            }
                        }
                        itr = tempDiscordantCounts.keySet().iterator();
                        while (itr.hasNext()) {
                            Long key = (Long) itr.next();
                            DiscordantTally dt = (DiscordantTally) tempDiscordantCounts.get(key);
                            dt.normalize(tempnorm);
                            DiscordantTally totalDT = getTally(key, totalDiscordantCounts);
                            totalDT.combine(dt);
                        }
                    }
                }
            }
            ArrayList sortedKeySet = new ArrayList(fullProbMap.getKeySet());
            Collections.sort(sortedKeySet);
            for (int j = 0; j < sortedKeySet.size(); j++) {
                if (fullProbMap.get(sortedKeySet.get(j)) > .001) {
                    obsT.add(new Double(totalT.get(sortedKeySet.get(j))));
                    obsU.add(new Double(totalU.get(sortedKeySet.get(j))));
                    if (Options.getTdtType() == TDT_PAREN) {
                        if (totalDiscordantCounts.containsKey(sortedKeySet.get(j))) {
                            discordantCounts.add(((DiscordantTally) totalDiscordantCounts.get(sortedKeySet.get(j))).getCounts());
                        } else {
                            discordantCounts.add(new double[9]);
                        }
                    }
                }
            }
        }
        if (Options.getAssocTest() == ASSOC_TRIO) {
            this.obsT = obsT;
            this.obsU = obsU;
        } else if (Options.getAssocTest() == ASSOC_CC) {
            this.caseCounts = caseCounts;
            this.controlCounts = controlCounts;
        }
    }

    private DiscordantTally getTally(Long key, HashMap tallyMap) {
        DiscordantTally dt;
        if (tallyMap.containsKey(key)) {
            dt = (DiscordantTally) tallyMap.get(key);
        } else {
            dt = new DiscordantTally(key.longValue());
            tallyMap.put(key, dt);
        }
        return dt;
    }

    public int read_observations(int num_haplos, int num_loci, byte[][] haplo, int start_locus, int end_locus) throws HaploViewException {
        int i, j, a1, a2, two_n, num_poss, loc, ind;
        long h1, h2;
        byte c1, c2;
        int currentInd = 0;
        int[] dhet = new int[MAXLOCI];
        int[] missing1 = new int[MAXLOCI];
        int[] missing2 = new int[MAXLOCI];
        for (i = 0; i < MAXLOCI; ++i) {
            dhet[i] = 0;
            missing1[i] = 0;
            missing2[i] = 0;
        }
        for (ind = 0; ind < num_haplos; ind += 2) {
            two_n = 1;
            h1 = h2 = 0;
            num_poss = 1;
            for (loc = start_locus; loc <= end_locus; loc++) {
                i = loc - start_locus;
                c1 = haplo[ind][loc];
                c2 = haplo[ind + 1][loc];
                if (c1 == 'h' || c1 == '9') {
                    a1 = 0;
                    a2 = 1;
                    dhet[i] = 1;
                    missing1[i] = 0;
                    missing2[i] = 0;
                } else {
                    dhet[i] = 0;
                    missing1[i] = 0;
                    missing2[i] = 0;
                    if (c1 > '0' && c1 < '3') {
                        a1 = c1 - '1';
                    } else {
                        a1 = 0;
                        missing1[i] = 1;
                    }
                    if (c2 > '0' && c2 < '3') {
                        a2 = c2 - '1';
                    } else {
                        a2 = 0;
                        missing2[i] = 1;
                    }
                }
                h1 += two_n * a1;
                h2 += two_n * a2;
                if (dhet[i] == 1) num_poss *= 2;
                if (missing1[i] == 1) num_poss *= 2;
                if (missing2[i] == 1) num_poss *= 2;
                two_n *= 2;
            }
            Recovery tempRec;
            if (data[currentInd].poss.size() < num_poss) {
                for (int k = data[currentInd].poss.size(); k < num_poss; k++) {
                    tempRec = new Recovery();
                    data[currentInd].poss.add(tempRec);
                }
            }
            data[currentInd].nposs = num_poss;
            tempRec = (Recovery) data[currentInd].poss.elementAt(0);
            tempRec.h1 = h1;
            tempRec.h2 = h2;
            tempRec.p = 0.0f;
            two_n = 1;
            num_poss = 1;
            for (i = 0; i <= end_locus - start_locus; i++) {
                if (dhet[i] != 0) {
                    for (j = 0; j < num_poss; j++) {
                        tempRec = (Recovery) data[currentInd].poss.elementAt(j);
                        h1 = tempRec.h1;
                        h2 = tempRec.h2;
                        if ((h1 & two_n) == two_n && (h2 & two_n) == 0) {
                            h1 -= two_n;
                            h2 += two_n;
                        } else if ((h1 & two_n) == 0 && (h2 & two_n) == two_n) {
                            h1 += two_n;
                            h2 -= two_n;
                        } else {
                        }
                        tempRec = (Recovery) data[currentInd].poss.elementAt(num_poss + j);
                        tempRec.h1 = h1;
                        tempRec.h2 = h2;
                        tempRec.p = 0.0f;
                    }
                    num_poss *= 2;
                }
                if (missing1[i] != 0) {
                    for (j = 0; j < num_poss; j++) {
                        tempRec = (Recovery) data[currentInd].poss.elementAt(j);
                        h1 = tempRec.h1;
                        h2 = tempRec.h2;
                        if ((h1 & two_n) == 0) {
                            h1 += two_n;
                        } else {
                        }
                        tempRec = (Recovery) data[currentInd].poss.elementAt(num_poss + j);
                        tempRec.h1 = h1;
                        tempRec.h2 = h2;
                        tempRec.p = 0.0f;
                    }
                    num_poss *= 2;
                }
                if (missing2[i] != 0) {
                    for (j = 0; j < num_poss; j++) {
                        tempRec = (Recovery) data[currentInd].poss.elementAt(j);
                        h1 = tempRec.h1;
                        h2 = tempRec.h2;
                        if ((h2 & two_n) == 0) {
                            h2 += two_n;
                        } else {
                        }
                        tempRec = (Recovery) data[currentInd].poss.elementAt(num_poss + j);
                        tempRec.h1 = h1;
                        tempRec.h2 = h2;
                        tempRec.p = 0.0f;
                    }
                    num_poss *= 2;
                }
                two_n *= 2;
            }
            currentInd++;
        }
        return (currentInd);
    }

    public void store_block_haplos(int[][] hlist, double[][] hprob, int[] hint, int block, int num_indivs) {
        int i, j, k, num_poss, h1, h2;
        Recovery tempRec;
        for (i = 0; i < num_indivs; i++) {
            num_poss = 0;
            for (j = 0; j < data[i].nposs; j++) {
                tempRec = (Recovery) data[i].poss.elementAt(j);
                h1 = (int) tempRec.h1;
                h2 = (int) tempRec.h2;
                if (hint[h1] >= 0 && hint[h2] >= 0) {
                    num_poss++;
                }
            }
            superdata[i].nposs[block] = num_poss;
            if (num_poss > 0) {
                superdata[i].poss[block] = new Recovery[num_poss];
                for (int ii = 0; ii < num_poss; ++ii) superdata[i].poss[block][ii] = new Recovery();
                k = 0;
                for (j = 0; j < data[i].nposs; j++) {
                    tempRec = (Recovery) data[i].poss.elementAt(j);
                    h1 = (int) tempRec.h1;
                    h2 = (int) tempRec.h2;
                    if (hint[h1] >= 0 && hint[h2] >= 0) {
                        superdata[i].poss[block][k].h1 = hint[h1];
                        superdata[i].poss[block][k].h2 = hint[h2];
                        k++;
                    }
                }
            }
        }
    }

    public int[] decode_haplo_str(long chap, int num_blocks, int[] block_size, int[][] hlist, int[] num_hlist) {
        int i, val, size = 0, counter = 0;
        for (i = 0; i < num_blocks; i++) {
            size += block_size[i];
        }
        int[] decoded = new int[size];
        for (i = 0; i < num_blocks; i++) {
            val = (int) (chap % num_hlist[i]);
            for (int j = 0; j < block_size[i]; j++) {
                if ((hlist[i][val] & two_n[j]) == two_n[j]) {
                    decoded[counter] = 2;
                } else {
                    decoded[counter] = 1;
                }
                counter++;
            }
            chap -= val;
            chap /= num_hlist[i];
        }
        return decoded;
    }

    public void create_super_haplos(int num_indivs, int num_blocks, int[] num_hlist) {
        int i, j, num_poss, h1, h2;
        for (i = 0; i < num_indivs; i++) {
            num_poss = 1;
            for (j = 0; j < num_blocks; j++) {
                num_poss *= superdata[i].nposs[j];
            }
            superdata[i].nsuper = 0;
            superdata[i].superposs = new Recovery[num_poss];
            for (int ii = 0; ii < num_poss; ++ii) superdata[i].superposs[ii] = new Recovery();
            for (j = 0; j < superdata[i].nposs[0]; j++) {
                h1 = (int) superdata[i].poss[0][j].h1;
                h2 = (int) superdata[i].poss[0][j].h2;
                recursive_superposs(h1, h2, 1, num_blocks, num_hlist, i);
            }
            if (superdata[i].nsuper != num_poss) {
                System.out.println("error in superfill" + " " + i);
            }
        }
    }

    public void recursive_superposs(long h1, long h2, int block, int num_blocks, int[] num_hlist, int indiv) {
        int j;
        long newh1, newh2, curr_prod;
        if (block == num_blocks) {
            superdata[indiv].superposs[superdata[indiv].nsuper].h1 = h1;
            superdata[indiv].superposs[superdata[indiv].nsuper].h2 = h2;
            superdata[indiv].nsuper++;
        } else {
            curr_prod = 1;
            for (j = 0; j < block; j++) {
                curr_prod *= num_hlist[j];
            }
            for (j = 0; j < superdata[indiv].nposs[block]; j++) {
                newh1 = h1 + (superdata[indiv].poss[block][j].h1 * curr_prod);
                newh2 = h2 + (superdata[indiv].poss[block][j].h2 * curr_prod);
                recursive_superposs(newh1, newh2, block + 1, num_blocks, num_hlist, indiv);
            }
        }
    }

    void store_dhet_status(int num_haplos, int num_loci, byte[][] haplo) {
        int ind, loc;
        byte c1, c2;
        for (ind = 0; ind + 2 < num_haplos; ind += 4) {
            for (loc = 0; loc < num_loci; loc++) {
                c1 = haplo[ind][loc];
                c2 = haplo[ind + 2][loc];
                ambighet[ind / 4][loc] = 0;
                if (c1 == 'h' || c1 == '9') {
                    if (c2 == 'h' || c2 == '9' || c2 == '0') {
                        ambighet[ind / 4][loc] = 1;
                    }
                } else if (c1 == '0' && (c2 == 'h' || c2 == '9')) {
                    ambighet[ind / 4][loc] = 1;
                }
            }
        }
    }

    boolean kid_consistent(long chap1, long chap2, int num_blocks, int[] block_size, int[][] hlist, int[] num_hlist, int this_trio, int num_loci) {
        int i;
        boolean retval;
        int[] temp1 = decode_haplo_str(chap1, num_blocks, block_size, hlist, num_hlist);
        int[] temp2 = decode_haplo_str(chap2, num_blocks, block_size, hlist, num_hlist);
        retval = true;
        for (i = 0; i < num_loci; i++) {
            if (ambighet[this_trio][i] != 0) {
                if (temp1[i] == temp2[i]) {
                    retval = false;
                    break;
                }
            }
        }
        return (retval);
    }

    public int[][] getHaplotypes() {
        return haplotypes;
    }

    public void setHaplotypes(int[][] haplotypes) {
        this.haplotypes = haplotypes;
    }

    public double[] getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(double[] frequencies) {
        this.frequencies = frequencies;
    }

    public double getTransCount(int i) {
        return ((Double) obsT.elementAt(i)).doubleValue();
    }

    public void setObsT(Vector obsT) {
        this.obsT = obsT;
    }

    public double getUntransCount(int i) {
        return ((Double) obsU.elementAt(i)).doubleValue();
    }

    public void setObsU(Vector obsU) {
        this.obsU = obsU;
    }

    public double getControlCount(int i) {
        return ((Double) controlCounts.elementAt(i)).doubleValue();
    }

    public void setControlCounts(Vector controlCounts) {
        this.controlCounts = controlCounts;
    }

    public double getCaseCount(int i) {
        return ((Double) caseCounts.elementAt(i)).doubleValue();
    }

    public void setCaseCounts(Vector caseCounts) {
        this.caseCounts = caseCounts;
    }

    public int numHaplos() {
        return haplotypes.length;
    }

    public double[] getDiscordantCounts(int i) {
        return (double[]) discordantCounts.get(i);
    }
}
