package mi.polinema.uts_pemjar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class CustomAdapter (val context : Context,
                     arrayList : ArrayList<HashMap<String, Any>> ) : BaseAdapter() {
    val F_URL = "file_url"
    val F_NAMA = "nama"
    val F_TYPE = "file_type"
    val F_CAPTION = "caption"
    val list = arrayList
    var uri = Uri.EMPTY

    inner class ViewHolder(){
        var txnama : TextView? = null
        var txcaption : TextView? = null
        var img : ImageView? = null
    }
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder = ViewHolder()
        var view:View? = convertView
        if(convertView == null){
            var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                    as LayoutInflater
            view = inflater.inflate(R.layout.list_upload, null, true)

            holder.txnama = view!!.findViewById(R.id.txnama) as TextView
            holder.txcaption = view!!.findViewById(R.id.txcaption) as TextView
            holder.img = view!!.findViewById(R.id.img) as ImageView

            view.tag = holder
        }else{
            holder = view!!.tag as ViewHolder
        }

        uri = Uri.parse(list.get(position).get(F_URL).toString())

        holder.txnama!!.setText(list.get(position).get(F_NAMA).toString())
        holder.txcaption!!.setText(list.get(position).get(F_CAPTION).toString())
        Picasso.get().load(uri).into(holder.img)
        return view!!
    }
}