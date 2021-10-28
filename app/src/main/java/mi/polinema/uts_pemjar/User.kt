package mi.polinema.uts_pemjar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val nama: String? = null,
    val alamat: String? = null,
    val telp: Int? = null
) : Parcelable
