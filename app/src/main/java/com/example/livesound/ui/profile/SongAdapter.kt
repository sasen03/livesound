package com.example.livesound.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.livesound.R

data class Song(val image: Int, val name: String, val artist: String, val duration: String)

class SongAdapter(private val songs: List<Song>, private val onPlayClick: (Song) -> Unit) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songImage: ImageView = itemView.findViewById(R.id.song_image)
        val songName: TextView = itemView.findViewById(R.id.song_name)
        val artistName: TextView = itemView.findViewById(R.id.artist_name)
        val songDuration: TextView = itemView.findViewById(R.id.song_duration)
        val playButton: ImageButton = itemView.findViewById(R.id.play_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.songImage.setImageResource(song.image)
        holder.songName.text = song.name
        holder.artistName.text = song.artist
        holder.songDuration.text = song.duration

        // Configurar el botón de play
        holder.playButton.setOnClickListener {
            onPlayClick(song) // Llamada a la función de reproducción
        }
    }

    override fun getItemCount() = songs.size
}
