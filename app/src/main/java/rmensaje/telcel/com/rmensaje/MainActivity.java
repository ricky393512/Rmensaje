package rmensaje.telcel.com.rmensaje;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import rmensaje.telcel.com.rmensaje.utils.Autoupdater;


public class MainActivity extends AppCompatActivity {


    private Autoupdater updater;
    private RelativeLayout loadingPanel;
    private Context context;


    private void comenzarActualizar(){
        //Para tener el contexto mas a mano.
        context = this;
        //Creamos el Autoupdater.
        updater = new Autoupdater(this);
        //Ponemos a correr el ProgressBar.
        loadingPanel.setVisibility(View.VISIBLE);
        //Ejecutamos el primer metodo del Autoupdater.
        updater.DownloadData(finishBackgroundDownload);
    }

    /**
     * Codigo que se va a ejecutar una vez terminado de bajar los datos.
     */
    private Runnable finishBackgroundDownload = new Runnable() {
        @Override
        public void run() {
            //Volvemos el ProgressBar a invisible.
            loadingPanel.setVisibility(View.GONE);
            //Comprueba que halla nueva versión.
            if(updater.isNewVersionAvailable()){
                //Crea mensaje con datos de versión.
                String msj = "Nueva Version: " + updater.isNewVersionAvailable();
                msj += "\nCurrent Version: " + updater.getCurrentVersionName() + "(" + updater.getCurrentVersionCode() + ")";
                msj += "\nLastest Version: " + updater.getLatestVersionName() + "(" + updater.getLatestVersionCode() +")";
                msj += "\nDesea Actualizar?";
                //Crea ventana de alerta.
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
                dialog1.setMessage(msj);
                dialog1.setNegativeButton("CAncelaer", null);
                //Establece el boton de Aceptar y que hacer si se selecciona.
                dialog1.setPositiveButton("ACEptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Vuelve a poner el ProgressBar mientras se baja e instala.
                        loadingPanel.setVisibility(View.VISIBLE);
                        //Se ejecuta el Autoupdater con la orden de instalar. Se puede poner un listener o no
                        updater.InstallNewVersion(null);
                    }
                });

                //Muestra la ventana esperando respuesta.
                dialog1.show();
            }else{
                //No existen Actualizaciones.
                Log.e("RVISORTR","No Hay actualizaciones");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            PackageInfo pckginfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);

            Log.e("RVOSPR","version"+pckginfo.versionCode);
            Log.e("RVOSPR","versionNAme"+pckginfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        try {
           loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
            //Esto sirve si la actualizacion no se realiza al principio. No es este caso.
           loadingPanel.setVisibility(View.GONE);
         //   comenzarActualizar();

        }catch (Exception ex){
            //Por Cualquier error.
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG);
        }



        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this project the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);

                if (key.equals("AnotherActivity") && value.equals("True")) {
              //      Intent intent = new Intent(this, AnotherActivity.class);
              //      intent.putExtra("value", value);
                //    startActivity(intent);
                    String url = getIntent().getExtras().getString("paginaParaDireccionar");
                    //    if(url != null) {
                    Log.e("RVisorr", "paginaParaDireccionar " + url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    finish();
                }

            }
        }

        subscribeToPushService();

    }

    private void subscribeToPushService() {
      //  FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        Log.d("AndroidBash", "Subscribed");
        Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        Log.d("AndroidBash", token);
        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
         //   return true;
       // }

        return super.onOptionsItemSelected(item);
    }
}