package producao;

public class RecipienteList {
    int id;
    private String tipo;
    private String amostra;

    public RecipienteList(String amostra, String tipo, int id) {
        super();
        this.id			= id;
        this.tipo 		= tipo;
        this.amostra 	= amostra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAmostra() {
        return amostra;
    }

    public void setAmostra(String amostra) {
        this.amostra = amostra;
    }



}
