package net.minecraft.src;

class StructureStrongholdPieceWeight {

    public Class pieceClass;

    /**
     * 'This basically keeps track of the 'epicness' of a structure. Epic structure components have a higher 'weight',
     * and Structures may only grow up to a certain 'weight' before generation is stopped'
     */
    public final int pieceWeight;

    public int instancesSpawned;

    /** 'How many Structure Pieces of this type may spawn in a structure' */
    public int instancesLimit;

    public StructureStrongholdPieceWeight(Class par1Class, int par2, int par3) {
        pieceClass = par1Class;
        pieceWeight = par2;
        instancesLimit = par3;
    }

    public boolean canSpawnMoreStructuresOfType(int par1) {
        return instancesLimit == 0 || instancesSpawned < instancesLimit;
    }

    public boolean canSpawnMoreStructures() {
        return instancesLimit == 0 || instancesSpawned < instancesLimit;
    }
}
