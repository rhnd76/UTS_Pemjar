package mi.polinema.uts_pemjar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val nama: String? = "nama",
    val alamat: String? = "alamat",
    val telp: Int? = 0,
) : Parcelable
