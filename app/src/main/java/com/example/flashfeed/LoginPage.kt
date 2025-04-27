//package com.example.flashfeed
//// changed
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import com.example.flashfeed.ui.theme.FlashFeedTheme
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.tooling.preview.Preview
//
//class LoginPage : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            FlashFeedTheme {
//                LoginPageContent()
//            }
//        }
//    }
//}
//
//
//@Composable
//fun LoginPageContent() {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    val context = LocalContext.current
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(250.dp)
//                .background(Color(0xFF2ECC71)),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Image(
//                    painter = painterResource(id = R.drawable.logo_white),
//                    contentDescription = "Logo",
//                    modifier = Modifier.size(80.dp)
//                )
//                Text(
//                    text = "Flash Feed",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .clip(RoundedCornerShape(topStart = 50.dp))
//                .background(Color.White)
//                .padding(24.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    text = "Login",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Row {
//                    Text(text = "Don't have an Account? ")
//                    Text(
//                        text = "Sign Up",
//                        color = Color(0xFF2ECC71),
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.clickable {
//                            val intent = Intent(context, SignupPage::class.java)
//                            context.startActivity(intent)
//                        }
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                OutlinedTextField(
//                    value = username,
//                    onValueChange = { username = it },
//                    label = { Text("Username") },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp)
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = { Text("Password") },
//                    visualTransformation = PasswordVisualTransformation(),
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp)
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Button(
//                    onClick = {
//                        val intent = Intent(context, MainActivity::class.java)
//                        context.startActivity(intent)
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp),
//                    shape = RoundedCornerShape(25.dp),
//                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2ECC71))
//                ) {
//                    Text(text = "Submit", fontSize = 18.sp, color = Color.White)
//                }
//
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun LoginPagePreview() {
//    LoginPage()
//}
