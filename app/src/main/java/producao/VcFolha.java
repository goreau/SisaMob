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
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sucen.sisamob.PrincipalActivity;

public class VcFolha {
    long _id;
    String dt_cadastro;
    int id_municipio;
    int id_quarteirao;
    int id_area_nav;
    int id_atividade;
    int imovel;
    String casa;
    int id_execucao;
    int id_situacao;
    int id_tipo;
    int id_prod_focal;
    int qt_focal;
    int id_prod_peri;
    int qt_peri;
    int id_prod_neb;
    int qt_neb;
    int focal;
    int peri;
    int neb;
    int mecanico;
    int alternativo;
    String agente;
    String latitude;
    String longitude;
    int status;
    MyToast toast;
    Context context;
    public List<String> id_Quart;

    public VcFolha(long _id) {
        super();
        this.context = PrincipalActivity.sisamobContext;
        this._id = _id;
        status = 0;
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

    public int getId_atividade() {
        return id_atividade;
    }

    public void setId_atividade(int id_atividade) {
        this.id_atividade = id_atividade;
    }

    public int getImovel() {
        return imovel;
    }

    public void setImovel(int imovel) {
        this.imovel = imovel;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
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

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
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

    public int getId_area_nav() {
        return id_area_nav;
    }

    public void setId_area_nav(int id_area_nav) {
        this.id_area_nav = id_area_nav;
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

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void popula(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        String selectQuery = "SELECT dt_cadastro, id_municipio, id_quarteirao, id_atividade, imovel, "+
                "id_execucao, id_situacao, id_prod_focal, qt_focal, id_prod_peri, qt_peri, id_prod_neb, " +
                "qt_neb, mecanico, alternativo, latitude, longitude, agente, id_tipo, id_area_nav, " +
                "focal, peri, neb, status, casa FROM vc_folha v where _id=" + this._id;

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            this.dt_cadastro 	= cursor.getString(0);
            this.id_municipio 	= cursor.getInt(1);
            this.id_quarteirao	= cursor.getInt(2);
            this.id_atividade 	= cursor.getInt(3);
            this.imovel 		= cursor.getInt(4);
            this.id_execucao 	= cursor.getInt(5);
            this.id_situacao 	= cursor.getInt(6);
            this.id_prod_focal 	= cursor.getInt(7);
            this.qt_focal 		= cursor.getInt(8);
            this.id_prod_peri 	= cursor.getInt(9);
            this.qt_peri 		= cursor.getInt(10);
            this.id_prod_neb 	= cursor.getInt(11);
            this.qt_neb 		= cursor.getInt(12);
            this.mecanico		= cursor.getInt(13);
            this.alternativo	= cursor.getInt(14);
            this.latitude		= cursor.getString(15);
            this.longitude		= cursor.getString(16);
            this.agente 		= cursor.getString(17);
            this.id_tipo 		= cursor.getInt(18);
            this.id_area_nav	= cursor.getInt(19);
            this.focal          = cursor.getInt(20);
            this.peri           = cursor.getInt(21);
            this.neb            = cursor.getInt(22);
            this.status         = cursor.getInt(23);
            this.casa           = cursor.getString(24);
        }
        db.close();

    }

    public boolean manipula() {
        GerenciarBanco db = new GerenciarBanco(this.context);
        try {
            ContentValues valores = new ContentValues();
            valores.put("dt_cadastro", this.dt_cadastro);
            valores.put("id_atividade", this.id_atividade);
            valores.put("id_municipio", this.id_municipio);
            valores.put("id_quarteirao", this.id_quarteirao);
            valores.put("id_area_nav", this.id_area_nav);
            valores.put("imovel", this.imovel);
            valores.put("id_execucao", this.id_execucao);
            valores.put("id_situacao", this.id_situacao);
            valores.put("id_prod_focal", this.id_prod_focal);
            valores.put("qt_focal", this.qt_focal);
            valores.put("id_prod_peri", this.id_prod_peri);
            valores.put("qt_peri", this.qt_peri);
            valores.put("id_prod_neb", this.id_prod_neb);
            valores.put("qt_neb", this.qt_neb);
            valores.put("mecanico", this.mecanico);
            valores.put("alternativo", this.alternativo);
            valores.put("focal",this.focal);
            valores.put("peri",this.peri);
            valores.put("neb",this.neb);
            if (this._id==0) {
                valores.put("latitude", this.latitude);
                valores.put("longitude", this.longitude);
            }
            valores.put("agente", this.agente);
            valores.put("id_tipo", this.id_tipo);
            valores.put("status", this.status);
            valores.put("casa", this.casa);
            if (this._id > 0) {
                String[] args = { Long.toString(this._id) };
                db.getWritableDatabase().update("vc_folha", valores, "_id=?", args);
            } else {
                this._id = db.getWritableDatabase().insert("vc_folha", null,
                        valores);
            }
            return true;
        } catch (SQLException e) {
            toast = new MyToast(this.context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean delete() {
        GerenciarBanco db = new GerenciarBanco(this.context);
        try {
            String[] args = { Long.toString(this._id) };

            db.getWritableDatabase().delete("vc_folha", "_id=?", args);
            return true;
        } catch (SQLException e) {
            toast = new MyToast(this.context, Toast.LENGTH_SHORT);
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
            Log.d("query",sql);
            db.getWritableDatabase().execSQL(sql);

            sql = "DELETE FROM condicao where id_fk="+"'"+ id +"'";
            Log.d("query",sql);
            db.getWritableDatabase().execSQL(sql);

            sql = "DELETE FROM vc_folha where _id = " + id;
            Log.d("query",sql);
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
        GerenciarBanco db = new GerenciarBanco(this.context);
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT v._id as id, q.numero_quarteirao as texto FROM vc_folha v join quarteirao q on v.id_quarteirao=q.id_quarteirao";

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
    public String composeJSONfromSQLite(String qt) {
        Context context 	= PrincipalActivity.sisamobContext;
        GerenciarBanco db 	= new GerenciarBanco(context);
        Recipiente rec 		= new Recipiente(0);
        Condicao cond		= new Condicao(0);
        String filtQd = qt=="0" ? "" : " and id_quarteirao="+qt;

        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  _id, dt_cadastro, id_municipio, id_quarteirao, id_atividade, imovel, id_execucao, id_situacao, "
                + "id_prod_focal, qt_focal, id_prod_peri, qt_peri, id_prod_neb, qt_neb, mecanico, alternativo, latitude, longitude, "
                + "agente, status, id_tipo, id_area_nav, focal, peri, neb, casa, datetime(dt_insere,'localtime') FROM vc_folha where status = 0"
                + filtQd;

        try {
            Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id_vc_folha", cursor.getString(0));
                    map.put("dt_cadastro", cursor.getString(1));
                    map.put("id_municipio", cursor.getString(2));
                    map.put("id_quarteirao", cursor.getString(3));
                    map.put("id_atividade", cursor.getString(4));
                    map.put("imovel", cursor.getString(5));
                    map.put("id_execucao", cursor.getString(6));
                    map.put("id_situacao", cursor.getString(7));
                    map.put("id_prod_focal", cursor.getString(8));
                    map.put("qt_focal", cursor.getString(9));
                    map.put("id_prod_peri", cursor.getString(10));
                    map.put("qt_peri", cursor.getString(11));
                    map.put("id_prod_neb", cursor.getString(12));
                    map.put("qt_neb", cursor.getString(13));
                    map.put("mecanico", cursor.getString(14));
                    map.put("alternativo", cursor.getString(15));
                    map.put("latitude", cursor.getString(16));
                    map.put("longitude", cursor.getString(17));
                    map.put("agente", cursor.getString(18).replace(" ", "_"));
                    map.put("status", cursor.getString(19));
                    map.put("id_tipo", cursor.getString(20));
                    map.put("id_area_nav", cursor.getString(21));
                    map.put("focal", cursor.getString(22));
                    map.put("peri", cursor.getString(23));
                    map.put("neb", cursor.getString(24));
                    map.put("casa", cursor.getString(25).replace(" ", "_"));
                    map.put("dt_insere", cursor.getString(26));
                    map.put("recipientes", rec.composeJSONfromSQLite2(2, cursor.getString(0)));
                    //map.put("condicao", "[]");//cond.composeJSONfromSQLite2(cursor.getString(0)));
                    wordList.add(map);
                    Log.d("query",map.toString());
                } while (cursor.moveToNext());
            }
        } catch (SQLException e){
            toast = new MyToast(this.context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
        } finally {
            db.close();
        }

        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
        System.out.println(wordList);
        return gson.toJson(wordList);
    }

    public int dbSyncCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM vc_folha where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }

    public int dbCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM vc_folha";
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
        String updateQuery = "Update vc_folha set status = '"+ status +"' where _id="+"'"+ id +"'";
       // Log.d("query",updateQuery);
        db.getWritableDatabase().execSQL(updateQuery);

        updateQuery = "Update recipiente set status = '"+ status +"' where id_fk="+"'"+ id +"'";
      //  Log.d("query",updateQuery);
        db.getWritableDatabase().execSQL(updateQuery);

       // updateQuery = "Update condicao set status = '"+ status +"' where id_fk="+"'"+ id +"'";
      //  Log.d("query",updateQuery);
      //  db.getWritableDatabase().execSQL(updateQuery);

        db.close();
    }

    public int Limpar(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        int id = 0;
        int regs=0;
        String sql = "SELECT _id FROM vc_folha WHERE status=1";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);
        regs = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                sql = "DELETE FROM recipiente where id_fk="+"'"+ id +"'";
             //   Log.d("query",sql);
                db.getWritableDatabase().execSQL(sql);

           //     sql = "DELETE FROM condicao where id_fk="+"'"+ id +"'";
              //  Log.d("query",sql);
          //      db.getWritableDatabase().execSQL(sql);
            } while (cursor.moveToNext());
        }

        sql = "DELETE FROM vc_folha where status = 1";
      //  Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        db.close();
        return regs;
    }

    public int LimparTudo(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        int id = 0;
        int regs=0;
        String sql = "SELECT _id FROM vc_folha";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);
        regs = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                sql = "DELETE FROM recipiente where id_fk="+"'"+ id +"'";
              //  Log.d("query",sql);
                db.getWritableDatabase().execSQL(sql);

            //    sql = "DELETE FROM condicao where id_fk="+"'"+ id +"'";
              //  Log.d("query",sql);
         //       db.getWritableDatabase().execSQL(sql);
            } while (cursor.moveToNext());
        }

        sql = "DELETE FROM vc_folha";
      //  Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        db.close();
        return regs;
    }

    public List<RelatorioList> getList(){
        GerenciarBanco db = new GerenciarBanco(this.context);

        /*String selectQuery = "SELECT a.nome, ' T:(' || sum(case when id_situacao=1 then 1 else 0 end) || ') / F: (' || sum(case when id_situacao=2 then 1 else 0 end) || " +
                "') / D: (' || sum(case when id_situacao=3 then 1 else 0 end) || ')/ Tp: (' || sum(case when id_situacao=4 then 1 else 0 end) || " +
                "') / P: (' || sum(case when id_situacao=5 then 1 else 0 end) || ')/ R: (' || sum(case when id_situacao=6 then 1 else 0 end) ||')' ||"+
                "' \nRecipientes' , 'Total: ' || count(v._id) FROM vc_folha v join atividade a using(id_atividade) group by a.nome";*/

        String selectQuery = "select a.nome, a.pa ||  '\n-> Rec. Lv: ' ||  (case when b.rec is null then 'N/C' else b.rec end), a.tt " +
                "from (SELECT a.nome, ' T:(' || sum(case when id_situacao=1 then 1 else 0 end) || ') / F: (' || sum(case when id_situacao=2 then 1 else 0 end) || " +
                "                 ') / D: (' || sum(case when id_situacao=3 then 1 else 0 end) || ')/ Tp: (' || sum(case when id_situacao=4 then 1 else 0 end) || " +
                "                 ') / P: (' || sum(case when id_situacao=5 then 1 else 0 end) || ')/ R: (' || sum(case when id_situacao=6 then 1 else 0 end) ||')' || " +
                " ' - Total: ' || count(v._id) || ' \n-> Im Lv: ' || count(distinct r.id_fk)  as pa, count(v._id) as tt " +
                " FROM vc_folha v join atividade a using(id_atividade) left join " +
                " (select id_fk, sum(larva) as foco from recipiente where larva>0 group by id_fk) r " +
                " on r.id_fk=v._id " +
                " group by a.nome) a " +
                " left join " +
                " (select nome, group_concat(tipo || ' (' || foco || ')' ) as rec from (" +
                "  select a.nome, tipo, sum(foco) as foco from vc_folha v join atividade a using(id_atividade) left join " +
                "  (select t.nome as tipo, id_fk, sum(larva) as foco from recipiente r join tipo_rec t on t.id_tipo_rec=r.id_tipo where larva>0 " +
                "  group by tipo, id_fk) x on x.id_fk=v._id group by a.nome, tipo) y group by nome) b using(nome)";

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

    public ContentValues[] persiste(){
        int i = 0;
        GerenciarBanco db = new GerenciarBanco(this.context);
        String selectQuery = "SELECT  _id, dt_cadastro, id_municipio, id_quarteirao, id_atividade, imovel, id_execucao, id_situacao, "
                + "id_prod_focal, qt_focal, id_prod_peri, qt_peri, id_prod_neb, qt_neb, mecanico, alternativo, latitude, longitude, "
                + "agente, status, id_tipo, id_area_nav, focal, peri, neb, 0 as casa FROM vc_folha where status = 0";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        ContentValues[] total = new ContentValues[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                map.put("_id", cursor.getString(0));
                map.put("dt_cadastro", cursor.getString(1));
                map.put("id_municipio", cursor.getString(2));
                map.put("id_quarteirao", cursor.getString(3));
                map.put("id_atividade", cursor.getString(4));
                map.put("imovel", cursor.getString(5));
                map.put("id_execucao", cursor.getString(6));
                map.put("id_situacao", cursor.getString(7));
                map.put("id_prod_focal", cursor.getString(8));
                map.put("qt_focal", cursor.getString(9));
                map.put("id_prod_peri", cursor.getString(10));
                map.put("qt_peri", cursor.getString(11));
                map.put("id_prod_neb", cursor.getString(12));
                map.put("qt_neb", cursor.getString(13));
                map.put("mecanico", cursor.getString(14));
                map.put("alternativo", cursor.getString(15));
                map.put("latitude", cursor.getString(16));
                map.put("longitude", cursor.getString(17));
                map.put("agente", cursor.getString(18).replace(" ", "_"));
                map.put("status", cursor.getString(19));
                map.put("id_tipo", cursor.getString(20));
                map.put("id_area_nav", cursor.getString(21));
                map.put("focal", cursor.getString(22));
                map.put("peri", cursor.getString(23));
                map.put("neb", cursor.getString(24));
                map.put("casa", cursor.getString(25));
                total[i++] = map;
            }while (cursor.moveToNext());
        }
        db.close();
        return total;
    }

    public boolean recupera(ContentValues[] dados){
        GerenciarBanco db = new GerenciarBanco(context);
        try{
            for (int i = 0; i<dados.length; i++){
                ContentValues valores = dados[i];
                this._id = db.getWritableDatabase().insert("vc_folha", null, valores);
            }
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.close();
        }
    }

    public List<String> combo(){
        List<String> quart = new ArrayList<String>();
        id_Quart = new ArrayList<String>();
        GerenciarBanco db = new GerenciarBanco(this.context);

        String sql = "SELECT distinct q.id_quarteirao, q.numero_quarteirao as codigo from vc_folha v join quarteirao q using(id_quarteirao) where v.status=0 order by codigo";
        quart.add("Todos");
        id_Quart.add("0");
        Cursor cursor = db.getReadableDatabase().rawQuery(sql, null);
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
