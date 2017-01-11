package io.agora.demo.agora;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by apple on 15/9/9.
 */
public class LoginActivity extends BaseActivity {

    private EditText mVendorKey;
    private EditText mChannelID;

    @Override
    public void onCreate(Bundle savedInstance) {

        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstance);

        new RequestTask().execute("http://192.168.99.253:8970/agora.inner.test.key.txt"); // just update inner testing vendor key

        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {

        // bind listeners
        findViewById(R.id.action_video_calling).setOnClickListener(getViewClickListener());
        findViewById(R.id.action_voice_calling).setOnClickListener(getViewClickListener());

        this.mVendorKey = (EditText) findViewById(R.id.input_vendor_key);
        this.mChannelID = (EditText) findViewById(R.id.input_room_number);

        // please your own key, the test key is unavailable soon.
        this.mVendorKey.setText(getSharedPreferences(getClass().getName(), Context.MODE_PRIVATE).getString(ChannelActivity.EXTRA_VENDOR_KEY, "5a04ec409d984031bd0ebd417efd7f8f"));
        this.mChannelID.setText(getSharedPreferences(getClass().getName(), Context.MODE_PRIVATE).getString(ChannelActivity.EXTRA_CHANNEL_ID, "88888888"));

    }

    @Override
    public void onUserInteraction(View view) {


        // Ensure inputs are valid;
        if(!validateInput()){
            return ;
        }


        switch (view.getId()) {
            default:
                super.onUserInteraction(view);

                // Voice calling
            case R.id.action_voice_calling: {

                Intent intent = new Intent(LoginActivity.this, ChannelActivity.class);
                intent.putExtra(ChannelActivity.EXTRA_CALLING_TYPE, ChannelActivity.CALLING_TYPE_VOICE);
                intent.putExtra(ChannelActivity.EXTRA_VENDOR_KEY, mVendorKey.getText().toString());
                intent.putExtra(ChannelActivity.EXTRA_CHANNEL_ID, mChannelID.getText().toString());
                startActivity(intent);
//                finish();

            }
            break;

            // Video calling
            case R.id.action_video_calling: {

                Intent intent = new Intent(LoginActivity.this, ChannelActivity.class);
                intent.putExtra(ChannelActivity.EXTRA_CALLING_TYPE, ChannelActivity.CALLING_TYPE_VIDEO);
                intent.putExtra(ChannelActivity.EXTRA_VENDOR_KEY, mVendorKey.getText().toString());
                intent.putExtra(ChannelActivity.EXTRA_CHANNEL_ID, mChannelID.getText().toString());
                startActivity(intent);
//                finish();

            }
            break;

        }



        // remember the vendor key and channel ID
        getSharedPreferences(getClass().getName(), Context.MODE_PRIVATE)
                .edit()
                .putString(ChannelActivity.EXTRA_VENDOR_KEY, mVendorKey.getText().toString())
                .putString(ChannelActivity.EXTRA_CHANNEL_ID, mChannelID.getText().toString())
                .apply();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }


    boolean validateInput() {

        String vendorKey = mVendorKey.getText().toString();
        String roomNumber = mChannelID.getText().toString();

        // validate vendor key
        if (TextUtils.isEmpty(vendorKey)) {
            Toast.makeText(getApplicationContext(), R.string.key_required, Toast.LENGTH_SHORT).show();
            return false;
        }

        // validate room number - cannot be empty
        if (TextUtils.isEmpty(roomNumber)) {
            Toast.makeText(getApplicationContext(), R.string.room_required, Toast.LENGTH_SHORT).show();
            return false;
        }


        // validate room number - should be digits only
        if(!TextUtils.isDigitsOnly(roomNumber)){
            Toast.makeText(getApplicationContext(), R.string.room_required, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    class RequestTask extends AsyncTask<String, String, String> {


        String responseString = null;

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            if (responseString != null) {
                mVendorKey.setText(responseString.replaceAll("\\s+",""), TextView.BufferType.EDITABLE);
            }
        }
    }

}
