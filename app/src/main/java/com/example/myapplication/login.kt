package com.example.myapplication

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

// Palet warna
private val LatarBelakang = Color(0xFFFFFFFF) // Putih FFFFFF sesuai permintaan
private val KuningTombol = Color(0xFFFFB100)
private val HijauTombolSosial = Color(0xFFD4E9E2)
private val BiruTeks = Color(0xFF3B82F6)
private val AbuPlaceholder = Color(0xFF9CA3AF)
private val AbuGaris = Color(0xFF9CA3AF)
private val HitamTeks = Color(0xFF111827)

enum class LoginViewState {
    Form, ErrorAnim, SuccessAnim
}

@Composable
fun DataMahasiswa() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var viewState by remember { mutableStateOf(LoginViewState.Form) }
    var pesanUsername by remember { mutableStateOf("") }
    var pesanPassword by remember { mutableStateOf("") }

    val usernameBenar = "mahasiswa"
    val passwordBenar = "12345"

    // Riwayat input untuk autosuggest
    val riwayatUsername = remember { mutableStateListOf<String>() }
    val riwayatPassword = remember { mutableStateListOf<String>() }
    var showUsernameSuggest by remember { mutableStateOf(false) }
    var showPasswordSuggest by remember { mutableStateOf(false) }
    // Pasangan kredensial tersimpan setelah login berhasil
    var savedUsername by remember { mutableStateOf("") }
    var savedPassword by remember { mutableStateOf("") }

    Crossfade(
        targetState = viewState,
        animationSpec = tween(durationMillis = 600),
        label = "LoginTransition"
    ) { currentState ->
        when (currentState) {
            LoginViewState.SuccessAnim -> {
                // Tampilan Berhasil
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LatarBelakang)
                        .padding(horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.succesc))
                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(200.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Login Berhasil!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF16A34A)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                viewState = LoginViewState.Form
                                username = ""
                                password = ""
                                pesanUsername = ""
                                pesanPassword = ""
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = KuningTombol)
                        ) {
                            Text("Kembali ke Login", fontWeight = FontWeight.Bold, color = HitamTeks)
                        }
                    }
                }
            }

            LoginViewState.ErrorAnim -> {
                // Tampilan Error Animasi (Tertawa)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LatarBelakang),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.laughingcat))
                    val progress by animateLottieCompositionAsState(
                        composition = composition,
                        iterations = 1,
                        isPlaying = true
                    )

                    // Kembali ke Form saat animasi selesai
                    LaunchedEffect(progress) {
                        if (progress == 1f) {
                            viewState = LoginViewState.Form
                        }
                    }

                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                }
            }

            LoginViewState.Form -> {
                // Tampilan Form Login
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LatarBelakang)
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    // Logo dan Teks Judul
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(120.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Masuk ke Portal Akademik",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    // Field Username dengan Autosuggest
                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = username,
                            onValueChange = { value ->
                                username = value
                                // Autofill password jika username cocok dengan yang tersimpan
                                if (value == savedUsername && savedPassword.isNotEmpty()) {
                                    password = savedPassword
                                }
                                showUsernameSuggest = value.isNotEmpty() && riwayatUsername.any { it.contains(value, ignoreCase = true) }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("Username", color = AbuPlaceholder, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = AbuGaris,
                                unfocusedIndicatorColor = AbuGaris,
                                focusedTextColor = HitamTeks,
                                unfocusedTextColor = HitamTeks
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        DropdownMenu(
                            expanded = showUsernameSuggest,
                            onDismissRequest = { showUsernameSuggest = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            riwayatUsername.filter { it.contains(username, ignoreCase = true) }.forEach { saran ->
                                DropdownMenuItem(
                                    text = { Text(saran) },
                                    onClick = {
                                        username = saran
                                        if (saran == savedUsername && savedPassword.isNotEmpty()) {
                                            password = savedPassword
                                        }
                                        showUsernameSuggest = false
                                    }
                                )
                            }
                        }
                    }

                    if (pesanUsername.isNotEmpty()) {
                        Text(
                            text = pesanUsername,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 11.sp,
                            color = Color(0xFFDC2626)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Field Password dengan Icon Mata + Autosuggest
                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = password,
                            onValueChange = { value ->
                                password = value
                                // Autofill username jika password cocok dengan yang tersimpan
                                if (value == savedPassword && savedUsername.isNotEmpty()) {
                                    username = savedUsername
                                }
                                showPasswordSuggest = value.isNotEmpty() && riwayatPassword.any { it.contains(value, ignoreCase = true) }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("password", color = AbuPlaceholder, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val iconRes = if (passwordVisible)
                                    R.drawable.ic_visibility
                                else
                                    R.drawable.ic_visibility_off
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                        tint = AbuGaris,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = AbuGaris,
                                unfocusedIndicatorColor = AbuGaris,
                                focusedTextColor = HitamTeks,
                                unfocusedTextColor = HitamTeks
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        DropdownMenu(
                            expanded = showPasswordSuggest,
                            onDismissRequest = { showPasswordSuggest = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            riwayatPassword.filter { it.contains(password, ignoreCase = true) }.forEach { saran ->
                                DropdownMenuItem(
                                    text = { Text("••••••") },
                                    onClick = {
                                        password = saran
                                        if (saran == savedPassword && savedUsername.isNotEmpty()) {
                                            username = savedUsername
                                        }
                                        showPasswordSuggest = false
                                    }
                                )
                            }
                        }
                    }

                    if (pesanPassword.isNotEmpty()) {
                        Text(
                            text = pesanPassword,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 11.sp,
                            color = Color(0xFFDC2626)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Forgot Password
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        Text(
                            text = "Forgot password?",
                            color = BiruTeks,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { /* Handle forgot password */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Tombol Login
                    Button(
                        onClick = {
                            pesanUsername = ""
                            pesanPassword = ""
                            // Simpan ke riwayat autosuggest
                            if (username.isNotEmpty() && !riwayatUsername.contains(username)) riwayatUsername.add(username)
                            if (password.isNotEmpty() && !riwayatPassword.contains(password)) riwayatPassword.add(password)
                            if (username.isEmpty()) {
                                pesanUsername = "Username harus diisi."
                            }
                            if (password.isEmpty()) {
                                pesanPassword = "Password harus diisi."
                            }
                            if (username.isNotEmpty() && password.isNotEmpty()) {
                                if (username != usernameBenar && password != passwordBenar) {
                                    pesanUsername = "Username salah"
                                    pesanPassword = "Password salah"
                                    viewState = LoginViewState.ErrorAnim
                                } else if (username != usernameBenar) {
                                    pesanUsername = "Username salah"
                                    viewState = LoginViewState.ErrorAnim
                                } else if (password != passwordBenar) {
                                    pesanPassword = "Password salah"
                                    viewState = LoginViewState.ErrorAnim
                                } else {
                                    // Simpan kredensial berhasil untuk autofill
                                    savedUsername = username
                                    savedPassword = password
                                    viewState = LoginViewState.SuccessAnim
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KuningTombol)
                    ) {
                        Text(
                            text = "Log in",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = HitamTeks
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Tombol Log in with Google - posisi center antara Log in dan teks footer
                    Button(
                        onClick = { viewState = LoginViewState.SuccessAnim },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HijauTombolSosial),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(36.dp))
                            Text(
                                text = "Log in with Google",
                                color = Color(0xFF374151),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = "Google Icon",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Footer nama mahasiswa
                    val annotatedText = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFF6B7280))) {
                            append("Rendi Asyari | ")
                        }
                        withStyle(style = SpanStyle(color = KuningTombol, fontWeight = FontWeight.SemiBold)) {
                            append("Teknik Informatika")
                        }
                    }
                    Text(
                        text = annotatedText,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }
            }
        }
    }
}