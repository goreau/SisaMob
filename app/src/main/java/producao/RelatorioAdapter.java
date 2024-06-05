package producao;

import java.util.List;

import producao.VcFolha;
import producao.VcImovel;

import com.sucen.sisamob.PrincipalActivity;
import com.sucen.sisamob.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RelatorioAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context ctx = PrincipalActivity.sisamobContext;
    int rec = 0;
    String subtitulo;
    List<RelatorioList> lista;

    public RelatorioAdapter() {
        super();
        this.mInflater = LayoutInflater.from(ctx);

        VcImovel obj2 = new VcImovel(0);
        lista = obj2.getList();

        VcOvitrampa obj5 = new VcOvitrampa(0);
        lista.addAll(obj5.getList());

        VcFolha obj = new VcFolha(0);
        lista.addAll(obj.getList());

        Condicao obj4 = new Condicao(0);
        lista.addAll(obj4.getList());
        rec=1;
        Recipiente obj3 = new Recipiente(0);
        lista.addAll(obj3.getList());

        Alado obj6 = new Alado(0);
        lista.addAll(obj6.getList());

        AladoIm obj7 = new AladoIm(0);
        lista.addAll(obj7.getList());
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getRegistros(int position){
        int tot = 0;
        try {
            if(lista.get(position).getAtividade().contains("Cond.") || lista.get(position).getAtividade().contains("Tipo:")){
                tot=0;
            } else {
                tot = Integer.valueOf(lista.get(position).getRegistros().replace("Total: ", ""));
            }
        } catch (Exception e) {
            tot = 0;
        }
        return tot;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.rel_producao,null);
        }

        final TextView atividade = (TextView) convertView.findViewById(R.id.tvAtividade);
        atividade.setText(lista.get(position).getAtividade());
        final TextView detalhe = (TextView) convertView.findViewById(R.id.tvDetalhe);
        detalhe.setText(lista.get(position).getDetalhe());
        final TextView registros = (TextView) convertView.findViewById(R.id.tvRegistros);
        if (rec==0){
            registros.setText(lista.get(position).getRegistros() + " registros.");
        } else {
            registros.setText(lista.get(position).getRegistros());
        }

        if(position % 2 ==0){
            convertView.setBackgroundColor(Color.parseColor("#F0F0F0"));
        }

        return convertView;
    }
}
