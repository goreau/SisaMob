package utilitarios;

import com.sucen.sisamob.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class Custom_ProgressBar extends ProgressDialog {
    private AnimationDrawable anim;

    public Custom_ProgressBar(Context context) {
        super(context);
    }

    public static ProgressDialog ctor(Context context){
        Custom_ProgressBar dialog = new Custom_ProgressBar(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress);
        ImageView iv = (ImageView) findViewById(R.id.Animacao);
        iv.setBackgroundResource(R.drawable.progress_anim);
        anim = (AnimationDrawable) iv.getBackground();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        anim.stop();
    }

    @Override
    public void show() {
        super.show();
        anim.start();
    }

}
