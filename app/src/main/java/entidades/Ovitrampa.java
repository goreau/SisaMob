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

public class Ovitrampa {
    private int id_ovitrampa;
    private int id_municipio;
    private int id_quarteirao;
    private String cadastro;
    private String endereco;
    public List<String> id_Ovi;
    private Context context;
    MyToast toast;

    public Ovitrampa() {
        context = PrincipalActivity.getSisamobContext();
    }

    public Ovitrampa(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }

    public Ovitrampa(int id_ovitrampa, int id_municipio, int id_quarteirao,
                     String cadastro, String endereco, Context context) {
        super();
        this.id_ovitrampa = id_ovitrampa;
        this.id_municipio = id_municipio;
        this.id_quarteirao = id_quarteirao;
        this.cadastro = cadastro;
        this.endereco = endereco;
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }

    public int getId_ovitrampa() {
        return id_ovitrampa;
    }

    public void setId_ovitrampa(int id_ovitrampa) {
        this.id_ovitrampa = id_ovitrampa;
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

    public String getCadastro() {
        return cadastro;
    }

    public void setCadastro(String cadastro) {
        this.cadastro = cadastro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<String> getId_Ovi() {
        return id_Ovi;
    }

    public void setId_Ovi(List<String> id_Ovi) {
        this.id_Ovi = id_Ovi;
    }

    public void limpar(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            db.getWritableDatabase().delete("ovitrampa", null, null);
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
            db.getWritableDatabase().insert("ovitrampa", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public List<String> combo(String id){
        List<String> im = new ArrayList<String>();
        id_Ovi = new ArrayList<String>();

        GerenciarBanco db = new GerenciarBanco(this.context);
        String sql = "SELECT id_ovitrampa, trim(cadastro)|| ' - ' || trim(endereco) as codigo from ovitrampa where id_municipio=?";
        Cursor cursor = db.getReadableDatabase().rawQuery(sql, new String[]{id});
        if(cursor.moveToFirst()){
            do {
                im.add(cursor.getString(1));
                id_Ovi.add(cursor.getString(0));
            } while (cursor.moveToNext());
        } else {
            im.add(id + "- Ovitrampa" );
        }
        cursor.close();
        db.close();
        return im;
    }
}
