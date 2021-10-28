package mi.polinema.uts_pemjar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_regirtasi.*
import java.util.*

class SignUpActvity : AppCompatActivity(), View.OnClickListener {


    companion object {
        var TAG = SignUpActvity::class.java.simpleName
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regirtasi)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance("https://rio-raihan-d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("TabelUsers")

        btnRegis.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnRegis -> {
                val email: String = etRegEmail.text.toString().trim()
                val password: String = etRegPassword.text.toString().trim()
                val username: String = etRegUsername.text.toString().trim()

                if(email.isEmpty()){
                    etRegEmail.error = "Email harus diisi"
                    etRegEmail.requestFocus()
                    return
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etRegEmail.error = "Email tidak valid"
                    etRegEmail.requestFocus()
                    return
                }

                if(password.isEmpty() || password.length < 8){
                    etRegPassword.error = "Password harus diisi"
                    etRegPassword.requestFocus()
                    return
                }

                if(username.isEmpty()){
                    etRegUsername.error = "Email harus diisi"
                    etRegUsername.requestFocus()
                    return
                }


                registerUser(User(UUID.randomUUID().toString(),email, username, password))
            }
            R.id.btnLogRegister -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun registerUser(userData: User) {
        auth.createUserWithEmailAndPassword(userData.email, userData.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val currentUser = auth.currentUser!!
                    if (currentUser != null) {
                        currentUser!!.updateProfile(userProfileChangeRequest {
                            displayName = etRegUsername.text.toString()
                        })
                        currentUser!!.sendEmailVerification()
                        db.child(userData.id).setValue(userData).addOnCompleteListener {
                            Toast.makeText(
                                this,
                                "Berhasil mendaftarkan user, silahkan cek email anda untuk memferivikasi akun anda",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
//                            return@addOnCompleteListener
                        }
                    }

                }
            }
            .addOnFailureListener {
                Log.d("user register", it.message.toString())
            }
    }
}