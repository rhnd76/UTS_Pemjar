package mi.polinema.uts_pemjar

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListAdapter
import android.widget.SimpleAdapter
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_profil.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ProfilActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var db: DatabaseReference
    val F_NAME = "nama_profil"
    val F_EMAIL = "email"
    val F_USER = "username"
    val F_ALAMAT = "alamat_profil"
    val F_NOMOR = "no_telepon"
    var fileName = ""
    lateinit var user: User
    private lateinit var auth: FirebaseAuth
    var profil = Profil()
    lateinit var adapter: ListAdapter
    var hm = HashMap<String, Any>()

    companion object {
        const val EXTRAPROFIL = "extra_profil"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        btnSimpan.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
        user = intent.getParcelableExtra<User>(EXTRAPROFIL)!!
    }

    override fun onStart() {
        super.onStart()
        showProfil()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            UsernameProfil.setText(currentUser!!.displayName)
            EmailProfil.setText(currentUser!!.email)
        }
        db = FirebaseDatabase.getInstance().getReference("TabelUsers")
    }

    fun showProfil() {
        NamaProfil.setText(user.nama)
        EmailProfil.setText(user.email)
        UsernameProfil.setText(user.username)
        AlamatProfil.setText(user.alamat)
        user.telp?.let { No_telpProfil.setText(it) }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnSimpan -> {
                db.child(user.email).setValue(
                    User(
                        EmailProfil.text.toString(),
                        UsernameProfil.text.toString(),
                        user.password,
                        NamaProfil.text.toString(),
                        AlamatProfil.text.toString(),
                        No_telpProfil.text.toString().toInt()
                    )
                ).addOnCompleteListener {
                    Toast.makeText(
                        this,
                        "File successfully uploaded",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intentProf = Intent(this, MainActivity::class.java)
                    startActivity(intentProf)
                }
            }
        }
    }
}