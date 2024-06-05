package producao;

import java.util.List;

import com.sucen.sisamob.PrincipalActivity;
import com.sucen.sisamob.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EditaRecAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context ctx = PrincipalActivity.sisamobContext;
    int rec = 0;
    String subtitulo;
    List<RecipienteList> lista;

    public EditaRecAdapter(Long id, int tabela) {
        super();
        this.mInflater = LayoutInflater.from(ctx);
        Recipiente rec = new Recipiente(0);
        lista = rec.getEdicao(tabela, id);
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.ed_recipiente,null);
        }

        final TextView id = (TextView) convertView.findViewById(R.id.id);
        id.setText(String.valueOf(lista.get(position).getId()));
        final TextView tipo = (TextView) convertView.findViewById(R.id.tipo);
        tipo.setText(lista.get(position).getTipo());
        final TextView amostra = (TextView) convertView.findViewById(R.id.amostra);
        amostra.setText(lista.get(position).getAmostra());
        return convertView;
    }

}

