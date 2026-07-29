package com.sk.chatmaster.ui.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sk.chatmaster.R
import com.sk.chatmaster.core.common.AppEmailTextField
import com.sk.chatmaster.core.common.AppOutlinedTextField
import com.sk.chatmaster.core.common.AppPasswordTextField
import com.sk.chatmaster.core.common.AppPhoneTextField
import com.sk.chatmaster.ui.theme.Blue40
import com.sk.chatmaster.ui.widget.BottomSheetDialog
import com.sk.chatmaster.ui.widget.StatusDialog

val containerColor = Color(0xFFFFFFFF) // Dark gray track
@Composable
fun LoginScreen(loginViewModel : LoginViewModel = hiltViewModel(),
                navController: NavController?) {
    val authState by loginViewModel.loginState.collectAsState()
    val dialogState by loginViewModel.dialogState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val scrollState : ScrollState = rememberScrollState()

    LaunchedEffect(authState.error) {
        authState.error?.let {
            snackbarHostState.showSnackbar(it,
                duration = SnackbarDuration.Short)
            loginViewModel.clearError()
        }
    }

    StatusDialog(
        dialogState,
        onDismiss = { route ->
            loginViewModel.dismissDialog()

            // 2. Safely check flag and trigger navigation
            if (!route.isNullOrEmpty()) {
                navController?.navigate(route) {
                    // Optional: Clear backstack so user cannot back into the dialog flow
                    popUpTo("LoginScreen") { inclusive = true }
                }
            }
        })
    //BottomSheetDialog(false,"",)
    Box(modifier = Modifier
        .fillMaxSize()
        .background(containerColor)
        .verticalScroll(scrollState),
        contentAlignment = Alignment.Center,) {
        var nameText by remember { mutableStateOf("") }
        var emailText by remember { mutableStateOf("") }
        var passwordText by remember { mutableStateOf("") }
        var mobileNumText by remember { mutableStateOf("") }
        var isSignUp by remember { mutableStateOf(false) }
        val emailTextRequester = remember { FocusRequester() }

        SnackbarHost(hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter))

        if (authState.isLoading) {
            CircularProgressComponent()
        }

        Column(Modifier
            .padding(16.dp)) {
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
                painter = painterResource(id = R.drawable.ic_account),
                contentDescription = "Login",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
            )
            AuthToggleSwitch(
                isSignUpSelected = authState.isSignUp,
                onToggleChanged = { loginViewModel.loginEvent(AuthUIEvent.SetSignUp(it)) },
                modifier = Modifier.padding(top = 20.dp)
            )
            Spacer(modifier = Modifier.size(20.dp))
            if (authState.isSignUp) {
                AppOutlinedTextField(
                    value = authState.name ?: "",
                    onValueChange = {newValue -> loginViewModel.registerEvent(AuthUIEvent.NameChanged(newValue))},
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
                Spacer(Modifier.size(8.dp))
                AppPhoneTextField(modifier = Modifier.fillMaxWidth(),
                    value = authState.mobile ?: "",
                    onValueChange = { mobileNum ->
                        //mobileNumText = mobileNum
                        loginViewModel.registerEvent(AuthUIEvent.MobileChanged(mobileNum))
                    },
                    label = "Mobile",
                    leadingIcon = Icons.Default.PhoneAndroid,
                    imeAction = ImeAction.Next,
                )
                Spacer(Modifier.size(8.dp))
            }
            AppEmailTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailTextRequester),
                value = authState.email ?: "",
                onValueChange = {loginViewModel.registerEvent(AuthUIEvent.EmailChanged(it))},
                label = "Email",
                leadingIcon = Icons.Default.Email,
                imeAction = ImeAction.Next,
            )
            Spacer(Modifier.size(8.dp))
            AppPasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                value = authState.password ?: "",
                label = "Password",
                onValueChange = { password ->
                    loginViewModel.registerEvent(AuthUIEvent.PasswordChaned(password))
                },
                leadingIcon = Icons.Default.Password,
                imeAction = if (authState.isSignUp) ImeAction.Next else ImeAction.Done,
                keyboardActions = KeyboardActions (
                    onDone = {
                        focusManager.clearFocus()
                        if (!authState.isSignUp)//login
                            loginViewModel.loginEvent(AuthUIEvent.submitOnClick)
                    }
                )
            )
            if (authState.isSignUp) {
                Spacer(Modifier.size(8.dp))
                AppPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = authState.confirmPassword ?: "",
                    label = "Confirm Password",
                    onValueChange = { confirmPass ->
                        loginViewModel.registerEvent(AuthUIEvent.ConfirmPasswordChaned(confirmPass))
                    },
                    leadingIcon = Icons.Default.Password,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            loginViewModel.registerEvent(AuthUIEvent.submitOnClick)
                        }
                    )
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .size(140.dp, 48.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(Blue40),
                onClick = {
                    if (authState.isSignUp) {
                        loginViewModel.registerEvent(AuthUIEvent.submitOnClick)
                    } else {
                        loginViewModel.loginEvent(AuthUIEvent.submitOnClick)
                    }
                }
            ) {
                if (authState.isSignUp) {
                    Text("Sign-Up")
                    Icon(
                        imageVector = Icons.Default.ManageAccounts,
                        contentDescription = "Sign-Up",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                } else {
                    Text("Login")
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = "Login",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}
@Composable
fun CircularProgressComponent() {
    // CircularProgressIndicator is generally used
    // at the loading screen and it indicates that
    // some progress is going on so please wait.
    Column(
        // we are using column to align our
        // imageview to center of the screen.
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .alpha(0.5f),

        // below line is used for specifying
        // vertical arrangement.
        verticalArrangement = Arrangement.Center,

        // below line is used for specifying
        // horizontal arrangement.
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // below line is used to display
        // a circular progress bar.
        CircularProgressIndicator(
            // below line is used to add padding
            // to our progress bar.
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp),

            // below line is used to add color
            // to our progress bar.
            color = Blue40,

            // below line is used to add stroke
            // width to our progress bar.
            strokeWidth = 8.dp,

            // below line is used to add track
            // color to our progress bar.
            trackColor = Color.LightGray,

            // below line is used to add strokeCap
            // to our progress bar.
            strokeCap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(navController = null)
}
