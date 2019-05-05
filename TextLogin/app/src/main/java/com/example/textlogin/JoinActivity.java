package com.example.textlogin;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import com.example.textlogin.databinding.ActivityJoinBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JoinActivity extends AppCompatActivity {
    ActivityJoinBinding Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_join);

        Binding.goJoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String email = Binding.joinEmail.getText().toString();
                final String pw = Binding.joinPw.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Binding.joinCheck.setText("이메일 형식이 아닙니다.");
                }
                else if(pw.length() == 0){
                    Binding.joinCheck.setText("패스워드를 입력하세요.");
                }
                else {
                    Binding.joinCheck.setText(email + pw);
                    new JSONTask().execute("http://127.0.0.1:4000/post");
                    finish();
                }
            }
        });
    }
    public class JSONTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... urls){
            final String email = Binding.joinEmail.getText().toString();
            final String pw = Binding.joinPw.getText().toString();

            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("Email", email);
                jsonObject.accumulate("Password", pw);

                HttpURLConnection con = null;

                BufferedReader reader = null;
                try{
                    URL url = new URL(urls[0]);

                    con = (HttpURLConnection)url.openConnection();

                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type","application/json");

                    con.setRequestProperty("Accept", "text/html");

                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outstream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line=reader.readLine())!=null){
                        buffer.append(line);
                    }
                    return buffer.toString();

                }catch(MalformedURLException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }finally{
                    if(con!=null){
                        con.disconnect();
                    }
                    try{
                        if(reader!=null) {
                            reader.close();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Binding.joinCheck.setText(result);
        }
    }
}
