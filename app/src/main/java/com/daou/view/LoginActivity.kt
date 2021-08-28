package com.daou.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daou.databinding.ActivityLoginBinding
import com.daou.data.LoginRequestData
import com.daou.repository.Repository
import com.daou.viewmodel.LoginViewModel
import com.daou.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Repository()
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        putLoginData()
    }

    private fun putLoginData(){
        binding.loginButton.setOnClickListener {
            if (binding.idEditText.text.isNullOrBlank() || binding.passwordEditText.text.isNullOrBlank()) {
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                viewModel.requestLogin(
                    LoginRequestData(
                        username = binding.idEditText.text.toString(),
                        password = binding.passwordEditText.text.toString()
                    )
                )

                viewModel.putLoginResponse.observe(this, Observer {
                    if (it?.body()?.code.toString() == "200"
                        && it?.body()?.message.toString() == "OK"
                        && it?.body()?.goChecksum.toString() == "true"
                    ) {
                        Toast.makeText(this, "로그인 정보 일치!!", Toast.LENGTH_LONG).show()
                        getLoginData()
                    } else {
                        AlertDialog.Builder(this@LoginActivity)
                            .setTitle("로그인 실패")
                            .setMessage("ID/PW를 다시 입력해 주세요")
                            .create()
                            .show()
                    }
                })
            }
        }
    }

    private fun getLoginData() {
        viewModel.requestLoginResult()

        viewModel.getLoginResponse.observe(this, Observer {
            if (it?.body()?.code.toString() == "200"
                && it?.body()?.message.toString() == "OK"
                && it?.body()?.goChecksum.toString() == "true"
                && it?.body()?.name.toString() == "null"
            ) {
                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            } else {
                AlertDialog.Builder(this@LoginActivity)
                    .setTitle("로그인 실패")
                    .setMessage("ID/PW를 다시 입력해 주세요")
                    .create()
                    .show()
            }
        })
    }
}
