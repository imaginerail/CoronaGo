package com.aneeq.coronago.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.aneeq.coronago.R
import com.aneeq.coronago.fragment.CountriesFragment
import com.aneeq.coronago.util.ConnectionManager
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    lateinit var imgProfile: ImageView
    lateinit var etName: EditText
    lateinit var etPass: EditText
    lateinit var etEmail: EditText
    lateinit var btnSignUp: Button
    lateinit var txtAlreadyLogin: TextView
    lateinit var etPhone: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        imgProfile = findViewById(R.id.imgProfile)
        etName = findViewById(R.id.etName)
        etPass = findViewById(R.id.etPass)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        btnSignUp = findViewById(R.id.btnSignUp)
        txtAlreadyLogin = findViewById(R.id.txtAlreadyLogin)

        txtAlreadyLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
        }
        btnSignUp.setOnClickListener {
            val nameString = etName.text.toString()
            val emailString = etEmail.text.toString()
            val passString = etPass.text.toString()
            val phoneString = etPhone.text.toString()
            if (ConnectionManager().checkConnection(this)) {
                if (nameString == "" || emailString == "" || passString == "" || phoneString == "")
                    return@setOnClickListener
                else
                    setUpRegister()
            } else {
                noInternet()
            }
        }

    }

    private fun setUpRegister() {


        val nameString = etName.text.toString()
        val emailString = etEmail.text.toString()
        val passString = etPass.text.toString()
        val phoneString = etPhone.text.toString()
        val url = "https://androaneeq.000webhostapp.com/registerlol.php"
        val queue = Volley.newRequestQueue(this)
        val request = object :
            StringRequest(Method.POST, url, Response.Listener {

                etName.setText("")
                etEmail.setText("")
                etPass.setText("")
                etPhone.setText("")

                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))

            }, Response.ErrorListener {

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();

            }) {

            override fun getParams(): MutableMap<String, String> {

                val params = HashMap<String, String>()
                params["name"] = nameString
                params["email"] = emailString
                params["password"] = passString
                params["phone"] = phoneString
                return params
            }
        }
        queue.add(request)
    }

    private fun noInternet() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Failure")
        dialog.setMessage("Internet Connection NOT Found")
        dialog.setPositiveButton("Open Settings")
        { _, _ ->
            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(settingsIntent)
            finish()
        }
        dialog.setNegativeButton("Exit")
        { _, _ ->
            ActivityCompat.finishAffinity(this)
        }
        dialog.create()
        dialog.show()
    }
}
/*<?php

/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['patient_name']) && isset($_POST['patient_gender']) && isset($_POST['patient_address']) && isset($_POST['patient_email'])) {

    $patient_name = $_POST['patient_name'];
    $patient_gender = $_POST['patient_gender'];
    $patient_address = $_POST['patient_address'];
    $patient_email = $_POST['patient_email'];


     define('DB_USER', "a9723596_patient"); // db user
define('DB_PASSWORD', ""); // db password (mention your db password here)
define('DB_DATABASE', "a9723596_data"); // database name
define('DB_SERVER', "mysql13.000webhost.com"); // db server
// array for JSON response



 $conn = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD,DB_DATABASE);


$sql = "INSERT INTO patient_info(patient_name,patient_gender,patient_email,patient_address) VALUES('$patient_name','$patient_gender','$patient_email','$patient_address')";

    // mysql inserting a new row
   $result = $conn->query($sql) or die (mysqli_connect_error());

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";

        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>*/

/*public class AddNewPatient extends Activity {
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputGender;
    EditText inputEmail;
EditText inputAddress;
    // url to create new product
    private static String url_create_product = "http://vishalthakkar.netai.net/androidphp/add_new_patient.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_patient);

        // Edit Text
        inputAddress=(EditText)findViewById(R.id.inputAddress1);
        inputName = (EditText) findViewById(R.id.inputName);
        inputGender = (EditText) findViewById(R.id.inputGender);
        inputEmail = (EditText) findViewById(R.id.inputEmail);

        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnCreate);

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewPatient().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new Patient
     * */
    class CreateNewPatient extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddNewPatient.this);
            pDialog.setMessage("Uploading to server...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String gender = inputGender.getText().toString();
            String email = inputEmail.getText().toString();
            String Address= inputAddress.getText().toString();
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("patient_name", name));
            params.add(new BasicNameValuePair("patient_gender", gender));
            params.add(new BasicNameValuePair("patient_email", email));
            params.add(new BasicNameValuePair("patient_address",Address));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), patient_info.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}*/