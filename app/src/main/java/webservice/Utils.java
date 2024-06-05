package webservice;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entidades.Area;
import entidades.Area_nav;
import entidades.Atividade;
import entidades.Censitario;
import entidades.GrupoRec;
import entidades.Imovel;
import entidades.Municipio;
import entidades.Ovitrampa;
import entidades.Produto;
import entidades.Quart_nav;
import entidades.Quarteirao;
import entidades.TipoRec;
import producao.Alado;
import producao.AladoIm;
import producao.Coordenadas;
import producao.Recipiente;
import producao.VcFolha;
import producao.VcImovel;
import producao.VcOvitrampa;


public class Utils {
    static Context contexto;

    public Utils(Context ctx) {
        contexto = ctx;
    }

    public boolean limpar = true;

    public String getInformacao(String end){
        String json;
        String retorno;
        json = NetworkUtils.getJSONFromAPI(end);
        Log.i("Resultado", json);

        retorno = parseJson(json);

        return retorno;
    }

    public String sendInformacao(String end, int tab){
        String json;
        String retorno;
        json = NetworkUtils.getJSONFromAPI(end);
        Log.i("Resultado", json);

        retorno = parseRetorno(json, tab);

        return retorno;
    }

    private String parseJson(String json){
        int quant = 0;
        int linhas = 0;
        int inseridos = 0;
        String tabela = "";
        String resultado = "";
        try {
            if (json==""){
                resultado = "Erro recebendo os registros do servidor";
            } else {
                JSONObject dados = new JSONObject(json);
                //  JSONObject dados = jsonObj.getJSONObject("dados");
                JSONArray tabelas = dados.names();
                quant = tabelas.length();
                //  Log.w("tabelas",""+dados.names());
                for (int j = 0; j < quant; j++) {
                    tabela = tabelas.getString(j); //nome da tabela
                    JSONArray objetos = dados.getJSONArray(tabela);//array da tabela do banco (municipio, area,..)
             /*   if (tabela.equals("atividade")){
                    jsonObj = new JSONObject();

                    /*jsonObj.put("id_atividade","99");
                    jsonObj.put("nome","Alado Im Cad");
                    jsonObj.put("grupo","2");*/
                    //   objetos.put(jsonObj);
                    //   }
                    linhas = objetos.length(); //registros da tabela
                    if (linhas == 0) continue;
                    //   Log.w("Registros",""+linhas);
                    JSONArray names = objetos.getJSONObject(0).names(); //nomes dos campos
                    //   Log.w("Campos",""+names);
                    int fields = names.length(); //quantidade de campos
                    String[] campos = new String[fields];
                    String[] valores = new String[fields];

                    for (int x = 0; x < linhas; x++) {
                        for (int i = 0; i < fields; i++) {
                            campos[i] = names.getString(i);
                            valores[i] = objetos.getJSONObject(x).getString(names.getString(i));
                        }
                        if (tabela.equals("municipio")) {
                            Municipio mun = new Municipio();
                            if (inseridos == 0 && limpar) mun.limpar();
                            if (mun.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("area_nav")) {
                            Area_nav area = new Area_nav();
                            if (inseridos == 0 && limpar) area.limpar();
                            if (area.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("quart_nav")) {
                            Quart_nav quart = new Quart_nav();
                            if (inseridos == 0 && limpar) quart.limpar();
                            if (quart.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("area")) {
                            Area area = new Area();
                            if (inseridos == 0 && limpar) area.limpar();
                            if (area.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("censitario")) {
                            Censitario cens = new Censitario();
                            if (inseridos == 0 && limpar) cens.limpar();
                            if (cens.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("quarteirao")) {
                            Quarteirao quart = new Quarteirao();
                            if (inseridos == 0 && limpar) quart.limpar();
                            if (quart.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("imovel")) {
                            Imovel im = new Imovel();
                            if (inseridos == 0 && limpar) im.limpar();
                            if (im.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("ovitrampa")) {
                            Ovitrampa ovi = new Ovitrampa();
                            if (inseridos == 0 && limpar) ovi.limpar();
                            if (ovi.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("atividade")) {
                            Atividade cad = new Atividade();
                            if (inseridos == 0) cad.limpar();
                            // String[] camposat = new String[campos.length+1];
                            // String[] valoresat = new String[valores.length+1];
                            // camposat[campos.length] = "";
                            //    valoresat[valores.length] = "";
                            if (cad.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("grupo_rec")) {
                            GrupoRec cad = new GrupoRec();
                            if (inseridos == 0) cad.limpar();
                            if (cad.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("tipo_rec")) {
                            TipoRec cad = new TipoRec();
                            if (inseridos == 0) cad.limpar();
                            if (cad.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("municipio")) {
                            Municipio cad = new Municipio();
                            if (inseridos == 0) cad.limpar();
                            if (cad.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("produto")) {
                            Produto cad = new Produto();
                            if (inseridos == 0) cad.limpar();
                            if (cad.insere(campos, valores))
                                inseridos++;
                        }
                    }
                    resultado += "  -" + tabela + ": " + inseridos;
                    if (inseridos > 1)
                        resultado += " registros\n";
                    else
                        resultado += " registro\n";
                    inseridos = 0;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            inseridos = -1;
            resultado = e.getMessage();
        }
        return resultado;
    }

    //recebimento da resposta do envio (atualização do status)
    public String parseRetorno(String json, int tab){
        int quant = 0;
        int linhas = 0;
        int inseridos = 0;
        String tabela = "";
        String resultado = "";
      //  json = json.substring(4);
       // Log.i("tabela Parse ret: " , "- "+tab);
        try {
            JSONArray registros = new JSONArray(json);
            quant = registros.length();

            for (int j=0;j<quant;j++) {
                JSONObject obj = registros.getJSONObject(j);//array da tabela do banco (municipio, area,..)
                switch (tab){
                    case 1:
                        VcFolha folha = new VcFolha(0);
                        tabela = "Visitas a Imóveis ";
                        folha.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    case 2:
                        VcImovel im = new VcImovel(0);
                        tabela = "Imóveis Cadastrados ";
                        im.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    case 3:
                        VcOvitrampa ovi = new VcOvitrampa(0);
                        tabela = "Ovitrampas ";
                        ovi.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    case 4:
                        Coordenadas coord = new Coordenadas(0);
                        tabela = "Coordenadas ";
                        coord.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    case 5:
                        Alado al = new Alado(0);
                        tabela = "Alado (Pré e Pós)";
                        al.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    case 6:
                        AladoIm alim = new AladoIm(0);
                        tabela = "Alado Im Cad";
                        alim.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    default:
                        Recipiente rec = new Recipiente(0);
                        rec.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                }

                inseridos += Integer.parseInt(obj.get("status").toString());
            }
            resultado += "  -" + tabela + ": " + inseridos;
            if (inseridos>1)
                resultado += " registros\n";
            else
                resultado += " registro\n";
        } catch (JSONException e) {
            e.printStackTrace();
            inseridos = -1;
            resultado = json;//e.getMessage();
        }
        return resultado;
    }
}
