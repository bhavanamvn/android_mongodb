// // My contact class is defined in the smae ppackage "com.example.navyaspc.mongodb", SO, in this file we can create object for Mycontact.
// (2) L39,40,41 define edittext_fname,_lname to findviewbyid(R.id.firstname) ... 
// (3) In mainactivity.xml all buttons "save,reset, fetch " are linked to methods "save,reset,fetch" methods defined in this file...
// (4) save method updates pojo object "my contact" and also saves it to "mongo db".. Reset method updates pojo object to be null..
// (5) What is toast?
// (6) Async task: AsyncTask is a mechanism for executing operations in a background thread without having to manually handle thread creation or execution
//         AsyncTask<Params, Progress, Result>
//         Params - the type that is passed into the execute() method.
//         Progress - the type that is used within the task to track progress.
//         Result - the type that is returned by doInBackground().
//     Here AsyncTask<Object, Void, Boolean> takes object "My contact", returns "true/false" after execution of doInBackground()

package com.example.navyaspc.mongodb;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editText_fname;
    EditText editText_lname;
    EditText editText_phonenumber;
    ArrayList<MyContact> returnValues = new ArrayList<MyContact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_fname = (EditText) findViewById(R.id.firstName);
        editText_lname = (EditText) findViewById(R.id.lastName);
        editText_phonenumber = (EditText) findViewById(R.id.phoneNumber);
    }

    public void reset(View v){
        editText_fname.setText("");
        editText_lname.setText("");
        editText_phonenumber.setText("");

        Toast.makeText(this, "Reset done", Toast.LENGTH_SHORT).show();
    }

    public void save(View v) {

        MyContact contact = new MyContact();

        contact.setFirst_name(editText_fname.getText().toString());
        contact.setLast_name(editText_lname.getText().toString());
        contact.setPhone_number(editText_phonenumber.getText().toString());

        MongoLabSaveContact tsk = new MongoLabSaveContact();
        tsk.execute(contact);

        Toast.makeText(this, "Saved to MongoDB!!", Toast.LENGTH_SHORT).show();

        editText_fname.setText("");
        editText_lname.setText("");
        editText_phonenumber.setText("");
    }

    final class MongoLabSaveContact extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
//          what does below line mean?
            MyContact contact = (MyContact) params[0];
            Log.d("contact", ""+contact);

            try {


                SupportData sd = new SupportData();
                URL url = new URL(sd.buildContactsSaveURL());
//              Building url from api key got from mongolab

                Log.d("url", ""+url);
//              https://stackoverflow.com/questions/10116961/can-you-explain-the-httpurlconnection-connection-process to understand HttpURLConnection connection process
                
                 // instantiate the HttpURLConnection with the URL object - A new
                // connection is opened every time by calling the openConnection
                // method of the protocol handler for this URL.
                // 1. This is the point where the connection is opened
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("PUT");
                // set connection output to true
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type",
                        "application/json");
                connection.setRequestProperty("Accept", "application/json");
                
                
                 // instantiate OutputStreamWriter using the output stream, returned
                // from getOutputStream, that writes to this connection.
               // 2. This is the point where you'll know if the connection was
              // successfully established. If an I/O error occurs while creating
             // the output stream, you'll see an IOException.
//              Here you are opening stream to write data, but you aren't actually writing

                OutputStreamWriter osw = new OutputStreamWriter(
                        connection.getOutputStream());
                
                // you are writing contact details as string fetched from support data class
                osw.write(sd.createContact(contact));
                osw.flush();
                // Closes this output stream and releases any system resources
               // associated with this stream. At this point, we've sent all the
              // data. Only the outputStream is closed at this point, not the
              // actual connection
                osw.close();

                Log.d("Response code", ""+(connection.getResponseCode()));
                if(connection.getResponseCode() <205)
                {

                    return true;
                }
                else
                {
                    return false;

                }

            } catch (Exception e) {
                e.getMessage();
                Log.d("Got error", e.getMessage());
                return false;

            }

        }

    }

    public void fetch(View v){
        GetContactsAsyncTask task = new GetContactsAsyncTask();
        try {
            returnValues = task.execute().get();
            MyContact FetchedData = (MyContact) returnValues.toArray()[0];

            editText_fname.setText(FetchedData.getFirst_name());
            editText_lname.setText(FetchedData.getLast_name());
            editText_phonenumber.setText(FetchedData.getPhone_nubmer());

            Toast.makeText(this, "Fetched from MongoDB!!", Toast.LENGTH_SHORT).show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
