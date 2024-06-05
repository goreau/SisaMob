package producao;

public class RelatorioList {
    private String atividade;
    private String registros;
    private String detalhe;

    public RelatorioList(String atividade, String det, String registros) {
        super();
        this.atividade = atividade;
        this.detalhe = det;
        this.registros = registros;
    }
    public String getAtividade() {
        return atividade;
    }
    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }
    public String getRegistros() {
        return registros;
    }
    public void setRegistros(String registros) {
        this.registros = registros;
    }
    public String getDetalhe() {
        return detalhe;
    }
    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }
}
