package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactRvAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity(), OnContactClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter: ContactRvAdapter by lazy {
        ContactRvAdapter(contactList, this)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_list)

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                contact?.also { newOrEditedContact ->
                    if(contactList.any{ it.id == newOrEditedContact.id }){
                        val position = contactList.indexOfFirst{ it.id == newOrEditedContact.id }
                        contactList[position] = newOrEditedContact
                    } else{
                        contactList.add(newOrEditedContact)
                    }
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }

        fillContacts()
        amb.contactsRv.adapter = contactAdapter
        amb.contactsRv.layoutManager = LinearLayoutManager(this)
        // registerForContextMenu(amb.contactsRv) TODO: Será feita a implementação do menu de contexto para o Recyclerview

        /* TODO: Será feita a implementação da seleção dos itens para o Recyclerview
        amb.contactsRv.setOnItemClickListener{ _, _, position, _ ->

        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> { false }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position

        return when(item.itemId){
            R.id.removeContactMi -> {
                onRemoveContact(position)
                true
            }
            R.id.editContactMi -> {
                onEditContact(position)
                true
            }
            else -> { false }
        }
    }

    override fun onContactClick(position: Int) {
        Intent(this, ContactActivity::class.java).apply{
            putExtra(EXTRA_CONTACT, contactList[position])
            putExtra(EXTRA_VIEW_CONTACT, true)
        }.also {
            startActivity(it)
        }
    }

    private fun onRemoveContact(position: Int){
        contactList.removeAt(position)
        contactAdapter.notifyDataSetChanged()
        Toast.makeText(this, getString(R.string.contact_removed), Toast.LENGTH_SHORT).show()
    }

    private fun onEditContact(position: Int){
        val contact = contactList[position]
        carl.launch(Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contact)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // unregisterForContextMenu(amb.contactsRv) TODO: Será feita a implementação do menu de contexto para o Recyclerview
    }

    private fun fillContacts(){
        for (i in 1..10){
            contactList.add(
                Contact(
                    id = i,
                    name = "Name $i",
                    address = "Endereço $i",
                    email = "Email $i",
                    phone = "Telefone $i",
                )
            )
        }
    }
}