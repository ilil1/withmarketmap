package com.example.YUmarket.screen.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.YUmarket.R
import com.example.YUmarket.databinding.ActivitySignupBinding
import com.example.YUmarket.screen.base.BaseActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class SignUpActivity : BaseActivity<ActivitySignupBinding>() {


    private var checkEye =0
    var number: String = ""
    private var doubleBackToExit = false

    //create instance of firebae auth
    lateinit var auth: FirebaseAuth

    // get storedVerificationId
    //we will use this match the sent otp from firebase
    private var checkPhone = 0

    //firebase에서 보낼 otp
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()


        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                checkPhone = 1
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@SignUpActivity, "Error Phone Message$e", Toast.LENGTH_LONG)
                    .show()
            }

            //On code is sent by the firebase this method is called
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("Msg", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
            }

        }

        binding.confirmButton.setOnClickListener {
            val otp = binding.confirm.text.toString()
            if (otp.isNotEmpty()) {
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId, otp
                )
                binding.confirmButton.setBackgroundResource(R.drawable.radius_btn_onclick)
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(this@SignUpActivity, "Enter CheckNumer", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun getViewBinding(): ActivitySignupBinding =
        ActivitySignupBinding.inflate(layoutInflater)

    override fun observeData() {}

    override fun initViews() = with(binding) {
        super.initViews()
        // auth
        auth = FirebaseAuth.getInstance()



        binding.confirm.visibility = View.INVISIBLE
        binding.confirmButton.visibility = View.INVISIBLE

        binding.signBtn.setOnClickListener {

            if (checkPhone == 1) {
                performRegister()
            } else {
                Toast.makeText(this@SignUpActivity, "전화번호 인증을 하지않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        sendBtn.setOnClickListener {
            binding.sendBtn.setBackgroundResource(R.drawable.radius_btn_onclick)
            sendotp()
            val phone = editphone.text.toString()
            if (phone.isNotEmpty()) {
                confirm.visibility = View.VISIBLE
                confirmButton.visibility = View.VISIBLE
            } else {
                Toast.makeText(this@SignUpActivity, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        //callback functuon for Phone Auth


        back.setOnClickListener {
            back()
        }

        eye.setOnClickListener {
            showAndHide()
        }

    }

    private fun showAndHide(){
        if(checkEye == 0){
            binding.editpassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.eye.setImageResource(R.drawable.eyes_show)
            checkEye = 1
        }else{
            binding.editpassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.eye.setImageResource(R.drawable.eyes)
            checkEye = 0
        }
    }

    //verifies if the code matches sent by firebase
    //if success start the new activity in our case we move LoginActivity

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "SMS인증이 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    checkPhone = 1
                    binding.signBtn.setBackgroundResource(R.drawable.radius_btn)
                } else {
                    //Sign in failed
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }


    private fun performRegister() {
        val email = binding.editEmail.text.toString()
        val password = binding.editpassword.text.toString()
        val username = binding.editname.text.toString()
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "이메일,비밀번호,이름 중에 비어져있는게 있습니다.", Toast.LENGTH_SHORT).show()
        }

        Log.d("SignUpFragment", "Email is " + email)
        Log.d("SignUpFragment", "password is + $password")


        // create a user with firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                // else if sucessful
                Log.d("Sing", "Successfully created user with uid: ${it.result.user?.uid}")
                Toast.makeText(this, "회원가입이 정상적으로 완료되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Log.d("Sign", "Faild to create user: ${it.message}")
                Toast.makeText(this, "Faild to create user: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun sendotp() {
        number = binding.editphone.text.toString()

        //get the phone number from edit text and append the country code
        if (number.isNotEmpty()) {
            number = "+82$number"
            sendVerificationCode(number)
        } else {
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_LONG).show()
        }
    }

    //this method sends the verification code
    //and starts the callback of verification
    //which is implemented above in onCreate

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("MGS", "Auth started")
    }

    private fun back() {
        //startActivity(Intent(this,LoginActivity::class.java))
        this.finish()
    }



    private fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }


//    companion object {
//        const val TAG = "SingUpFragment"
//
//        fun newInstance(): SignUpActivity {
//            return SignUpActivity().apply {
//
//            }
//        }
//    }
}