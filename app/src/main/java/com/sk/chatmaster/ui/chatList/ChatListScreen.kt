package com.sk.chatmaster.ui.chatList

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sk.chatmaster.R
import com.sk.chatmaster.core.common.AppConfig
import com.sk.chatmaster.data.model.ChatUser
import com.sk.chatmaster.ui.Login.CircularProgressComponent
import com.sk.chatmaster.ui.theme.Blue40

private val toolBarBackground = Color(0xFF5B4FE9)
private val toolBarTitleColor = Color(0xFF5B4FE9)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    viewModel : ChatListViewModel = hiltViewModel(),
    navController: NavController?,
    onUserClick: (String, String) -> Unit) {
    val context = LocalContext.current
    //val viewModel : ChatListViewModel = hiltViewModel()
    val uiState by viewModel._chatListState.collectAsState()
    /*var userList = listOf<ChatUser>(ChatUser(chatUserID = "sdfjasjdfjsdflj", "Sudhakar","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Vijay","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Mathu","sudhakar@gmail.com","9095655761","","",""),
        ChatUser( "sdfjasjdfjsdflj","Tamizhan","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Baskaran","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Karthik","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Ramamoorthy","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Varatharaj","sudhakar@gmail.com","9095655761","","",""))*/
    // Trigger fetch once when the screen is first shown (or when uid changes)
    viewModel.loadChatUsers(AppConfig.UID)
    var searchQry by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Chats",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .clickable(
                            onClick = {},
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }),
                        contentAlignment = Alignment.Center) {
                        IconButton(onClick = {}) {
                            Icon(
                                //imageVector = Icons.Filled.ArrowBack,
                                painter = painterResource(id = R.drawable.ic_menu_bar),
                                contentDescription = null
                            )
                        }
                    }
                }

            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = innerPadding)) {
            /*Column(Modifier.fillMaxSize().padding(16.dp)) {

            }*/
            when(val state = uiState) {
                is ChatUserListUiState.Loading -> {
                    CircularProgressComponent()
                }

                is ChatUserListUiState.Failure -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Button (
                            onClick = { viewModel.loadChatUsers(AppConfig.UID) }
                        ) {
                            Text("Retry")
                        }
                    }
                }
                is ChatUserListUiState.Success -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center)) {
                        if (state.userList.isEmpty()) {
                            Text(
                                text = "No chat users found.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {

                            val filteredUsers = remember(searchQry, state.userList) {
                                if(searchQry.isBlank()) {
                                    state.userList
                                } else {
                                    state.userList.filter { user ->
                                        user.name.contains(searchQry, ignoreCase = true)
                                                || user.mobile?.contains(searchQry, ignoreCase = true) == true
                                                || user.email?.contains(searchQry, ignoreCase = true) == true
                                    }
                                }
                            }
                            OutlinedTextField(value = searchQry,
                                onValueChange = {search ->
                                    searchQry = search
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp, 8.dp),
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Search,
                                        contentDescription = null)
                                },
                                placeholder = {
                                    Text("Search")
                                },
                                shape = RoundedCornerShape(28.dp),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFFFFFFF),
                                    //unfocusedTextColor = Color(0xFFF2F2F2),
                                    disabledContainerColor = Color(0xFFFFFFFF),

                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ))
                            LazyColumn(Modifier
                                .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                            ) {
                                items(filteredUsers) { chat ->
                                    //chatItem(chat,onClick = { onUserClick(chat.uid,chat.name) })
                                    ChatItem(chat,navController,onUserClick = onUserClick)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    BackHandler(enabled = true) {

        (context as Activity)?.finishAffinity()
    }
}

@Composable
fun ChatItem(chatUser: ChatUser, navController: NavController?,
             onUserClick: (String, String) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onUserClick(chatUser.uid,chatUser.name)
                    //navController?.navigate("ChatScreen/${chatUser.uid}/${chatUser.name}")
                   },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 12.dp)
            /*.clickable { navController?.navigate("ChatScreen/${chatUser.uid}/${chatUser.name}") }*/,
            verticalAlignment = Alignment.CenterVertically) {
            if (chatUser.icon.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Blue40, RoundedCornerShape(30.dp))
                ) {
                    Text(text = chatUser.name!!.first().toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                    )
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_signup),
                    contentDescription = "User Image",
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chatUser.name!!,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chatUser.mobile!!,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
            /*Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top) {
                Text(
                    text = "5 mins",
                    fontSize = 8.sp,
                )
                *//*Box(modifier = Modifier
                    .background(toolBarBackground, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 10.dp)) {
                    Text(
                        text = "5",
                        fontSize = 8.sp,
                        color = Color.White
                    )

                }*//*
                Box(modifier = Modifier.height(30.dp))
            }*/
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ShowPreview() {
    var userList = listOf<ChatUser>(
        ChatUser("sdfjasjdfjsdflj","Vijay","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Mathu","sudhakar@gmail.com","9095655761","","",""),
        ChatUser( "sdfjasjdfjsdflj","Tamizhan","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Baskaran","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Karthik","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Ramamoorthy","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Varatharaj","sudhakar@gmail.com","9095655761","","",""))

    Column() {
        var searchQry by remember { mutableStateOf("") }
        OutlinedTextField(value = searchQry,
            onValueChange = {search ->
                searchQry = search
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search,
                    contentDescription = null)
            },
            placeholder = {
                Text("Search")
            },
            shape = RoundedCornerShape(28.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFFFFFF),
                //unfocusedTextColor = Color(0xFFF2F2F2),
                disabledContainerColor = Color(0xFFFFFFFF),

                focusedIndicatorColor = Color(0xFFFFFFFF),
                unfocusedIndicatorColor = Color(0xFFFFFFFF),
                disabledIndicatorColor = Color(0xFFFFFFFF)
            ))
        val filterList = userList.filter { user ->
            user.name.contains(searchQry, ignoreCase = true)
        }

        LazyColumn(Modifier
            .fillMaxSize()
            .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        ) {
            items(filterList) { chat ->
                ChatItem(chat,null, { receiverId, receiverName ->  })
            }
        }
    }
}