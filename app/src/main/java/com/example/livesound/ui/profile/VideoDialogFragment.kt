import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.VideoView
import androidx.fragment.app.DialogFragment
import com.example.livesound.R

class VideoDialogFragment : DialogFragment() {

    private lateinit var videoView: VideoView
    private lateinit var playPauseButton: ImageButton
    private lateinit var videoSeekBar: SeekBar
    private lateinit var closeButton: ImageButton
    private var isPlaying = true // Inicialmente está en true para auto-play
    private var isFullScreen = false
    private lateinit var fullScreenButton: ImageButton
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video_dialog, container, false)

        // Inicializa los componentes de la UI
        videoView = view.findViewById(R.id.videoView)
        playPauseButton = view.findViewById(R.id.playPauseButton)
        videoSeekBar = view.findViewById(R.id.videoSeekBar)
        closeButton = view.findViewById(R.id.closeButton)
        fullScreenButton = view.findViewById(R.id.fullScreenButton)

        // Configuración del video (ajusta la URI según tu archivo de video)
        val videoUri = "android.resource://${requireContext().packageName}/${R.raw.billie720}"
        videoView.setVideoPath(videoUri)

        // Iniciar la reproducción automática
        videoView.setOnPreparedListener {
            videoView.start() // Inicia la reproducción
            updateSeekBar() // Comienza a actualizar la barra de progreso
            playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
        }

        // Control del botón de Play/Pause
        playPauseButton.setOnClickListener {
            if (isPlaying) {
                videoView.pause()
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
            } else {
                videoView.start()
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
                updateSeekBar() // Actualiza la barra de progreso mientras el video se reproduce
            }
            isPlaying = !isPlaying
        }

        // Actualizar la posición del video cuando se ajusta manualmente la SeekBar
        videoSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val duration = videoView.duration
                    val newPosition = (duration * progress) / 100
                    videoView.seekTo(newPosition)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        fullScreenButton.setOnClickListener {
            toggleFullScreen()
        }

        // Configurar botón de cerrar
        closeButton.setOnClickListener {
            dismiss() // Cierra el diálogo
        }

        return view
    }

    private fun toggleFullScreen() {
        if (isFullScreen) {
            // Salir de pantalla completa
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else {
            // Ir a pantalla completa
            activity?.window?.decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        isFullScreen = !isFullScreen
    }

    // Función para actualizar la SeekBar en tiempo real
    private fun updateSeekBar() {
        val currentPosition = videoView.currentPosition
        val duration = videoView.duration
        if (duration > 0) {
            val progress = (currentPosition * 100) / duration
            videoSeekBar.progress = progress
        }
        if (isPlaying) {
            handler.postDelayed({ updateSeekBar() }, 1000) // Actualiza cada segundo
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Detener la actualización de la SeekBar
    }
}
