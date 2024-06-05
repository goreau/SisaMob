package producao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utilitarios.GerenciarBanco;
import utilitarios.MyToast;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sucen.sisamob.PrincipalActivity;

public class VcImovel {
    long _id;
    String dt_cadastro;
    int id_imovel;
    int id_execucao;
    int id_situacao;
    int id_prod_focal;
    int qt_focal;
    int id_prod_peri;
    int qt_peri;
    int id_prod_neb;
    int qt_neb;
    int mecanico;
    int alternativo;
    int focal;
    int peri;
    int neb;
    String agente;
    int status;
    MyToast toast;
    Context context;

    public VcImovel(long _id) {
        super();
        this.context = PrincipalActivity.sisamobContext;
        this._id = _id;
        if (_id>0){
            popula();
        }
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getDt_cadastro() {
        return dt_cadastro;
    }

    public void setDt_cadastro(String dt_cadastro) {
        this.dt_cadastro = dt_cadastro;
    }

    public int getId_imovel() {
        return id_imovel;
    }

    public void setId_imovel(int id_imovel) {
        this.id_imovel = id_imovel;
    }

    public int getId_execucao() {
        return id_execucao;
    }

    public void setId_execucao(int id_execucao) {
        this.id_execucao = id_execucao;
    }

    public int getId_situacao() {
        return id_situacao;
    }

    public void setId_situacao(int id_situacao) {
        this.id_situacao = id_situacao;
    }

    public int getId_prod_focal() {
        return id_prod_focal;
    }

    public void setId_prod_focal(int id_prod_focal) {
        this.id_prod_focal = id_prod_focal;
    }

    public int getQt_focal() {
        return qt_focal;
    }

    public void setQt_focal(int qt_focal) {
        this.qt_focal = qt_focal;
    }

    public int getId_prod_peri() {
        return id_prod_peri;
    }

    public void setId_prod_peri(int id_prod_peri) {
        this.id_prod_peri = id_prod_peri;
    }

    public int getQt_peri() {
        return qt_peri;
    }

    public void setQt_peri(int qt_peri) {
        this.qt_peri = qt_peri;
    }

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public int getId_prod_neb() {
        return id_prod_neb;
    }

    public void setId_prod_neb(int id_prod_neb) {
        this.id_prod_neb = id_prod_neb;
    }

    public int getQt_neb() {
        return qt_neb;
    }

    public void setQt_neb(int qt_neb) {
        this.qt_neb = qt_neb;
    }

    public int getMecanico() {
        return mecanico;
    }

    public void setMecanico(int mecanico) {
        this.mecanico = mecanico;
    }

    public int getAlternativo() {
        return alternativo;
    }

    public void setAlternativo(int alternativo) {
        this.alternativo = alternativo;
    }

    public int getFocal() {
        return focal;
    }

    public void setFocal(int focal) {
        this.focal = focal;
    }

    public int getPeri() {
        return peri;
    }

    public void setPeri(int peri) {
        this.peri = peri;
    }

    public int getNeb() {
        return neb;
    }

    public void setNeb(int neb) {
        this.neb = neb;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void popula(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        String selectQuery = "SELECT dt_cadastro, id_imovel, id_execucao, id_situacao,"+
                "id_prod_focal, qt_focal, id_prod_peri, qt_peri, agente, id_prod_neb, qt_neb, " +
                "mecanico, alternativo, focal, peri, neb, status FROM vc_imovel v where _id=" + this._id;

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            this.dt_cadastro 	= cursor.getString(0);
            this.id_imovel 		= cursor.getInt(1);
            this.id_execucao	= cursor.getInt(2);
            this.id_situacao 	= cursor.getInt(3);
            this.id_prod_focal	= cursor.getInt(4);
            this.qt_focal 		= cursor.getInt(5);
            this.id_prod_peri 	= cursor.getInt(6);
            this.qt_peri 		= cursor.getInt(7);
            this.agente 		= cursor.getString(8);
            this.id_prod_neb	= cursor.getInt(9);
            this.qt_neb 		= cursor.getInt(10);
            this.mecanico 		= cursor.getInt(11);
            this.alternativo	= cursor.getInt(12);
            this.focal          = cursor.getInt(13);
            this.peri           = cursor.getInt(14);
            this.neb            = cursor.getInt(15);
            this.status         = cursor.getInt(16);
        }
        db.close();

    }


    public boolean manipula() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        try {
            ContentValues valores = new ContentValues();
            valores.put("dt_cadastro", this.dt_cadastro);
            valores.put("id_imovel", this.id_imovel);
            valores.put("id_execucao", this.id_execucao);
            valores.put("id_situacao", this.id_situacao);
            valores.put("id_prod_focal", this.id_prod_focal);
            valores.put("qt_focal", this.qt_focal);
            valores.put("id_prod_peri", this.id_prod_peri);
            valores.put("qt_peri", this.qt_peri);
            valores.put("agente", this.agente);
            valores.put("status", this.status);
            valores.put("id_prod_neb", this.id_prod_neb);
            valores.put("qt_neb", this.qt_neb);
            valores.put("mecanico", this.mecanico);
            valores.put("alternativo", this.alternativo);
            valores.put("focal",this.focal);
            valores.put("peri",this.peri);
            valores.put("neb",this.neb);
            if (this._id > 0) {
                String[] args = { Long.toString(this._id) };
                db.getWritableDatabase().update("vc_imovel", valores, "_id=?",
                        args);
            } else {
                this._id = db.getWritableDatabase().insert("vc_imovel", null,
                        valores);
            }
            return true;
        } catch (SQLException e) {
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean delete() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        try {
            String[] args = { Long.toString(this._id) };
            db.getWritableDatabase().delete("vc_imovel", "_id=?", args);
            return true;
        } catch (SQLException e) {
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean deleteCascade(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        long id = this._id;
        try{
            String sql = "DELETE FROM recipiente where id_fk="+"'"+ id +"'";
          //  Log.d("query",sql);
            db.getWritableDatabase().execSQL(sql);

            sql = "DELETE FROM vc_imovel where _id = " + id;
          //  Log.d("query",sql);
            db.getWritableDatabase().execSQL(sql);
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show("Imóvel excluído!");
            return true;
        } catch(SQLException e){
            toast = new MyToast(this.context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public ArrayList<HashMap<String, String>> getAllImoveis() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT v._id as id, i.endereco as texto FROM vc_imovel v join imovel i on v.id_imovel=i.id_imovel";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("texto", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return wordList;
    }

    /**
     * Compose JSON out of SQLite records
     *
     * @return
     */
    public String composeJSONfromSQLite() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        Recipiente rec = new Recipiente(0);

        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  _id, dt_cadastro, id_imovel, id_execucao, id_situacao, "
                + "id_prod_focal, qt_focal, id_prod_peri, qt_peri, agente, status, id_prod_neb, "
                + "qt_neb, mecanico, alternativo, focal, peri, neb, datetime(dt_insere,'localtime') FROM vc_imovel where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id_vc_imovel", cursor.getString(0));
                map.put("dt_cadastro", cursor.getString(1));
                map.put("id_imovel", cursor.getString(2));
                map.put("id_execucao", cursor.getString(3));
                map.put("id_situacao", cursor.getString(4));
                map.put("id_prod_focal", cursor.getString(5));
                map.put("qt_focal", cursor.getString(6));
                map.put("id_prod_peri", cursor.getString(7));
                map.put("qt_peri", cursor.getString(8));
                map.put("agente", cursor.getString(9).replace(" ","_"));
                map.put("status", cursor.getString(10));
                map.put("id_prod_neb", cursor.getString(11));
                map.put("qt_neb", cursor.getString(12));
                map.put("mecanico", cursor.getString(13));
                map.put("alternativo", cursor.getString(14));
                map.put("focal", cursor.getString(15));
                map.put("peri", cursor.getString(16));
                map.put("neb", cursor.getString(17));
                map.put("dt_insere", cursor.getString(18));
                map.put("recipientes", rec.composeJSONfromSQLite2(1, cursor.getString(0)));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
       // System.out.println(wordList);
        return gson.toJson(wordList);
    }

    public int dbSyncCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM vc_imovel where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }

    public int dbCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM vc_imovel";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }
    /**
     * Update Sync status against each User ID
     * @param id
     * @param status
     */
    public void atualizaStatus(String id, String status){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        String updateQuery = "Update vc_imovel set status = '"+ status +"' where _id="+"'"+ id +"'";
      //  Log.d("query",updateQuery);
        db.getWritableDatabase().execSQL(updateQuery);

        updateQuery = "Update recipiente set status = '"+ status +"' where id_fk="+"'"+ id +"'";
     //   Log.d("query",updateQuery);
        db.getWritableDatabase().execSQL(updateQuery);
        db.close();
    }

    public int Limpar(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int id = 0;
        int regs = 0;
        String sql = "SELECT _id FROM vc_imovel WHERE status=1";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);
        regs = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                sql = "DELETE FROM recipiente where id_fk="+"'"+ id +"'";
             //   Log.d("query",sql);
                db.getWritableDatabase().execSQL(sql);

                sql = "DELETE FROM condicao where id_fk="+"'"+ id +"'";
              //  Log.d("query",sql);
                db.getWritableDatabase().execSQL(sql);
            } while (cursor.moveToNext());
        }

