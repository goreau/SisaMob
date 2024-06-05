package consulta;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.sucen.sisamob.AladoFragment;
import com.sucen.sisamob.AladoImFragment;
import com.sucen.sisamob.ImovelCadFragment;
import com.sucen.sisamob.ImovelFolhaFragment;
import com.sucen.sisamob.OvitrampaFragment;
import com.sucen.sisamob.R;

import utilitarios.Storage;

public class ConsultaListAdapter extends BaseExpandableListAdapter {
    private final SparseArray<Group> groups;
    public LayoutInflater inflater;
    public Activity activity;
    int statusChild;

    public ConsultaListAdapter(Activity act, SparseArray<Group> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition).getTexto();
    }

    public String getNome(int groupPosition){
        String[] matriz = groups.get(groupPosition).string.split("-");
        return matriz[3];
    }

    private int getAtiv(int groupPosition){
        String[] matriz = groups.get(groupPosition).string.split("-");
        return Integer.parseInt(matriz[3]);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition).getId();
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String children = (String) getChild(groupPosition, childPosition);
        final Long id = getChildId(groupPosition, childPosition);
        final int papai = getAtiv(groupPosition);

        TextView text = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.consulta_detalhe, null);
        }
        text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(children);
        if (statusChild == 0){
            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.editavel, 0);
        } else {
            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.enviado, 0);
        }
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaEdicao(papai, id);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.consulta_grupo, null);
        }
        Group group = (Group) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.string);
        ((CheckedTextView) convertView).setChecked(isExpanded);
        statusChild = group.status;
        if (group.status == 0){
            ((CheckedTextView) convertView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.editavel,0,0,0);
        } else {
            ((CheckedTextView) convertView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.enviado,0,0,0);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private void chamaEdicao(int tab,Long id){
        Fragment frag;
        switch(tab){
            case 1: case 2: case 3: case 12:
                frag = new ImovelCadFragment();
                Storage.insere("atividade",String.valueOf(tab));
                break;
            case 9: case 10:
                frag = new AladoFragment();
                break;
            case 99:
                frag = new AladoImFragment();
                break;
            case 4:
                frag = new OvitrampaFragment();
                break;
            default:
                frag = new ImovelFolhaFragment();
                break;
        }
        // Creating a Bundle object
        Bundle data = new Bundle();

        // Setting the index of the currently selected item of mDrawerList
        data.putLong("Id", id);

        // Setting the id
        frag.setArguments(data);
        // Getting reference to the FragmentManager
        FragmentManager fragmentManager = activity.getFragmentManager();

        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();

        // Adding a fragment to the fragment transaction
        ft.replace(R.id.content_frame, frag);

        // Committing the transaction
        ft.commit();
    }
}
