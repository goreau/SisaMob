package webservice;

/**
 * Created by allanromanato on 11/4/15.
 */
public class SuspeitoObj {
    private String id_inseto_suspeito;
    private String id_municipio;
    private String localidade;
    private String qt_insetos;
    private String dt_captura;
    private String id_usuario;


    public String getId_inseto_suspeito() {
        return id_inseto_suspeito;
    }

    public void setId_inseto_suspeito(String id_inseto_suspeito) {
        this.id_inseto_suspeito = id_inseto_suspeito;
    }

    public String getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(String id_municipio) {
        this.id_municipio = id_municipio;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getQt_insetos() {
        return qt_insetos;
    }

    public void setQt_insetos(String qt_insetos) {
        this.qt_insetos = qt_insetos;
    }

    public String getDt_captura() {
        return dt_captura;
    }

    public void setDt_captura(String dt_captura) {
        this.dt_captura = dt_captura;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}
