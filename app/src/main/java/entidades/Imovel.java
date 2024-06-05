package entidades;

import java.util.ArrayList;
import java.util.List;

import utilitarios.GerenciarBanco;
import utilitarios.MyToast;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.Toast;

import com.sucen.sisamob.PrincipalActivity;

public class Imovel {
    private int id_imovel;
    private int id_municipio;
    private int id_quarteirao;
    private String numero_imovel;
    private String endereco;
    private int id_atividade;
    public List<String> id_Im;
    private Context context;
    MyToast toast;

    public Imovel() {
        context = PrincipalActivity.getSisamobContext();
    }

    public Imovel(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public Imovel(int id_imovel, int id_municipio, int id_quarteirao,
                  String numero_imovel, String endereco, int id_atividade,
                  Context context) {
        super();
        this.id_imovel = id_imovel;
        this.id_municipio = id_municipio;
        this.id_quarteirao = id_quarteirao;
        this.numero_imovel = numero_imovel;
        this.endereco = endereco;
        this.id_atividade = id_atividade;
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }

    public int getId_imovel() {
        return id_imovel;
    }
    public void setId_imovel(int id_imovel) {
        this.id_imovel = id_imovel;
    }
    public int getId_municipio() {
        return id_municipio;
    }
    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }
    public int getId_quarteirao() {
        return id_quarteirao;
    }
    public void setId_quarteirao(int id_quarteirao) {
        this.id_quarteirao = id_quarteirao;
    }
    public String getNumero_imovel() {
        return numero_imovel;
    }
    public void setNumero_imovel(String numero_imovel) {
        this.numero_imovel = numero_imovel;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public int getId_atividade() {
        return id_atividade;
    }
    public void setId_atividade(int id_atividade) {
        this.id_atividade = id_atividade;
    }

    public void limpar(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            db.getWritableDatabase().delete("imovel", null, null);
        } catch (SQLException e) {
            toast.show(e.getMessage());
        }
    }

    public boolean insere(String[] campos, String[] val){
        GerenciarBanco db = new GerenciarBanco(this.context);

        try{
            ContentValues valores = new ContentValues();
            for (int i = 0; i<val.length;i++){
                valores.put(campos[i],val[i]);
            }
            db.getWritableDatabase().insert("imovel", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public List<String> combo(String id, String ativ){
        List<String> im = new ArrayList<String>();
        id_Im = new ArrayList<String>();
        String sql = "";

        GerenciarBanco db = new GerenciarBanco(this.context);
        if (ativ == "0"){
            sql = "SELECT id_imovel, case when id_atividade=1 then 'PE' else 'IE' end || ' Cad: ' || trim(numero_imovel)|| ' - ' || trim(endereco) as codigo, " +
                    "CASE WHEN (NOT TRIM(numero_imovel) GLOB '*[^0-9]*') AND TRIM(numero_imovel) like '_%' THEN CAST(TRIM(numero_imovel) AS INTEGER) ELSE TRIM(numero_imovel) END AS ordena " +
                    "from imovel where id_municipio=? and id_atividade>? ORDER BY ordena";
        } else {
            sql = "SELECT id_imovel, trim(numero_imovel)|| ' - ' || trim(endereco) as codigo, " +
                    "CASE WHEN (NOT TRIM(numero_imovel) GLOB '*[^0-9]*') AND TRIM(numero_imovel) like '_%' THEN CAST(TRIM(numero_imovel) AS INTEGER) ELSE TRIM(numero_imovel) END AS ordena " +
                    "from imovel where id_municipio=? and id_atividade=? ORDER BY ordena";
        }
        Cursor cursor = db.getReadableDatabase().rawQuery(sql, new String[]{id, ativ});
        if(cursor.moveToFirst()){
            do {
                im.add(cursor.getString(1));
                id_Im.add(cursor.getString(0));
            } while (cursor.moveToNext());
        } else {
            im.add("Favor recarregar o conteudo");
        }
        cursor.close();
        db.close();
        return im;
    }
}
