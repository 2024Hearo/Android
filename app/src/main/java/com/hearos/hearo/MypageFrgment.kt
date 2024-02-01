import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.hearos.hearo.R

class MypageFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FirebaseUser 객체
        val user = FirebaseAuth.getInstance().currentUser

        // 이메일 주소 적용
        if (user != null) {
            val email = user.email
            val emailTextView = view.findViewById<TextView>(R.id.userEmail)
            emailTextView.text = email


            val icPlusButton = view.findViewById<Button>(R.id.ic_plus)
            icPlusButton.setOnClickListener {
                // 새 팝업 창을 만듭니다
                val popup = PopupWindow(activity)
                val inflater: LayoutInflater = LayoutInflater.from(context)
                val popupView = inflater.inflate(R.layout.popup_mypage, null)

                popup.contentView = popupView
                popup.width = ViewGroup.LayoutParams.WRAP_CONTENT
                popup.height = ViewGroup.LayoutParams.WRAP_CONTENT
                popup.isFocusable = true
                popup.showAtLocation(view, Gravity.CENTER, 0, 0)
            }

        }
    }
}
