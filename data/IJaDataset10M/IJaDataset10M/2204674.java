package eisbot.proxy.types;

/**
 * Represents a StarCraft bullet type.
 * 
 * For a description of fields see: http://code.google.com/p/bwapi/wiki/BulletType
 */
public class BulletType {

    public static final int numAttributes = 1;

    private String name;

    private int ID;

    public enum BulletTypes {

        Melee, Undefined1, Undefined2, Undefined3, Undefined4, Undefined5, Undefined6, Undefined7, Undefined8, Undefined9, Undefined10, Undefined11, Undefined12, Undefined13, Undefined14, Undefined15, Undefined16, Undefined17, Undefined18, Undefined19, Undefined20, Undefined21, Undefined22, Undefined23, Undefined24, Undefined25, Undefined26, Undefined27, Undefined28, Undefined29, Undefined30, Undefined31, Undefined32, Undefined33, Undefined34, Undefined35, Undefined36, Undefined37, Undefined38, Undefined39, Undefined40, Undefined41, Undefined42, Undefined43, Undefined44, Undefined45, Undefined46, Undefined47, Undefined48, Undefined49, Undefined50, Undefined51, Undefined52, Undefined53, Undefined54, Undefined55, Undefined56, Undefined57, Undefined58, Undefined59, Undefined60, Undefined61, Undefined62, Undefined63, Undefined64, Undefined65, Undefined66, Undefined67, Undefined68, Undefined69, Undefined70, Undefined71, Undefined72, Undefined73, Undefined74, Undefined75, Undefined76, Undefined77, Undefined78, Undefined79, Undefined80, Undefined81, Undefined82, Undefined83, Undefined84, Undefined85, Undefined86, Undefined87, Undefined88, Undefined89, Undefined90, Undefined91, Undefined92, Undefined93, Undefined94, Undefined95, Undefined96, Undefined97, Undefined98, Undefined99, Undefined100, Undefined101, Undefined102, Undefined103, Undefined104, Undefined105, Undefined106, Undefined107, Undefined108, Undefined109, Undefined110, Undefined111, Undefined112, Undefined113, Undefined114, Undefined115, Undefined116, Undefined117, Undefined118, Undefined119, Undefined120, Undefined121, Undefined122, Undefined123, Undefined124, Undefined125, Undefined126, Undefined127, Undefined128, Undefined129, Undefined130, Undefined131, Undefined132, Undefined133, Undefined134, Undefined135, Undefined136, Undefined137, Undefined138, Undefined139, Undefined140, Fusion_Cutter_Hit, Gauss_Rifle_Hit, C_10_Canister_Rifle_Hit, Gemini_Missiles, Fragmentation_Grenade, Longbolt_Missile, Undefined147, ATS_ATA_Laser_Battery, Burst_Lasers, Arclite_Shock_Cannon_Hit, EMP_Missile, Dual_Photon_Blasters_Hit, Particle_Beam_Hit, Anti_Matter_Missile, Pulse_Cannon, Psionic_Shockwave_Hit, Psionic_Storm, Yamato_Gun, Phase_Disruptor, STA_STS_Cannon_Overlay, Sunken_Colony_Tentacle, Undefined162, Acid_Spore, Undefined164, Glave_Wurm, Seeker_Spores, Queen_Spell_Carrier, Plague_Cloud, Consume, Undefined170, Needle_Spine_Hit, Invisible, Undefined173, Undefined174, Undefined175, Undefined176, Undefined177, Undefined178, Undefined179, Undefined180, Undefined181, Undefined182, Undefined183, Undefined184, Undefined185, Undefined186, Undefined187, Undefined188, Undefined189, Undefined190, Undefined191, Undefined192, Undefined193, Undefined194, Undefined195, Undefined196, Undefined197, Undefined198, Undefined199, Undefined200, Optical_Flare_Grenade, Halo_Rockets, Subterranean_Spines, Corrosive_Acid_Shot, Undefined205, Neutron_Flare, Undefined207, Undefined208, None, Unknown
    }

    ;

    public BulletType(int[] data, int index) {
        ID = data[index++];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }
}
