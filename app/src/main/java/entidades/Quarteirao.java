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

public class Quarteirao {
    private int id_quarteirao;
    private int id_censitario;
    private String numero_quarteirao;
    private String sub_numero;
    private Context context;
    public List<String> id_Quart;
    MyToast toast;

    public Quarteirao() {
        context = PrincipalActivity.getSisamobContext();
    }

    public Quarteirao(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }

    public Quarteirao(int id_quarteirao, int id_censitario,
                      String numero_quarteirao, String sub_numero, Context context) {
        super();
        this.id_quarteirao = id_quarteirao;
        this.id_censitario = id_censitario;
        this.numero_quarteirao = numero_quarteirao;
        this.sub_numero = sub_numero;
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }

    public int getId_quarteirao() {
        return id_quarteirao;
    }

    public void setId_quarteirao(int id_quarteirao) {
        this.id_quarteirao = id_quarteirao;
    }

    public int getId_censitario() {
        return id_censitario;
    }

    public void setId_censitario(int id_censitario) {
        this.id_censitario = id_censitario;
    }

    public String getNumero_quarteirao() {
        return numero_quarteirao;
    }

    public void setNumero_quarteirao(String numero_quarteirao) {
        this.numero_quarteirao = numero_quarteirao;
    }

    public String getSub_numero() {
        return sub_numero;
    }

    public void setSub_numero(String sub_numero) {
        this.sub_numero = sub_numero;
    }

    public void limpar(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            db.getWritableDatabase().delete("quarteirao", null, null);
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
            db.getWritableDatabase().insert("quarteirao", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public List<String> combo(String id, int ativ){
        List<String> quart = new ArrayList<String>();
        id_Quart = new ArrayList<String>();
        GerenciarBanco db = new GerenciarBanco(this.context);
        String numQuart = ((ativ==8 || ativ==13) ? "(trim(numero_quarteirao)|| ' - ' || trim(sub_numero))" : "trim(numero_quarteirao)");
        String sql = "SELECT id_quarteirao, " + numQuart + " as codigo from quarteirao where id_censitario=? order by codigo";

        Cursor cursor = db.getReadableDatabase().rawQuery(sql, new String[]{id});
        if(cursor.moveToFirst()){
            do {
                quart.add(cursor.getString(1));
                id_Quart.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return quart;
    }
}
