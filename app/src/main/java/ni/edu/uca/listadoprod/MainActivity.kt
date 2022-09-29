package ni.edu.uca.listadoprod

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import dataadapter.ProductoAdapter
import dataclass.Producto
import ni.edu.uca.listadoprod.databinding.ActivityMainBinding
import ni.edu.uca.listadoprod.databinding.ItemlistaBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var listaProd = ArrayList<Producto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniciar()
    }
    private fun limpiar(){
        with(binding){
            etID.setText("")
            etNombreProd.setText("")
            etPrecio.setText("")
            etID.requestFocus()
        }
    }

    private fun agregarProd(){
        with(binding){
            try {
                val id: Int = etID.text.toString().toInt()
                val nombre: String = etNombreProd.text.toString()
                val precio: Double = etPrecio.text.toString().toDouble()
                val prod = Producto(id, nombre, precio)
                listaProd.add(prod)
            }catch (ex: Exception){
                Toast.makeText(this@MainActivity, "Error: ${ex.toString()}", Toast.LENGTH_SHORT).show()
            }
            rcvLista.layoutManager = LinearLayoutManager(this@MainActivity)
            rcvLista.adapter = ProductoAdapter(listaProd,
                {producto -> onItemSelected(producto)},
                {position -> onDeleteItem(position)},
                {position -> onUpdateItem(position)})
            limpiar()
        }
    }

    private fun iniciar(){
        binding.btnAgregar.setOnClickListener {
            agregarProd()
        }
        binding.btnLimpiar.setOnClickListener {
            limpiar()
        }

    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    fun onDeleteItem(position: Int) {
        val message = AlertDialog.Builder(this)
        message.setTitle("Eliminar")
        message.setMessage("Desea eliminar?")
        message.setPositiveButton("Eliminar",DialogInterface.OnClickListener{_,_ ->
            with(binding){
                listaProd.removeAt(position)
                rcvLista.adapter?.notifyItemRemoved(position)
                limpiar()
            }
        })
        message.setNegativeButton("Cancelar",DialogInterface.OnClickListener(){_,_ ->
            Toast.makeText(this, "Operacion cancelada", Toast.LENGTH_SHORT).show()
            limpiar()
        })
        message.show()

    }

    fun onUpdateItem(position: Int){
        try {
            with(binding) {
                val id: Int = etID.text.toString().toInt()
                val nombre: String = etNombreProd.text.toString()
                val precio: Double = etPrecio.text.toString().toDouble()
                val prod = Producto(id, nombre, precio)
                listaProd.set(position, prod)
                rcvLista.adapter?.notifyItemChanged(position)
            }
        }catch (ex: Exception){
            Toast.makeText(this@MainActivity, "No se pueden editar datos vacios", Toast.LENGTH_SHORT).show()
        }
    }
    fun onItemSelected(producto: Producto) {
        with(binding) {
            etID.text = producto.id.toString().toEditable()
            etNombreProd.text = producto.nombre.toEditable()
            etPrecio.text = producto.precio.toString().toEditable()
        }
    }


}

