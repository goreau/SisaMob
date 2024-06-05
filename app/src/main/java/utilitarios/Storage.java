

/**
 * Created by henrique on 20/09/2016.
 */
package utilitarios;


import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;

import com.sucen.sisamob.PrincipalActivity;

public class Storage {
    public static final String PREFS_NAME = "SisamobStore";
    private static Context appContext;

    public static void insere(String key, String value){
        appContext = PrincipalActivity.getSisamobContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);

        // Commit the edits!
        editor.commit();
    }

    public static String recupera(String key){
        appContext = PrincipalActivity.getSisamobContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(appContext);
        String value = settings.getString(key,"");
        return value;
    }

    public static String dataAtual() {
        Date now = new Date();
        return DateFormat.format("dd-MM-yyyy",now).toString();
    }

    public static void insere(String key, int value) {
        appContext = PrincipalActivity.getSisamobContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);

        // Commit the edits!
        editor.commit();
    }

    public static Integer recuperaI(String key){
        appContext = PrincipalActivity.getSisamobContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(appContext);
        Integer value = settings.getInt(key,0);
        return value;
    }

}
