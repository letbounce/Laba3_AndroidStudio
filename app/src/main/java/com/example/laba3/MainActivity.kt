package com.example.laba3

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
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

        // Оновлення списку після змін
        updateList()

        addButton.setOnClickListener {
            val title = titleInput.text.toString()
            val year = yearInput.text.toString().toIntOrNull() ?: 0
            val genre = genreInput.text.toString()
            val rating = ratingInput.text.toString().toDoubleOrNull() ?: 0.0

            if (title.isNotBlank() && genre.isNotBlank() && year > 0 && rating > 0) {
                val movie = Movie(title = title, year = year, genre = genre, rating = rating)
                dbHandler.addMovie(movie)
                updateList()  // Оновлюємо список після додавання
                titleInput.text.clear()
                yearInput.text.clear()
                genreInput.text.clear()
                ratingInput.text.clear()
            } else {
                Toast.makeText(this, "Заповніть усі поля коректно", Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val selectedMovie = dbHandler.getAllMovies()[position]

            val options = arrayOf("Редагувати", "Видалити")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Оберіть дію")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> showEditDialog(selectedMovie) // Редагування
                    1 -> {
                        dbHandler.deleteMovie(selectedMovie)   // Видалення
                        updateList()                           // Оновлення списку
                    }
                }
            }
            builder.show()
            true
        }
    }

    // Метод для оновлення списку
    private fun updateList() {
        val movies = dbHandler.getAllMovies()
        val movieTitles = movies.map {
            "${it.title} (${it.year}) - ${it.genre}, рейтинг: ${it.rating}"
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieTitles)
        listView.adapter = adapter
    }

    private fun showEditDialog(movie: Movie) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_movie, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.editTitle)
        val yearInput = dialogView.findViewById<EditText>(R.id.editYear)
        val genreInput = dialogView.findViewById<EditText>(R.id.editGenre)
        val ratingInput = dialogView.findViewById<EditText>(R.id.editRating)

        // Встановлюємо поточні значення
        titleInput.setText(movie.title)
        yearInput.setText(movie.year.toString())
        genreInput.setText(movie.genre)
        ratingInput.setText(movie.rating.toString())

        AlertDialog.Builder(this)
            .setTitle("Редагувати фільм")
            .setView(dialogView)
            .setPositiveButton("Зберегти") { _, _ ->
                movie.title = titleInput.text.toString()
                movie.year = yearInput.text.toString().toIntOrNull() ?: movie.year
                movie.genre = genreInput.text.toString()
                movie.rating = ratingInput.text.toString().toDoubleOrNull() ?: movie.rating
                dbHandler.updateMovie(movie)
                updateList()  // Оновлюємо список після редагування
            }
            .setNegativeButton("Скасувати", null)
            .show()
    }
}
