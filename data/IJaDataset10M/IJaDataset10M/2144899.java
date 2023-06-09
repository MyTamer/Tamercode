package hibernate.classes.guarana;

/**
 * BlastxXyva generated by hbm2java
 */
public class BlastxXyva implements java.io.Serializable {

    private GruposMontagemId id;

    private byte[] data;

    private GruposMontagem grupo;

    public GruposMontagem getGrupo() {
        return grupo;
    }

    public void setGrupo(GruposMontagem grupo) {
        this.grupo = grupo;
    }

    public BlastxXyva() {
    }

    public BlastxXyva(GruposMontagemId id) {
        this.id = id;
    }

    public BlastxXyva(GruposMontagemId id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public GruposMontagemId getId() {
        return this.id;
    }

    public void setId(GruposMontagemId id) {
        this.id = id;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
