package com.loz.iyaf.kpound;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.loz.R;

public class KPoundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kpound);

        Button signUpCust = (Button)findViewById(R.id.signupCustomers);
        Button signUpBus = (Button)findViewById(R.id.signupBusinesses);

        signUpCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kingstonpound.org/customer-pilot-sign/"));
                startActivity(browserIntent);
            }
        });

        signUpBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kingstonpound.org/business-sign/"));
                startActivity(browserIntent);
            }
        });
    }
}
