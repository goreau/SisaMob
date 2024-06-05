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

public class GrupoRec {
    private int id_grupo_rec;
    private String codigo;
    private String nome;
    private Context context;
    public List<String> id_grupo;
    MyToast toast;

    public GrupoRec() {
        context = PrincipalActivity.getSisamobContext();
    }

    public GrupoRec(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public GrupoRec(int id_grupo_rec, String codigo, String nome,
                    Context context) {
        super();
        this.id_grupo_rec = id_grupo_rec;
        this.codigo = codigo;
        this.nome = nome;
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public int getId_grupo_rec() {
        return id_grupo_rec;
    }
    public void setId_grupo_rec(int id_grupo_rec) {
        this.id_grupo_rec = id_grupo_rec;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void limpar(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            db.getWritableDatabase().delete("grupo_rec", null, null);
        } catch (SQLException e) {
            toast.show(e.getMessage());
        }
    }

    public boolean insere(String[] campos, String[] val){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{;
            ContentValues valores = new ContentValues();
            for (int i = 0; i<val.length;i++){
                valores.put(campos[i],val[i]);
            }
            db.getWritableDatabase().insert("grupo_rec", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }

    }

    public List<String> combo(){
        List<String> grupo = new ArrayList<String>();
        id_grupo = new ArrayList<String>();
        GerenciarBanco db = new GerenciarBanco(this.context);
        String sql = "SELECT id_grupo_rec, codigo || ' - ' || nome as codigo FROM grupo_rec";

        Cursor cursor = db.getReadableDatabase().rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                grupo.add(cursor.getString(1));
                id_grupo.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return grupo;
    }


}
