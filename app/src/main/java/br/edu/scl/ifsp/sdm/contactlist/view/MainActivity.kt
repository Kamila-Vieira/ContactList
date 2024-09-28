package br.edu.scl.ifsp.sdm.contactlist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(this, android.R.layout.simple_list_item_1, contactList.map { it.toString() })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        fillContacts()
        amb.contactsLv.adapter = contactAdapter
    }

    private fun fillContacts(){
        for (i in 1..50){
            contactList.add(
                Contact(
                    name = "Name $i",
                    address = "Endereço $i",
                    email = "Email $i",
                    phone = "Telefone $i",
                )
            )
        }
    }
}