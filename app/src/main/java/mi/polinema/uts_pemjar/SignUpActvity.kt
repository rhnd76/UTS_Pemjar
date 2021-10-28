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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_regirtasi.*

class SignUpActvity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnRegis ->{
                val email :String = etRegEmail.text.toString().trim()
                val password :String = etRegPassword.text.toString().trim()
                val username :String = etRegUsername.text.toString().trim()

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


                registerUser(email, password,User(email,username,password,null,null,null))
            }
            R.id.btnLogRegister -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    companion object {
        var TAG = SignUpActvity::class.java.simpleName
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regirtasi)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("TabelUsers")

        btnRegis.setOnClickListener(this)
    }

    private fun registerUser(email: String, password: String,userData:User) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val currentUser = auth.currentUser!!
                    if (currentUser!=null){
                        currentUser!!.updateProfile(userProfileChangeRequest {
                            displayName = etRegUsername.text.toString()
                        })
                        currentUser!!.sendEmailVerification()
                        db.child(userData.email).setValue(userData).addOnCompleteListener {
                            Toast.makeText(
                                this, "Berhasil mendaftarkan user, silahkan cek email anda untuk memferivikasi akun anda",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent =Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            return@addOnCompleteListener
                        }
                    }

                }
            }
    }
}