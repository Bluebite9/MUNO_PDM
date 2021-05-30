package ro.pdm.muno_pdm.account.adaptor

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.account.models.User

class AdminAdaptor(
    var adminList: MutableList<User>?,
    val application: Application
) : RecyclerView.Adapter<AdminAdaptor.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        var firstNameTv: TextView = view.findViewById(R.id.firstNameTv)
        var lastNameTv: TextView = view.findViewById(R.id.lastNameTv)
        var emailTv: TextView = view.findViewById(R.id.emailTv)
        var cityTv: TextView = view.findViewById(R.id.cityTv)
        var countryTv: TextView = view.findViewById(R.id.countryTv)
        var phoneTv: TextView = view.findViewById(R.id.phoneTv);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context).inflate(R.layout.admin_user_line, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val user = adminList?.get(position)

        if (user != null) {
            holder.firstNameTv.text = user.firstName
            holder.lastNameTv.text = user.lastName
            holder.emailTv.text = user.email
            holder.cityTv.text = user.city
            holder.countryTv.text = user.county
            holder.phoneTv.text = user.phone

        }

    }

    override fun getItemCount(): Int {
        return adminList?.size ?: 0
    }

}