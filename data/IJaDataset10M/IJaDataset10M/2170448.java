package dominio.entity;

/**
 * Arquivoscasoestudo generated by hbm2java
 */
public class Arquivoscasoestudo implements java.io.Serializable {

    private Integer idArquivos;

    private Casoestudo casoestudo;

    private String descricaoArquivo;

    private String caminhoArquivo;

    public Arquivoscasoestudo() {
    }

    public Arquivoscasoestudo(Casoestudo casoestudo) {
        this.casoestudo = casoestudo;
    }

    public Arquivoscasoestudo(Casoestudo casoestudo, String descricaoArquivo, String caminhoArquivo) {
        this.casoestudo = casoestudo;
        this.descricaoArquivo = descricaoArquivo;
        this.caminhoArquivo = caminhoArquivo;
    }

    public Integer getIdArquivos() {
        return this.idArquivos;
    }

    public void setIdArquivos(Integer idArquivos) {
        this.idArquivos = idArquivos;
    }

    public Casoestudo getCasoestudo() {
        return this.casoestudo;
    }

    public void setCasoestudo(Casoestudo casoestudo) {
        this.casoestudo = casoestudo;
    }

    public String getDescricaoArquivo() {
        return this.descricaoArquivo;
    }

    public void setDescricaoArquivo(String descricaoArquivo) {
        this.descricaoArquivo = descricaoArquivo;
    }

    public String getCaminhoArquivo() {
        return this.caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }
}