        sql = "DELETE FROM vc_imovel where status = 1";
     //   Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        db.close();
        return regs;
    }

    public int LimparTudo(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int id = 0;
        int regs = 0;
        String sql = "SELECT _id FROM vc_imovel";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);
        regs = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                sql = "DELETE FROM recipiente where id_fk="+"'"+ id +"'";
              //  Log.d("query",sql);
                db.getWritableDatabase().execSQL(sql);

                sql = "DELETE FROM condicao where id_fk="+"'"+ id +"'";
              //  Log.d("query",sql);
                db.getWritableDatabase().execSQL(sql);
            } while (cursor.moveToNext());
        }

        sql = "DELETE FROM vc_imovel";
      //  Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        db.close();
        return regs;
    }

    public List<RelatorioList> getList(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);

        /*String selectQuery = "SELECT a.nome, ' T:(' || sum(case when id_situacao=1 then 1 else 0 end) || ') / F: (' || sum(case when id_situacao=2 then 1 else 0 end) || ') / R: ('" +
                "|| sum(case when id_situacao=3 then 1 else 0 end) || ')', 'Total: ' || count(v._id) FROM vc_imovel v join imovel i using(id_imovel) join " +
                "atividade a using(id_atividade) group by a.nome";*/

        String selectQuery = "select a.nome, a.pa ||  ' \n-> Rec. Lv: ' ||  b.rec, a.tt from  (" +
                "SELECT a.nome, ' T:(' || sum(case when id_situacao=1 then 1 else 0 end) || ') / F: (' || sum(case when id_situacao=2 then 1 else 0 end) ||') / R: (' || sum(case when id_situacao=3 then 1 else 0 end) || ')' || ' - Total: ' || count(v._id) || ' \n-> Im Lv: ' || count(distinct r.id_fk)  as pa, count(v._id) as tt  FROM vc_imovel v join imovel i using(id_imovel) join atividade a using(id_atividade) left join  (select id_fk, sum(larva) as foco from recipiente  group by id_fk) r  on r.id_fk=v._id  group by a.nome) a " +
                "left join   (" +
                "select nome, group_concat(tipo || ' (' || foco || ')' ) as rec from (  select a.nome, tipo, sum(foco) as foco from vc_imovel v join imovel i using(id_imovel) join atividade a using(id_atividade) left join   (select t.nome as tipo, id_fk, sum(larva) as foco from recipiente r join tipo_rec t on t.id_tipo_rec=r.id_tipo where larva>0 group by tipo, id_fk) x on x.id_fk=v._id group by a.nome, tipo) y) b" +
                " using(nome) group by a.nome";

        Cursor cursor = db.getReadableDatabase().rawQuery(selectQuery, null);
        List<RelatorioList> lista = new ArrayList<RelatorioList>();

        if (cursor.moveToFirst()) {
            do {
                RelatorioList list = new RelatorioList(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                lista.add(list);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public ContentValues[] persiste() {
        int i = 0;
        GerenciarBanco db = new GerenciarBanco(context);
        String selectQuery = "SELECT  _id, dt_cadastro, id_imovel, id_execucao, id_situacao, "
                + "id_prod_focal, qt_focal, id_prod_peri, qt_peri, agente, status, id_prod_neb, "
                + "qt_neb, mecanico, alternativo, focal, peri, neb FROM vc_imovel where status = 0";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        ContentValues[] total = new ContentValues[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                map.put("_id", cursor.getString(0));
                map.put("dt_cadastro", cursor.getString(1));
                map.put("id_imovel", cursor.getString(2));
                map.put("id_execucao", cursor.getString(3));
                map.put("id_situacao", cursor.getString(4));
                map.put("id_prod_focal", cursor.getString(5));
                map.put("qt_focal", cursor.getString(6));
                map.put("id_prod_peri", cursor.getString(7));
                map.put("qt_peri", cursor.getString(8));
                map.put("agente", cursor.getString(9).replace(" ","_"));
                map.put("status", cursor.getString(10));
                map.put("id_prod_neb", cursor.getString(11));
                map.put("qt_neb", cursor.getString(12));
                map.put("mecanico", cursor.getString(13));
                map.put("alternativo", cursor.getString(14));
                map.put("focal", cursor.getString(15));
                map.put("peri", cursor.getString(16));
                map.put("neb", cursor.getString(17));
                total[i++] = map;
            } while (cursor.moveToNext());
        }
        db.close();
        return total;
    }

    public boolean recupera(ContentValues[] dados){
        GerenciarBanco db = new GerenciarBanco(context);
        try{
            for (int i = 0; i<dados.length; i++){
                ContentValues valores = dados[i];
                this._id = db.getWritableDatabase().insert("vc_imovel", null, valores);
            }
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.close();
        }
    }

}
