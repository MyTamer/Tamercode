package net.minecraft.src;

public class ServerNBTStorage {

    /** User specified name for server */
    public String name;

    /** Hostname or IP address of server */
    public String host;

    /** The count/max number of players */
    public String playerCount;

    /** Server's Message of the Day */
    public String motd;

    /** Lag meter; -2 if server check pending; -1 if server check failed */
    public long lag;

    /** True if server was already polled or is in the process of polling */
    public boolean polled;

    public ServerNBTStorage(String par1Str, String par2Str) {
        polled = false;
        name = par1Str;
        host = par2Str;
    }

    /**
     * Return a new NBTTagCompound representation of this ServerNBTStorage
     */
    public NBTTagCompound getCompoundTag() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("name", name);
        nbttagcompound.setString("ip", host);
        return nbttagcompound;
    }

    /**
     * Factory method to create ServerNBTStorage object from a NBTTagCompound
     */
    public static ServerNBTStorage createServerNBTStorage(NBTTagCompound par0NBTTagCompound) {
        return new ServerNBTStorage(par0NBTTagCompound.getString("name"), par0NBTTagCompound.getString("ip"));
    }
}
