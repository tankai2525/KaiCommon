package tan.kai.com.kaicommon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import tan.kai.com.oaid.MsaIdHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.but1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("main", "----" + MsaIdHelper.getInstance().getOaid());
            }
        });
    }
}
