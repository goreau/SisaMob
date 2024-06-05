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

public class Produto {
    private int id_produto;
    private String codigo;
    private String nome;
    private int tipo_uso;
    public List<String> id_prod;
    private Context context;
    MyToast toast;

    public Produto() {
        context = PrincipalActivity.getSisamobContext();
    }

    public Produto(int id_produto, String codigo, String nome, int tipo_uso,
                   List<String> id_prod, Context context) {
        super();
        this.id_produto = id_produto;
        this.codigo = codigo;
        this.nome = nome;
        this.tipo_uso = tipo_uso;
        this.id_prod = id_prod;
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }

    public Produto(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
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

    public int getTipo_uso() {
        return tipo_uso;
    }

    public void setTipo_uso(int tipo_uso) {
        this.tipo_uso = tipo_uso;
    }

    public List<String> getId_prod() {
        return id_prod;
    }

    public void setId_prod(List<String> id_prod) {
        this.id_prod = id_prod;
    }

    public void limpar(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            db.getWritableDatabase().delete("produto", null, null);
        } catch (SQLException e) {
            toast.show(e.getMessage());
        } finally {
            db.close();
        }
    }

    public boolean insere(String[] campos, String[] val){
        GerenciarBanco db = new GerenciarBanco(this.context);

        try{
            ContentValues valores = new ContentValues();
            for (int i = 0; i<val.length;i++){
                valores.put(campos[i],val[i]);
            }
            db.getWritableDatabase().insert("produto", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }

    }

    public List<String> combo(String tipo){
        List<String> prod = new ArrayList<String>();
        id_prod = new ArrayList<String>();
        GerenciarBanco db = new GerenciarBanco(this.context);
        String sql = "SELECT id_produto, trim(codigo)|| ' - ' || trim(nome) as produto from produto where tipo_uso=?";

        Cursor cursor = db.getReadableDatabase().rawQuery(sql, new String[]{tipo});
        if(cursor.moveToFirst()){
            do {
                prod.add(cursor.getString(1));
                id_prod.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return prod;
    }

}
