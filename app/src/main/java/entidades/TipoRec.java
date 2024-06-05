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

public class TipoRec {
    private int id_tipo_rec;
    private int id_grupo_rec;
    private int codigo;
    private String nome;
    private Context context;
    public List<String> id_Tipo;
    MyToast toast;

    public TipoRec() {
        context = PrincipalActivity.getSisamobContext();
    }

    public TipoRec(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public TipoRec(int id_tipo_rec, int id_grupo_rec, int codigo, String nome,
                   Context context) {
        super();
        this.id_tipo_rec = id_tipo_rec;
        this.id_grupo_rec = id_grupo_rec;
        this.codigo = codigo;
        this.nome = nome;
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public int getId_tipo_rec() {
        return id_tipo_rec;
    }
    public void setId_tipo_rec(int id_tipo_rec) {
        this.id_tipo_rec = id_tipo_rec;
    }
    public int getId_grupo_rec() {
        return id_grupo_rec;
    }
    public void setId_grupo_rec(int id_grupo_rec) {
        this.id_grupo_rec = id_grupo_rec;
    }
    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
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
            db.getWritableDatabase().delete("tipo_rec", null, null);
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
            db.getWritableDatabase().insert("tipo_rec", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }

    }

    public List<String> combo(String id){
        List<String> tipo = new ArrayList<String>();
        id_Tipo = new ArrayList<String>();
        GerenciarBanco db = new GerenciarBanco(this.context);
        String sql = "SELECT id_tipo_rec, codigo || ' - ' || nome as codigo FROM tipo_rec t where id_grupo_rec=? order by t.codigo";

        Cursor cursor = db.getReadableDatabase().rawQuery(sql, new String[]{id});
        if(cursor.moveToFirst()){
            do {
                tipo.add(cursor.getString(1));
                id_Tipo.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tipo;
    }
}
