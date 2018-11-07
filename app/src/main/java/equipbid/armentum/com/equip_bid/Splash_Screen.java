package equipbid.armentum.com.equip_bid;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Splash_Screen extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    String p_username, p_token, p_email,p_id;
    private String user_roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        final ActionBar actionBar = getActionBar();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        p_username = sharedpreferences.getString("username",null);
        p_token    = sharedpreferences.getString("token",null);
        p_email     = sharedpreferences.getString("email",null);
        user_roles  = sharedpreferences.getString("user_roles",null);
        p_id  = sharedpreferences.getString("ID",null);
        Log.e("DetailsSplash",p_email+" ..username.. "+ p_username +" ..tokentoken.. "+p_token +" ..id.."+user_roles);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if (p_token != null)
                    {
                        startActivity(new Intent(Splash_Screen.this, Auction_Screen_Main.class));
                        finish();
                    } else {
                        Intent intent = new Intent(Splash_Screen.this,Login_Activity.class);
                        startActivity(intent);
                    }

                    /*Intent intent = new Intent(Splash_Screen.this,Login_Activity.class);
                    startActivity(intent);
                    finish();*/
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}