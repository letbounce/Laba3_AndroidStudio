package com.example.laba3

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var editTextInput: EditText
    private lateinit var addButton: Button
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val itemList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextInput = findViewById(R.id.editTextInput)
        addButton = findViewById(R.id.addButton)
        listView = findViewById(R.id.listView)

        adapter = ArrayAdapter(this, R.layout.list_item, itemList)
        listView.adapter = adapter

        addButton.setOnClickListener {
            val text = editTextInput.text.toString()
            if (text.isNotBlank()) {
                itemList.add(text)
                adapter.notifyDataSetChanged()
                editTextInput.text.clear()
            } else {
                Toast.makeText(this, "Поле не повинно бути порожнім", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
