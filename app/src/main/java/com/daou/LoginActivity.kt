package com.daou

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.daou.databinding.ActivityLoginBinding
import com.daou.model.RequestLogin
import com.daou.model.ResponseLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.exitProcess

// todo OkHttp 같이 사용해야함!

class LoginActivity : AppCompatActivity() {
    private val requestToServer = RequestToServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            if (binding.idEditText.text.isNullOrBlank() || binding.passwordEditText.text.isNullOrBlank()) {
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                requestToServer.service.requestLogin(
                    RequestLogin(
                        username = binding.idEditText.text.toString(),
                        password = binding.passwordEditText.text.toString()
                    ) // 로그인 정보를 전달
                ).enqueue(object : Callback<ResponseLogin> {
                    override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                        Log.d("통신 실패", "${t.message}")
                        Toast.makeText(this@LoginActivity, "통신실패", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<ResponseLogin>,
                        response: Response<ResponseLogin>,
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()!!.code == "200") {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java));
                                //finish()
                            } else {
                                Toast.makeText(this@LoginActivity,"아이디와 비밀번호를 확인해주세요", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            AlertDialog.Builder(this@LoginActivity)
                                .setTitle("로그인 실패")
                                .setMessage("ID/PW를 다시 입력해 주세요")
                                .create()
                                .show()
                        }
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finishAffinity()
        System.runFinalization()
        exitProcess(0)
    }
}
