package com.example.livesound.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.livesound.R

data class Video(val image: Int, val name: String, val artist: String, val duration: String)

class VideoAdapter(private val videos: List<Video>, private val onPlayClick: (Video) -> Unit) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoImage: ImageView = itemView.findViewById(R.id.video_image)
        val videoName: TextView = itemView.findViewById(R.id.video_name)
        val artistName: TextView = itemView.findViewById(R.id.artist_name)
        val videoDuration: TextView = itemView.findViewById(R.id.video_duration)
        val playVideoButton: ImageButton = itemView.findViewById(R.id.play_video_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.videoImage.setImageResource(video.image)
        holder.videoName.text = video.name
        holder.artistName.text = video.artist
        holder.videoDuration.text = video.duration

        // Configurar el botón de play
        holder.playVideoButton.setOnClickListener {
            onPlayClick(video) // Llamada a la función de reproducción
        }
    }

    override fun getItemCount() = videos.size
}
