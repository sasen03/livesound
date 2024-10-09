package com.example.livesound.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.livesound.R

data class Playlist(val image: Int, val name: String, val artist: String, val duration: String)

class PlaylistAdapter(private val playlists: List<Playlist>, private val onPlayClick: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistImage: ImageView = itemView.findViewById(R.id.playlist_image)
        val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
        val artistsName: TextView = itemView.findViewById(R.id.artists_name)
        val playlistDuration: TextView = itemView.findViewById(R.id.playlist_duration)
        val playButton: ImageButton = itemView.findViewById(R.id.play_playlist_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.playlistImage.setImageResource(playlist.image)
        holder.playlistName.text = playlist.name
        holder.artistsName.text = playlist.artist
        holder.playlistDuration.text = playlist.duration

        // Configurar el botón de play
        holder.playButton.setOnClickListener {
            onPlayClick(playlist) // Llamada a la función de reproducción
        }
    }

    override fun getItemCount() = playlists.size
}
