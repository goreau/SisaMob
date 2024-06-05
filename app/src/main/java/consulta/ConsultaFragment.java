package consulta;

import utilitarios.GerenciarBanco;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.sucen.sisamob.PrincipalActivity;
import com.sucen.sisamob.R;

public class ConsultaFragment extends Fragment{
    SparseArray<Group> groups = new SparseArray<Group>();
    Context context;
    GerenciarBanco db;
    int j=0;

    private OnFragmentInteractionListener mListener;

    public ConsultaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = PrincipalActivity.sisamobContext;
        db = new GerenciarBanco(context);

        // Creating view correspoding to the fragment
        View v = inflater.inflate(R.layout.consulta_main, container, false);

        // Updating the action bar title
//        getActivity().getActionBar().setTitle("Consulta");
        createLinha();
        createFolha();
        createOvitrampa();
        createAlado();
        createAladoIm();
        if (j==0){
            groups.append(j, new Group("Nenhum registro cadastrado."));
        }
        ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.listView);
        ConsultaListAdapter adapter = new ConsultaListAdapter(getActivity(), groups);
        listView.setAdapter(adapter);
        return v;
    }

    public void createFolha() {
        String sql="", strGrupo="", strLinha="", oldGrupo="";
        Cursor cursor;
        Group group = null;

        sql = "SELECT  v.dt_cadastro, m.nome as mun, (trim(q.numero_quarteirao)|| ' - ' || trim(q.sub_numero)) as quart," +
                "a.nome || '-' || (case id_tipo when 1 then 'Rot' when 2 then 'Pend' else 'Dem' end) as ativ, " +
                "(case id_situacao when 1 then 'T' when 2 then 'F' when 3 then 'D' when 4 then 'Tp' when 5 then 'P' else 'R' end) as sit, v.status, v._id, v.id_atividade, r.amostra, imovel, casa " +
                "FROM vc_folha v join municipio m using(id_municipio) join quarteirao q using(id_quarteirao) " +
                "join atividade a using(id_atividade) left join " +
                "(select id_fk, count(*) as amostra from recipiente where tabela=2 and amostra <>'' group by id_fk) r on r.id_fk=v._id";
        cursor = db.getWritableDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String am = "Am: " + (cursor.getString(8)==null ? "-" : cursor.getString(8));
                strGrupo = cursor.getString(0).trim()+"\n-"+cursor.getString(7).trim()+"-"+cursor.getString(3).trim()+"\n-"+cursor.getString(1).trim()+"- Quadra: "+cursor.getString(2).trim();
                if (oldGrupo == ""){
                    oldGrupo = strGrupo;
                    strLinha = "Im: " + cursor.getString(9)+" - Nº Casa: " + cursor.getString(10) +" ("+cursor.getString(4)+") - " + am;
                    group = new Group(strGrupo);
                    group.setStatus(cursor.getInt(5));
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                } else if (strGrupo.equals(oldGrupo)){
                    strLinha = "Im: "  + cursor.getString(9)+" - Nº Casa: " + cursor.getString(10) +" ("+cursor.getString(4)+") - " + am;
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                } else {
                    groups.append(j++, group);
                    oldGrupo = strGrupo;
                    strLinha = "Im: " + cursor.getString(9)+" - Nº Casa: " + cursor.getString(10) +" ("+cursor.getString(4)+") - " + am;
                    group = new Group(strGrupo);
                    group.setStatus(cursor.getInt(5));
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                }
            } while (cursor.moveToNext());
            groups.append(j++, group);
        }
        db.close();
    }

    public void createLinha() {
        String sql="", strGrupo="", strLinha="", oldGrupo="";
        Cursor cursor;
        Group group = null;

        sql = "SELECT  v.dt_cadastro, m.nome as mun, i.numero_imovel, i.endereco, a.nome as ativ, v.status, v._id, " +
                "i.id_atividade, r.amostra FROM vc_imovel v join imovel i using(id_imovel) join municipio m using(id_municipio) " +
                "join atividade a using(id_atividade) left join " +
                "(select id_fk, count(*) as amostra from recipiente where tabela=1 and amostra <>'' group by id_fk) r on r.id_fk=v._id";
        cursor = db.getWritableDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String am = "Am: " + (cursor.getString(8)==null ? "-" : cursor.getString(8));
                strGrupo = cursor.getString(0).trim()+"\n-"+cursor.getString(7).trim()+"-"+cursor.getString(4).trim()+"\n-"+cursor.getString(1).trim();
                if (oldGrupo == ""){
                    oldGrupo = strGrupo;
                    strLinha = "Cad: " + cursor.getString(2).trim()+" ("+cursor.getString(3).trim()+") - " + am;
                    group = new Group(strGrupo);
                    group.setStatus(cursor.getInt(5));
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                } else if (strGrupo.equals(oldGrupo)){
                    strLinha = "Cad: " + cursor.getString(2).trim()+" ("+cursor.getString(3).trim()+") - " + am;
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                } else {
                    groups.append(j++, group);
                    //strGrupo = cursor.getString(0)+"-"+cursor.getString(3)+"-"+cursor.getString(1)+"- Quarteirao: "+cursor.getString(2);
                    oldGrupo = strGrupo;
                    strLinha = "Cad: " + cursor.getString(2).trim()+" ("+cursor.getString(3).trim()+") - " + am;
                    group = new Group(strGrupo);
                    group.setStatus(cursor.getInt(5));
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                }
            } while (cursor.moveToNext());
            groups.append(j++, group);
        }
        db.close();
    }

    public void createAlado() {
        String sql="", strGrupo="", strLinha="", oldGrupo="";
        Cursor cursor;
        Group group = null;

        sql = "SELECT  v.dt_cadastro, m.nome as mun, (trim(q.numero_quarteirao)|| ' - ' || trim(q.sub_numero)) as quart," +
                "a.nome as ativ, (case id_situacao when 1 then 'T' else 'P' end) as sit, v.status, v._id, v.id_atividade, imovel, am_larva, am_intra, am_peri " +
                "FROM alado v join municipio m using(id_municipio) join quarteirao q using(id_quarteirao) " +
                "join atividade a using(id_atividade)";
        cursor = db.getWritableDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String am = "Am: Larva(" + (cursor.getString(9)==null ? "-" : cursor.getString(9))+") / Intra("
                        + (cursor.getString(10)==null ? "-" : cursor.getString(10))+") / Peri("
                        + (cursor.getString(11)==null ? "-" : cursor.getString(11))+")";
                strGrupo = cursor.getString(0).trim()+"\n-"+cursor.getString(7).trim()+"-"+cursor.getString(3).trim()+"\n-"+cursor.getString(1).trim()+"- Quadra: "+cursor.getString(2).trim();
                if (oldGrupo == ""){
                    oldGrupo = strGrupo;
                    strLinha = "Im: " + cursor.getString(8)+" ("+cursor.getString(4)+") - " + am;
                    group = new Group(strGrupo);
                    group.setStatus(cursor.getInt(5));
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                } else if (strGrupo.equals(oldGrupo)){
                    strLinha = "Im: "  + cursor.getString(8)+" ("+cursor.getString(4)+") - " + am;
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                } else {
                    groups.append(j++, group);
                    oldGrupo = strGrupo;
                    strLinha = "Im: " + cursor.getString(8)+" ("+cursor.getString(4)+") - " + am;
                    group = new Group(strGrupo);
                    group.setStatus(cursor.getInt(5));
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                }
            } while (cursor.moveToNext());
            groups.append(j++, group);
        }
        db.close();
    }

    public void createAladoIm() {
        String sql="", strGrupo="", strLinha="", oldGrupo="";
        Cursor cursor;
        Group group = null;

        sql = "SELECT  v.dt_cadastro, m.nome as mun, i.numero_imovel, i.endereco, a.nome as ativ, v.status, v._id, " +
                "v.id_atividade, am_larva, am_intra, am_peri, v.id_sub_ativ  FROM alado_im v join imovel i using(id_imovel) join municipio m using(id_municipio) " +
                "join atividade a on(a.id_atividade=v.id_sub_ativ)";
        cursor = db.getWritableDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String am = "Am: Larva(" + (cursor.getString(8)==null ? "-" : cursor.getString(8))+") / Intra("
                        + (cursor.getString(9)==null ? "-" : cursor.getString(9))+") / Peri("
                        + (cursor.getString(10)==null ? "-" : cursor.getString(10))+")";
                strGrupo = cursor.getString(0).trim()+"\n-"+cursor.getString(7).trim()+"- Capt Alado("+cursor.getString(11).trim()+")\n-"+cursor.getString(1).trim();
                if (oldGrupo == ""){
                    oldGrupo = strGrupo;
                    strLinha = "Cad: " + cursor.getString(2).trim()+" ("+cursor.getString(3).trim()+") - " + am;
                    group = new Group(strGrupo);
                    group.setStatus(cursor.getInt(5));
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                } else if (strGrupo.equals(oldGrupo)){
                    strLinha = "Cad: " + cursor.getString(2).trim()+" ("+cursor.getString(3).trim()+") - " + am;
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                } else {
                    groups.append(j++, group);
                    //strGrupo = cursor.getString(0)+"-"+cursor.getString(3)+"-"+cursor.getString(1)+"- Quarteirao: "+cursor.getString(2);
                    oldGrupo = strGrupo;
                    strLinha = "Cad: " + cursor.getString(2).trim()+" ("+cursor.getString(3).trim()+") - " + am;
                    group = new Group(strGrupo);
                    group.setStatus(cursor.getInt(5));
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                }
            } while (cursor.moveToNext());
            groups.append(j++, group);
        }
        db.close();
    }

    public void createOvitrampa() {
        String sql="", strGrupo="", strLinha="", oldGrupo="";
        Cursor cursor;
        Group group = null;

        sql = "SELECT  (case when v.dt_retira='' then '01-01-1900' else v.dt_retira end), m.nome as mun, i.cadastro, i.endereco, 'Ovitrampa' as ativ, v.status, v._id, 4 as id_ativ " +
                "FROM vc_ovitrampa v join ovitrampa i using(id_ovitrampa) join municipio m using(id_municipio)";
        cursor = db.getWritableDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                strGrupo = cursor.getString(0).trim()+"-"+cursor.getString(7).trim()+"-"+cursor.getString(4).trim()+"-"+cursor.getString(1).trim();
                if (oldGrupo == ""){
                    oldGrupo = strGrupo;
                    strLinha = "Cad: " + cursor.getString(2)+" ("+cursor.getString(3)+")";
                    group = new Group(strGrupo);
                    group.setStatus(cursor.getInt(5));
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                } else if (strGrupo.equals(oldGrupo)){
                    strLinha = "Cad: " + cursor.getString(2)+" ("+cursor.getString(3)+")";
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                } else {
                    groups.append(j++, group);
                    //strGrupo = cursor.getString(0)+"-"+cursor.getString(3)+"-"+cursor.getString(1)+"- Quarteirao: "+cursor.getString(2);
                    oldGrupo = strGrupo;
                    strLinha = "Cad: " + cursor.getString(2)+" ("+cursor.getString(3)+")";
                    group = new Group(strGrupo);
                    group.setStatus(cursor.getInt(5));
                    group.children.add(new Children((long) cursor.getInt(6),strLinha));
                }
            } while (cursor.moveToNext());
            groups.append(j++, group);
        }
        db.close();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
