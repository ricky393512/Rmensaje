package rmensaje.telcel.com.rmensaje;

/**
 * Created by PIN7025 on 12/04/2017.
 */

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        //Obtener telefono e imei

        TelephonyManager mTelephonyManager;
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String numeroTelefono = mTelephonyManager.getLine1Number();
        Log.e(TAG, "el telefono ess   1!!!!!!!!!! : " + numeroTelefono);

        TelephonyManager telemamanger = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String getSimSerialNumber = telemamanger.getSimSerialNumber();

        Log.e(TAG, "el telefono es sim   1!!!!!!!!!! : " + getSimSerialNumber);

        String imei = getImei(getBaseContext());

        Log.e(TAG, "el imei es sim   1!!!!!!!!!! : " + imei);
    }

    public static String getImei(Context c) {
        TelephonyManager telephonyManager = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
