package com.example.laba3

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var dbHandler: DatabaseHandler
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titleInput = findViewById<EditText>(R.id.titleInput)
        val yearInput = findViewById<EditText>(R.id.yearInput)
        val genreInput = findViewById<EditText>(R.id.genreInput)
        val ratingInput = findViewById<EditText>(R.id.ratingInput)
        val addButton = findViewById<Button>(R.id.addButton)
        listView = findViewById(R.id.listView)

        dbHandler = DatabaseHandler(this)

        fun updateList() {
            val movies = dbHandler.getAllMovies()
            val movieTitles = movies.map {
                "${it.title} (${it.year}) - ${it.genre}, рейтинг: ${it.rating}"
            }
            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieTitles)
            listView.adapter = adapter
        }

        addButton.setOnClickListener {
            val title = titleInput.text.toString()
            val year = yearInput.text.toString().toIntOrNull() ?: 0
            val genre = genreInput.text.toString()
            val rating = ratingInput.text.toString().toDoubleOrNull() ?: 0.0

            if (title.isNotBlank() && genre.isNotBlank() && year > 0 && rating > 0) {
                val movie = Movie(title = title, year = year, genre = genre, rating = rating)
                dbHandler.addMovie(movie)
                updateList()
                titleInput.text.clear()
                yearInput.text.clear()
                genreInput.text.clear()
                ratingInput.text.clear()
            } else {
                Toast.makeText(this, "Заповніть усі поля коректно", Toast.LENGTH_SHORT).show()
            }
        }


        updateList()
    }

}
