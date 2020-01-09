package com.example.tcpandroidtooracledb;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

public class MainActivity extends AppCompatActivity {

    private static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@192.168.10.100:1521:odb";
    private static final String DEFAULT_USERNAME = "tApp";
    private static final String DEFAULT_PASSWORD = "101171";
    private Connection connection;

    Button btn ;
    EditText edUserId, edUserName,edUserPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        edUserId = findViewById(R.id.userId);
        // edUserName = findViewById(R.id.userName);
        edUserPass = findViewById(R.id.userPassword);
        if (android.os.Build.VERSION.SDK_INT > 15) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginValid();
            }
        });
        //TextView tv = (TextView) findViewById(R.id.hello);
        try {
            this.connection = createConnection();
            //Statement stmt=connection.createStatement();
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void LoginValid(){
        try {
            CallableStatement callableStatement;
            callableStatement = connection.prepareCall("call signin(?,?,?)");

            callableStatement.setString(1,edUserId.getText().toString());
            //callableStatement.setString(2,edUserName.getText().toString());
            callableStatement.setString(2,edUserPass.getText().toString());
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            if (!callableStatement.getString(3).equals("False")){
                Toast.makeText(MainActivity.this, "Successfully LogIn", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,WelcomePage.class);
                startActivity(intent);
                finish();
                return;
            }
            else {
                Toast.makeText(MainActivity.this, "Invalid Email & Password", Toast.LENGTH_SHORT).show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }
    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }
}