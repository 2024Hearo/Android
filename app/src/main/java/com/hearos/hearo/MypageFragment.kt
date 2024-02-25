import android.app.ProgressDialog
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.hearos.hearo.R
import com.hearos.hearo.VoiceActivity1
import com.hearos.hearo.dto.SoundRes
import com.hearos.hearo.utils.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MypageFragment : Fragment() {

    private lateinit var mediaPlayer: MediaPlayer

    private suspend fun PlaySound(filename:String) : String {
        return RetrofitService.MypageApi.getAudioFileUrl(filename = filename )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 사용자 이메일 표시
        FirebaseAuth.getInstance().currentUser?.let { user ->
            view.findViewById<TextView>(R.id.userEmail).text = user.email
        }

        // 오디오 재생 버튼 리스너 설정
        val btnEmerancy: LinearLayout = view.findViewById(R.id.btn_emerancy)
        btnEmerancy.setOnClickListener {
            val fileName = "help.mp3"
            playSound(fileName)
        }


        val btnWait: LinearLayout = view.findViewById(R.id.btn_wait)
        btnWait.setOnClickListener {
            val fileName = "wait.mp3"
            playSound(fileName)
        }

        val btnProb: LinearLayout = view.findViewById(R.id.btn_hear) // 올바른 변수명으로 변경
        btnProb.setOnClickListener {
            // MediaPlayer를 이용하여 raw 폴더 내의 problem.mp3 파일 재생
            val mediaPlayer = MediaPlayer.create(context, R.raw.problem)
            mediaPlayer.start() // 사운드 재생 시작

            mediaPlayer.setOnCompletionListener {
                // 재생이 완료되면 MediaPlayer 리소스 해제
                it.release()
            }
        }


        // "btn_plus_voice" 버튼에 대한 클릭 리스너 설정
        val btnPlusVoice: ImageView = view.findViewById(R.id.ic_plus_voice)
        btnPlusVoice.setOnClickListener {
            // VoiceActivity로 이동
            val intent = Intent(activity, VoiceActivity1::class.java)
            startActivity(intent)
        }
    }

    //소리
    private fun playSound(fileName: String) {
        val progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Loading...")
            isIndeterminate = true
            setCancelable(false)
            show()
        }

        Log.d("MypageFragment", "playSound called with fileName: $fileName")

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val url = withContext(Dispatchers.IO) { PlaySound(fileName) }
                if (url.isNotEmpty()) {
                    playAudioFromUrl(url)
                    Log.d("MypageFragment", "Playing sound from URL: $url")
                } else {
                    Log.d("MypageFragment", "URL is empty")
                }
            } catch (e: Exception) {
                Log.e("MypageFragment", "Error fetching audio file: ${e.localizedMessage}")
            } finally {
                progressDialog.dismiss()
            }
        }
    }

    private fun playAudioFromUrl(url: String) {
        if (::mediaPlayer.isInitialized) mediaPlayer.release()

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepareAsync() // 비동기 준비
            setOnPreparedListener { start() } // 준비가 되면 시작
            setOnCompletionListener {
                Toast.makeText(requireContext(), "재생 완료", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
