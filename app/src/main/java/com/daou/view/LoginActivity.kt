package com.daou.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daou.R
import com.daou.databinding.ActivityLoginBinding
import com.daou.repository.RemoteRepository
import com.daou.viewmodel.LoginViewModel
import com.daou.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.apply {
            lifecycleOwner = this@LoginActivity
        }

        val repository = RemoteRepository()
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.vm = viewModel

        viewModel.emptyLoginData.observe(this, Observer {
            AlertDialog.Builder(this@LoginActivity)
                .setTitle("로그인 실패")
                .setMessage("아이디와 비밀번호를 모두 입력해주세요!")
                .create()
                .show()
        })

        viewModel.failedLogin.observe(this, Observer {
            AlertDialog.Builder(this@LoginActivity)
                .setTitle("로그인 실패")
                .setMessage("아이디와 비밀번호를 다시 확인해주세요!")
                .create()
                .show()
        })

        viewModel.errorMessage.observe(this, Observer {
            AlertDialog.Builder(this@LoginActivity)
                .setTitle("네트워크 에러")
                .setMessage("와이파이 혹은 데이터를 확인해주세요")
                .create()
                .show()
        })

        viewModel.successLogin.observe(this, Observer {
            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }
}