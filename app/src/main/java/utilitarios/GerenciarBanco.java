package utilitarios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class GerenciarBanco extends SQLiteOpenHelper {
    HashMap<String,ContentValues[]> registros = new HashMap<String,ContentValues[]>();
    private static final String NOME_BANCO = "sisamob.db";
    private static final int VERSAO_SCHEMA = 15;
    private static final String[] sql = {
            "CREATE TABLE municipio(id_municipio INTEGER, nome TEXT, codigo TEXT)",
            "CREATE TABLE area_nav(id_area_nav INTEGER, id_municipio INTEGER, nome TEXT)",
            "CREATE TABLE area(id_area INTEGER, id_municipio INTEGER, codigo TEXT)",
            "CREATE TABLE censitario(id_censitario INTEGER, id_area INTEGER, codigo TEXT)",
            "CREATE TABLE quarteirao(id_quarteirao INTEGER, id_censitario INTEGER, numero_quarteirao TEXT, sub_numero TEXT)",
            "CREATE TABLE quart_nav(id_area_nav INTEGER, id_quarteirao INTEGER, id_censitario INTEGER, numero_quarteirao TEXT, sub_numero TEXT)",
            "CREATE TABLE imovel(id_imovel INTEGER, id_municipio INTEGER, id_quarteirao INTEGER, numero_imovel TEXT, endereco TEXT, id_atividade INTEGER)",
            "CREATE TABLE ovitrampa(id_ovitrampa INTEGER, id_municipio INTEGER, id_quarteirao INTEGER, cadastro TEXT, endereco TEXT)",
            "CREATE TABLE grupo_rec(id_grupo_rec INTEGER, codigo TEXT, nome TEXT)",
            "CREATE TABLE tipo_rec(id_tipo_rec INTEGER, id_grupo_rec INTEGER, codigo INTEGER, nome TEXT)",
            "CREATE TABLE atividade(id_atividade INTEGER, nome TEXT, grupo INTEGER)",
            "CREATE TABLE produto(id_produto INTEGER, codigo TEXT, nome TEXT, tipo_uso INTEGER)",
            //tabelas de atividade
            "CREATE TABLE vc_imovel(_id INTEGER PRIMARY KEY AUTOINCREMENT, dt_cadastro TEXT, id_imovel INTEGER, id_execucao INTEGER, id_situacao INTEGER,"+
                    "id_prod_focal INTEGER, qt_focal integer, id_prod_peri INTEGER, qt_peri INTEGER, id_prod_neb INTEGER, qt_neb INTEGER, mecanico INTEGER, " +
                    "alternativo INTEGER, focal INTEGER, peri INTEGER, neb INTEGER, agente TEXT, status INTEGER, dt_insere DATETIME DEFAULT CURRENT_TIMESTAMP)",
            "CREATE TABLE vc_folha(_id INTEGER PRIMARY KEY AUTOINCREMENT, dt_cadastro TEXT, id_municipio INTEGER, id_quarteirao INTEGER, id_area_nav INTEGER, id_atividade INTEGER, imovel INTEGER, "+
                    "id_execucao INTEGER, id_situacao INTEGER, id_tipo INTEGER, id_prod_focal INTEGER, qt_focal integer, id_prod_peri INTEGER, qt_peri INTEGER, id_prod_neb INTEGER, qt_neb INTEGER, " +
                    "mecanico INTEGER, alternativo INTEGER, focal INTEGER, peri INTEGER, neb INTEGER, agente TEXT, latitude TEXT, longitude TEXT, status INTEGER, casa TEXT, dt_insere DATETIME DEFAULT CURRENT_TIMESTAMP)",
            "CREATE TABLE vc_ovitrampa(_id INTEGER PRIMARY KEY AUTOINCREMENT, id_ovitrampa INTEGER, id_execucao INTEGER, dt_instala TEXT, dt_retira TEXT, "+
                    "peri_intra INTEGER, obs INTEGER, agente TEXT, status INTEGER, dt_insere DATETIME DEFAULT CURRENT_TIMESTAMP)",
            "CREATE TABLE recipiente(_id INTEGER PRIMARY KEY AUTOINCREMENT, id_grupo INTEGER, id_tipo INTEGER, existente INTEGER, agua INTEGER, larva INTEGER,"+
                    "amostra TEXT, tabela INTEGER, id_fk INTEGER, status INTEGER)",
            "CREATE TABLE condicao(_id INTEGER PRIMARY KEY AUTOINCREMENT, id_fk INTEGER, cond_casa INTEGER, cond_quintal INTEGER, cond_sombra INTEGER, pavimento INTEGER, galinha INTEGER, "+
                    "cao INTEGER, outros INTEGER, status INTEGER)",
            "CREATE TABLE coordenadas(_id INTEGER PRIMARY KEY AUTOINCREMENT, id_imovel INTEGER, latitude TEXT, longitude TEXT, status INTEGER)",
            "CREATE TABLE alado(_id INTEGER PRIMARY KEY AUTOINCREMENT, dt_cadastro TEXT, id_municipio INTEGER, id_quarteirao INTEGER, id_atividade INTEGER, imovel INTEGER, casa TEXT, agente TEXT, "+
                    "id_situacao INTEGER, umidade REAL, temperatura REAL, moradores INTEGER, rec_larva INTEGER, am_larva TEXT, am_intra TEXT, am_peri TEXT, latitude TEXT, longitude TEXT, id_execucao INTEGER, status INTEGER, dt_insere DATETIME DEFAULT CURRENT_TIMESTAMP)",
            "CREATE TABLE alado_im(_id INTEGER PRIMARY KEY AUTOINCREMENT, dt_cadastro TEXT, id_municipio INTEGER, id_atividade INTEGER, id_imovel INTEGER, id_sub_ativ INTEGER, agente TEXT, "+
                    "id_situacao INTEGER, umidade REAL, temperatura REAL, moradores INTEGER, rec_larva INTEGER, am_larva TEXT, am_intra TEXT, am_peri TEXT, latitude TEXT, longitude TEXT, id_execucao INTEGER, status INTEGER, dt_insere DATETIME DEFAULT CURRENT_TIMESTAMP)"

    };

    private static final String[] tabelas = {"municipio","area_nav","area","censitario","quarteirao","quart_nav","imovel","ovitrampa","grupo_rec","tipo_rec","atividade","produto","vc_imovel", "vc_folha", "vc_ovitrampa", "recipiente", "condicao","coordenadas","alado","alado_im"};

    //Construtor
    public GerenciarBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO_SCHEMA);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < sql.length; i++){
            db.execSQL(sql[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVers, int newVrs) {
        persiste(db);
        for (int i = 0; i < tabelas.length; i++){
            db.execSQL("DROP TABLE IF EXISTS " + tabelas[i]);
        }
        onCreate(db);
        recupera(db);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    public void persiste(SQLiteDatabase db){
        //fornecer valor padrão para o campo alterado
        String[] sqlPersiste = {
                "SELECT id_municipio, nome, codigo FROM municipio",
                "SELECT id_area_nav, id_municipio, nome FROM area_nav",
                "SELECT id_area, id_municipio, codigo FROM area",
                "SELECT id_censitario, id_area, codigo FROM censitario",
                "SELECT id_quarteirao, id_censitario, numero_quarteirao, sub_numero FROM quarteirao",
                "SELECT id_area_nav, id_quarteirao, id_censitario, numero_quarteirao, sub_numero FROM quart_nav",
                "SELECT id_imovel, id_municipio, id_quarteirao, numero_imovel, endereco, id_atividade FROM imovel",
                "SELECT id_ovitrampa, id_municipio, id_quarteirao, cadastro, endereco FROM ovitrampa",
                "SELECT id_grupo_rec, codigo, nome FROM grupo_rec",
                "SELECT id_tipo_rec, id_grupo_rec, codigo, nome FROM tipo_rec",
                "SELECT id_atividade, nome, grupo FROM atividade",
                "SELECT id_produto, codigo, nome, tipo_uso FROM produto",
                "SELECT _id, dt_cadastro, id_imovel, id_execucao, id_situacao, id_prod_focal, qt_focal, id_prod_peri, qt_peri, id_prod_neb, qt_neb, mecanico, " +
                        "alternativo, focal, peri, neb, agente, status FROM vc_imovel",
                "SELECT _id, dt_cadastro, id_municipio, id_quarteirao, id_area_nav, id_atividade, imovel, id_execucao, id_situacao, id_tipo, id_prod_focal, qt_focal, id_prod_peri, qt_peri, id_prod_neb, qt_neb, " +
                        "mecanico, alternativo, focal, peri, neb, agente, latitude, longitude, status, casa  FROM vc_folha",
                "SELECT _id, id_ovitrampa, id_execucao, dt_instala, dt_retira, peri_intra, obs, agente, status  FROM vc_ovitrampa",
                "SELECT _id, id_grupo, id_tipo, existente, agua, larva, amostra, tabela, id_fk, status FROM recipiente",
                "SELECT _id, id_fk, cond_casa, cond_quintal, cond_sombra, pavimento, galinha, cao, outros, status  FROM condicao",
                "SELECT _id, id_imovel, latitude, longitude, status FROM coordenadas",
                "SELECT _id, dt_cadastro, id_municipio, id_quarteirao, id_atividade, imovel, casa, id_situacao, umidade, temperatura, moradores, rec_larva, am_larva, " +
                        "am_intra, am_peri, latitude, longitude, status, dt_insere, agente FROM alado",
                "SELECT _id, dt_cadastro, id_municipio, id_atividade, id_imovel, id_sub_ativ, id_situacao, umidade, temperatura, moradores, rec_larva, am_larva, " +
                        "am_intra, am_peri, latitude, longitude, status, dt_insere, agente FROM alado_im"};

        for (int i = 0; i < sqlPersiste.length; i++) {
            int x = 0;
            if (checaTabela(db,tabelas[i])==false){
                continue;
            }
            Cursor cursor = db.rawQuery(sqlPersiste[i], null);
            ContentValues[] total = new ContentValues[cursor.getCount()];
            if (cursor.moveToFirst()) {
                do {
                    ContentValues map = new ContentValues();
                    for (int j = 0; j < cursor.getColumnCount(); j++) {
                        map.put(cursor.getColumnName(j), cursor.getString(j));
                    }
                    total[x++] = map;
                } while (cursor.moveToNext());
                registros.put(tabelas[i], total);
            }
        }
    }

    private boolean checaTabela(SQLiteDatabase db,String tab){
        String sql = "SELECT * FROM sqlite_master WHERE name ='"+tab+"' and type='table'";
        Cursor crs = db.rawQuery(sql,null);
        return crs.getCount()>0;
    }

    public void recupera(SQLiteDatabase db){
        ContentValues[] dados;
        for (int x = 0; x < tabelas.length; x++) {
            dados = registros.get(tabelas[x]);
            try {
                for (int i = 0; i < dados.length; i++) {
                    ContentValues valores = dados[i];
                    db.insert(tabelas[x], null, valores);
                }
            } catch (SQLException e) {
               // Log.i("Exception",tabelas[x]);
                continue;
            } catch (NullPointerException nex){
              //  Log.i("Null",tabelas[x]);
                continue;
            }
        }
    }
}
