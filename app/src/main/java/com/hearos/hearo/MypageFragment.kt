import android.app.ProgressDialog
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import android.widget.Toast
import com.hearos.hearo.R
import com.hearos.hearo.VoiceActivity1


class MypageFragment : Fragment() {

    private lateinit var mediaPlayer: MediaPlayer

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
            val fileName = "sound1.mp3"
            playSound(fileName)
        }

        val btnWait: LinearLayout = view.findViewById(R.id.btn_wait)
        btnWait.setOnClickListener {
            val fileName = "sound2.mp3"
            playSound(fileName)
        }

        val btnHear: LinearLayout = view.findViewById(R.id.btn_hear)
        btnHear.setOnClickListener {
            val fileName = "sound3.mp3"
            playSound(fileName)
        }

        // "btn_plus_voice" 버튼에 대한 클릭 리스너 설정
        val btnPlusVoice: ImageView = view.findViewById(R.id.ic_plus_voice)
        btnPlusVoice.setOnClickListener {
            // VoiceActivity로 이동하기 위한 Intent 생성
            val intent = Intent(activity, VoiceActivity1::class.java)
            startActivity(intent)
        }
    }


    private fun playSound(fileName: String) {
        val storageRef = FirebaseStorage.getInstance("gs://hearo-2024").reference.child("sound/$fileName")
        val progressDialog = ProgressDialog(context).apply {
            setMessage("로딩 중...")
            setCancelable(false) // 뒤로가기 버튼 등으로 취소 불가능하게 설정
            show() // 다이얼로그 표시
        }

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            if (::mediaPlayer.isInitialized) mediaPlayer.release() // 기존 MediaPlayer가 초기화되어 있으면 먼저 해제

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(requireContext(), uri)
                prepareAsync() // 비동기 준비
                setOnPreparedListener {
                    progressDialog.dismiss() // 준비가 완료되면 로딩 다이얼로그를 닫음
                    start() // 재생 시작
                }
                setOnCompletionListener {
                    // 재생이 완료되면 "재생 성공" 메시지 표시
                    Toast.makeText(context, "재생 성공", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Error loading file: ${exception.message}")
            Toast.makeText(context, "오디오 파일 로드 실패: ${exception.message}", Toast.LENGTH_LONG).show()
        }

    }


    private fun prepareMediaPlayer(uri: Uri) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(requireContext(), uri)
            prepareAsync()
            setOnPreparedListener { start() } // 준비가 완료되면 재생 시작
        }
    }

    override fun onStop() {
        super.onStop()
        // mediaPlayer가 초기화되었는지 확인
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release() // MediaPlayer 리소스 해제
        }
    }
}