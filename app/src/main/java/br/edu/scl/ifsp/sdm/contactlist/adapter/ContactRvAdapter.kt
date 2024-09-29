package br.edu.scl.ifsp.sdm.contactlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.sdm.contactlist.databinding.TileContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactRvAdapter(private val contactList: MutableList<Contact>):
    RecyclerView.Adapter<ContactRvAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(tileContactBinding: TileContactBinding): RecyclerView.ViewHolder(tileContactBinding.root){
        val nameTv: TextView = tileContactBinding.nameTv
        val emailTv: TextView = tileContactBinding.emailTv
    }

    override fun getItemCount(): Int = contactList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        TileContactBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            ContactViewHolder(this)
        }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
       contactList[position].also{ contact ->
           with(holder){
               nameTv.text = contact.name
               emailTv.text = contact.email
           }
       }
    }

}