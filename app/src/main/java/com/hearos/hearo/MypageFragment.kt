import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.hearos.hearo.R
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.net.VpnService.prepare
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.NonCancellable.start

class MypageFragment: Fragment() {

    private lateinit var fileChooserLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fileChooserLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // 여기서 파일 업로드 로직을 호출
                uploadSoundFile(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val emailTextView = view.findViewById<TextView>(R.id.userEmail)
            emailTextView.text = user.email
        }

        // 파일 선택 버튼 리스너 설정
        val addSoundButton = view.findViewById<Button>(R.id.ic_plus)
        addSoundButton.setOnClickListener {
            // "audio/*" MIME 타입을 사용하여 오디오 파일만 선택할 수 있도록 설정
            fileChooserLauncher.launch("audio/*")
        }

        // Play Sound 버튼 클릭 이벤트 처리
        view.findViewById<Button>(R.id.btn_emerancy).setOnClickListener {
            // 재생할 음원 파일의 이름 또는 경로
            val fileName = "emergancy.mp3"
            playSound(fileName)
        }
    }

    private fun uploadSoundFile(uri: Uri) {
        val fileName = uri.lastPathSegment ?: "unknown"
        val storageRef = FirebaseStorage.getInstance().reference.child("sounds/$fileName")
        storageRef.putFile(uri).addOnSuccessListener {
            // 업로드 성공 시, 메타데이터를 Realtime Database에 저장하는 로직 추가
            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->

            }
        }.addOnFailureListener {
            // 업로드 실패 처리
        }
    }

    private fun playSound(fileName: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("sounds/$fileName")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(requireContext(), uri) // getContext()를 사용하여 Context를 전달
                prepareAsync() // 비동기로 준비
                setOnPreparedListener {
                    start() // 준비가 완료되면 재생 시작
                }
            }
        }.addOnFailureListener {
            // URL 가져오기 실패 처리
        }
    }



}