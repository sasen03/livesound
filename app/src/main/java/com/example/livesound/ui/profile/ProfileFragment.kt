package com.example.livesound.ui.profile

import VideoDialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.livesound.R
import com.example.livesound.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val root: View = binding.root
        val profileTextView: TextView = binding.textProfile
        val userTextView: TextView = binding.userName
        val mailTextView: TextView = binding.userEmail

        profileViewModel.text.observe(viewLifecycleOwner) {
            profileTextView.text = it
        }

        profileViewModel.textUserName.observe(viewLifecycleOwner) {
            userTextView.text = it
        }

        profileViewModel.textUserMail.observe(viewLifecycleOwner) {
            mailTextView.text = it
        }

        val songList = listOf(
            Song(R.drawable.song_image1, "Don't smile at me", "Billie Eilish", "5:30"),
            Song(R.drawable.song_image2, "As it was", "Harry Styles", "4:10"),
            Song(R.drawable.song_image1, "Don't smile at me", "Billie Eilish", "5:30"),
            Song(R.drawable.song_image2, "As it was", "Harry Styles", "4:10"),
            // Añadir más canciones aquí
        )

        val adapterSongs = SongAdapter(songList) { song ->
            // Manejar la acción de reproducción aquí
            println(song)
            findNavController().navigate(R.id.action_profileFragment_to_playerFragment)
        }

        binding.recyclerViewSongs.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSongs.adapter = adapterSongs

        val videoList = listOf(
            Video(R.drawable.video_image1, "Super Freaky Girl", "Nicki Minaj", "5:33"),
            Video(R.drawable.video_image2, "Bad Habit", "Stive Lacy", "4:10"),
            Video(R.drawable.video_image1, "Super Freaky Girl", "Nicki Minaj", "5:33"),
            Video(R.drawable.video_image2, "Bad Habit", "Stive Lacy", "4:10"),
            // Añadir más canciones aquí
        )

        val adapterVideo = VideoAdapter(videoList) { video ->
            // Manejar la acción de reproducción aquí
            println(video)
            val videoDialog = VideoDialogFragment()
            videoDialog.show(parentFragmentManager, "videoDialog")
        }

        binding.recyclerViewVideos.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewVideos.adapter = adapterVideo

        val playList = listOf(
            Playlist(R.drawable.video_image1, "Super Freaky Girl", "Nicki Minaj", "5:33"),
            Playlist(R.drawable.video_image2, "Bad Habit", "Stive Lacy", "4:10"),
            // Añadir más canciones aquí
        )

        val adapterPlaylist = PlaylistAdapter(playList) { playlist ->
            // Manejar la acción de reproducción aquí
            println(playlist)
            findNavController().navigate(R.id.action_profileFragment_to_playListFragment)
        }

        binding.recyclerViewPlaylistItems.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewPlaylistItems.adapter = adapterPlaylist

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}