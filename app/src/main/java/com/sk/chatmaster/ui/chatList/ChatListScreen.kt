package com.sk.chatmaster.ui.chatList

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.sk.chatmaster.navigation.Route
import com.sk.chatmaster.ui.Chat.ChatScreen
import com.sk.chatmaster.ui.Login.CircularProgressComponent
import com.sk.chatmaster.ui.theme.Blue40

private val toolBarBackground = Color(0xFF5B4FE9)
private val toolBarTitleColor = Color(0xFF5B4FE9)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController?,
                   /*onUserClick : (receiverID: String,receiverName : String)-> Unit = { _, _ -> },*/) {
    val viewModel : ChatListViewModel = hiltViewModel()
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Chats",
                        fontSize = 24.sp,
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
                    if (state.userList.isEmpty()) {
                        Text(
                            text = "No chat users found.",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        LazyColumn(Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        ) {
                            items(state.userList) { chat ->
                                //chatItem(chat,onClick = { onUserClick(chat.uid,chat.name) })
                                chatItem(chat,navController/*,onClick = {
                                    navController?.navigate(ChatScreen(navController,
                                    AppConfig.UID,chat.uid,chat.name)) }*/)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun chatItem(chatUser: ChatUser/*, onClick: () -> Unit = {}*/,navController: NavController?) {
    Card(modifier = Modifier.fillMaxWidth()
        .clickable { navController?.navigate("ChatScreen/${chatUser.uid}/${chatUser.name}") },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            if (chatUser.icon.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Blue40, RoundedCornerShape(30.dp))
                ) {
                    Text(text = chatUser.name!!.first().toString(),
                        fontSize = 32.sp,
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
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chatUser.name!!,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chatUser.mobile!!,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top) {
                Text(
                    text = "5 mins",
                    fontSize = 8.sp,
                )
                /*Box(modifier = Modifier
                    .background(toolBarBackground, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 10.dp)) {
                    Text(
                        text = "5",
                        fontSize = 8.sp,
                        color = Color.White
                    )

                }*/
                Box(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun showPreview() {
    var userList = listOf<ChatUser>(
        ChatUser("sdfjasjdfjsdflj","Vijay","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Mathu","sudhakar@gmail.com","9095655761","","",""),
        ChatUser( "sdfjasjdfjsdflj","Tamizhan","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Baskaran","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Karthik","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Ramamoorthy","sudhakar@gmail.com","9095655761","","",""),
        ChatUser("sdfjasjdfjsdflj","Varatharaj","sudhakar@gmail.com","9095655761","","",""))
    LazyColumn(Modifier
        .padding(16.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    ) {
        items(userList) { chat ->
            chatItem(chat,null)
        }
    }
    //ChatListScreen(null)
}