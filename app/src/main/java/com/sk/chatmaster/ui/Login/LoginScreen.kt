package com.sk.chatmaster.ui.Login

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sk.chatmaster.R
import com.sk.chatmaster.core.common.AppEmailTextField
import com.sk.chatmaster.core.common.AppOutlinedTextField
import com.sk.chatmaster.core.common.AppPasswordTextField
import com.sk.chatmaster.core.common.AppPhoneTextField
import com.sk.chatmaster.ui.theme.Pink40
import com.sk.chatmaster.ui.theme.Purple80

val containerColor = Color(0xFFFFFFFF) // Dark gray track
@Composable
fun LoginScreen(navController: NavController?) {

    Box(modifier = Modifier.fillMaxSize()
        .padding(16.dp).background(containerColor),
        contentAlignment = Alignment.Center,) {
        var nameText by remember { mutableStateOf("") }
        var emailText by remember { mutableStateOf("") }
        var passwordText by remember { mutableStateOf("") }
        var mobileNumText by remember { mutableStateOf("") }
        var isSignUp by remember { mutableStateOf(false) }

        val emailTextRequester = remember { FocusRequester() }

        Column() {
            /*Image(
                painter = painterResource(id = R.drawable.ic_signup),
                contentDescription = "Login",
                modifier = Modifier.size(70.dp).align(Alignment.CenterHorizontally)
            )
            Text(text = "Login",
                modifier = Modifier.padding(16.dp).
                align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,

            )*/
            Image(
                painter = painterResource(id = R.drawable.ic_signup),
                contentDescription = "Login",
                modifier = Modifier.size(70.dp).align(Alignment.CenterHorizontally)
            )
            AuthToggleSwitch(
                isSignUpSelected = isSignUp,
                onToggleChanged = { isSignUp = it },
                modifier = Modifier.padding(top = 20.dp)
            )
            Spacer(modifier = Modifier.size(20.dp))

            AppOutlinedTextField(
                value = nameText,
                onValueChange = {newValue -> nameText = newValue},
                label = "Name",
                leadingIcon = Icons.Default.Person,
                singleLine = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
                keyboardActions = KeyboardActions(
                    onNext = {
                        emailTextRequester.requestFocus()
                    }
                )
            )

            AppEmailTextField(
                modifier = Modifier.fillMaxWidth().
                focusRequester(emailTextRequester),
                value = emailText,
                onValueChange = {emailText = it},
                label = "Email",
                leadingIcon = Icons.Default.Email,
                imeAction = ImeAction.Next,
            )
            Spacer(Modifier.size(8.dp))

            AppPasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                value = passwordText,
                label = "Password",
                onValueChange = { password ->
                    passwordText = password
                },
                leadingIcon = Icons.Default.Password,
                imeAction = ImeAction.Next,
            )
            Spacer(Modifier.size(8.dp))
            AppPhoneTextField(modifier = Modifier.fillMaxWidth(),
                value = mobileNumText,
                onValueChange = {
                    mobileNum -> mobileNumText = mobileNum
                },
                label = "Mobile",
                leadingIcon = Icons.Default.PhoneAndroid,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        //
                    }
                )
            )
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(140.dp,48.dp).
                align(Alignment.CenterHorizontally),
                onClick = {

                }
            ) {
                Text("Login")
                Icon(
                    imageVector = Icons.Default.Login,
                    contentDescription = "Login",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(null)
}
