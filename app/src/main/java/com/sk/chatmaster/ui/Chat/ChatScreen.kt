package com.sk.chatmaster.ui.Chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sk.chatmaster.core.common.AppConfig
import com.sk.chatmaster.data.model.Message
import com.sk.chatmaster.data.model.MessageType
import com.sk.chatmaster.ui.Login.CircularProgressComponent
import java.text.SimpleDateFormat
import java.util.Locale

private val BubbleSent     = Color(0xFF6C63FF)   // purple — sent messages
private val BubbleReceived = Color(0xFFFFFFFF)   // white  — received messages
private val ScreenBg       = Color(0xFFF0F2FF)   // soft lavender background
private val InputBg        = Color(0xFFFFFFFF)
private val TopBarBg       = Color(0xFFFFFFFF)
private val TimeColor      = Color(0xFF9E9E9E)
private val DateChipBg     = Color(0xFFE0E0E0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController : NavController?,
               receiverId: String,
               receiverName: String,
               viewModel: ChatViewModel = hiltViewModel()
               ) {
    val uiState     by viewModel.uiState.collectAsState()
    val listState   = rememberLazyListState()
    var messageText by remember { mutableStateOf("") }

    // Init ViewModel with both UIDs on first composition
    LaunchedEffect(AppConfig.UID, receiverId) {
        viewModel.init(AppConfig.UID, receiverId)
    }
    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            val count = (uiState as UiState.Success).message.size
            if (count > 0) listState.animateScrollToItem(count - 1)
        }
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TopBarBg),
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(
                        text       = receiverName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 18.sp
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Info, contentDescription = "Info")
                    }
                }
            )
        },
        bottomBar = {
            ChatInputBar(
                value         = messageText,
                onValueChange = { messageText = it },
                onSendClick   = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendTextMessage(messageText.trim())
                        messageText = ""
                    }
                },
                onMicClick = {
                    // Hook up your audio recorder here; on stop call:
                    // viewModel.sendAudioMessage(uploadedUrl, "00:30")
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is UiState.Loading -> {
                    CircularProgressComponent()
                }

                is UiState.Failure -> {
                    Text(
                        text     = state.message,
                        color    = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UiState.Success -> {
                    LazyColumn(
                        state           = listState,
                        modifier        = Modifier.fillMaxSize(),
                        contentPadding  = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        //reverseLayout = true
                    ) {
                        // Date header
                        item {
                            DateChip(label = "Today")
                        }

                        items(state.message, key = { it.messageId }) { msg ->
                            MessageBubble(
                                message = msg,
                                isSentFlag = msg.senderId == AppConfig.UID,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isSentFlag : Boolean) {
    val alignment = if (isSentFlag) Alignment.End else Alignment.Start
    val bubbleColor = if (isSentFlag) BubbleSent else BubbleReceived
    val textColor    = if (isSentFlag) Color.White else Color.Black
    val bubbleShape = if (isSentFlag) RoundedCornerShape(
        topStart = 14.dp, topEnd = 0.dp,   bottomStart = 14.dp, bottomEnd = 14.dp
    )
    else
        RoundedCornerShape(topStart = 0.dp,  topEnd = 14.dp,  bottomStart = 14.dp, bottomEnd = 14.dp)
    val msgPadding = if (isSentFlag) PaddingValues(start = 10.dp,top = 5.dp,end = 10.dp, bottom = 0.dp)
        else
            PaddingValues(start = 10.dp,top = 5.dp,end = 10.dp, bottom = 0.dp)

    val readTimePadding = if (isSentFlag) PaddingValues(start = 1.dp,top = 1.dp,end = 10.dp, bottom = 5.dp)
    else
        PaddingValues(start = 1.dp,top = 1.dp,end = 10.dp, bottom = 5.dp)

    Column(
        modifier            = Modifier.fillMaxWidth().padding(
            start = if (isSentFlag) 60.dp else 0.dp,
            end = if (isSentFlag) 0.dp else 60.dp
        ),
        horizontalAlignment = alignment
    ) {
        Column (modifier = Modifier
            //.clip(bubbleShape)
            .padding(horizontal = 6.dp, vertical = 6.dp)
            //.background(bubbleColor)
            .background(bubbleColor, bubbleShape)
            ) {
            //Spacer(modifier = Modifier.height(10.dp))
            when(message.messageType) {
                MessageType.TEXT ->
                    Text(
                        text      = message.message,
                        color     = textColor,
                        fontSize  = 14.sp,
                        lineHeight = 20.sp,
                        textAlign = if (isSentFlag) TextAlign.End else TextAlign.Start,
                        modifier = Modifier.padding(msgPadding)
                    )
                else -> {
                    AudioBubble(
                        duration  = message.audioDuration ?: "0:00",
                        textColor = textColor,
                        isSent    = isSentFlag,
                        paddingValues = msgPadding
                    )
                }
            }

            Row(modifier = Modifier.align(AbsoluteAlignment.Right).padding(readTimePadding)) {
                Text(
                    text      = message.timestamp?.toDate()?.let {
                        SimpleDateFormat("h:mm a", Locale.getDefault()).format(it)
                    } ?: "10:30 am",
                    color     = textColor,
                    fontSize  = 10.sp,
                    modifier = Modifier.padding(3.dp,0.dp).align(Alignment.CenterVertically),
                )
                Spacer(Modifier.width(5.dp))
                if (isSentFlag && message.read) {
                    Icon(
                        imageVector = if (message.read) Icons.Default.DoneAll else Icons.Default.Done,
                        contentDescription = if (message.read) "Read" else "Sent",
                        tint = if (message.read) Color(0xFF34B7F1) else Color.White, // blue tick style
                        modifier = Modifier.size(18.dp).align(Alignment.CenterVertically)
                    )
                } else {
                    Icon(
                        imageVector        = Icons.Default.Done,
                        contentDescription = "Read",
                        tint               = if (isSentFlag) Color.White else textColor,
                        modifier           = Modifier.size(16.dp).align(Alignment.CenterVertically)
                    )
                }
            }
            //Spacer(modifier = Modifier.height(10.dp))
    }
    }
}
@Composable
private fun AudioBubble(duration: String, textColor: Color, isSent: Boolean, paddingValues: PaddingValues) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(paddingValues)) {
        // Play button
        Box(
            modifier         = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isSent) Color.White.copy(alpha = 0.2f) else BubbleSent),
            contentAlignment = Alignment.Center

        ) {
            Icon(
                imageVector        = Icons.Default.PlayArrow,
                contentDescription = "Play audio",
                tint               = if (isSent) Color.White else Color.White,
                modifier           = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(10.dp))

        // Waveform placeholder bars
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment     = Alignment.CenterVertically,
            modifier              = Modifier.weight(1f)
        ) {
            val heights = listOf(8, 16, 24, 12, 20, 28, 14, 18, 10, 22, 16, 8)
            heights.forEach { h ->
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(h.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(textColor.copy(alpha = 0.6f))
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(text = duration, color = textColor, fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun showPreview() {
    val messages = listOf(Message("sdfasdf","123","321",message = "Hi Mathu", messageType = MessageType.TEXT, read = true),
        Message("gsdf","321","123",message = "Hello Sudhakar", messageType = MessageType.TEXT, read = true),
                Message("dffgg","123","321",message = "Have you made any plans for the weekend dsfasfd asfasdf sdfasf dfadsf?", messageType = MessageType.TEXT, read = true),
        Message("llj","321","123",message = "Tell me the timing", messageType = MessageType.TEXT, read = true),
        Message("eoiut","123","321",message = "Around 10 PM", messageType = MessageType.TEXT, read = true),
        Message("eoiut","123","321", audioUrl = "Textsdfasdfasdfasdsdf",audioDuration = "2 Mins", messageType = MessageType.AUDIO, read = true),)
    Column() {
        LazyColumn(
            modifier        = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(ScreenBg),

            contentPadding  = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(messages) { message ->
                if (message.senderId.equals("123")) {
                    MessageBubble(message, isSentFlag = true)
                } else {
                    MessageBubble(message, isSentFlag = false)
                }
            }
        }

        ChatInputBar(
            "",
            onValueChange = {},
            onSendClick = {

            },) {

        }

    }
}

@Composable
private fun DateChip(label: String) {
    Box(
        modifier            = Modifier.fillMaxWidth(),
        contentAlignment    = Alignment.Center
    ) {
        Text(
            text      = label,
            fontSize  = 12.sp,
            color     = Color.DarkGray,
            textAlign = TextAlign.Center,
            modifier  = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(DateChipBg)
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit
) {
    Surface (
        color       = InputBg,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Add attachment button
            Box(
                modifier         = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(BubbleSent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.Add,
                    contentDescription = "Emoji",
                    tint               = Color.White,
                    modifier           = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(8.dp))

            OutlinedTextField(
                value         = value,
                onValueChange = onValueChange,
                placeholder   = { Text("Message...", color = Color.Gray, fontSize = 14.sp) },
                modifier      = Modifier.weight(1f),
                shape         = RoundedCornerShape(24.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor   = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5)
                ),
                maxLines = 4,
                singleLine = false
            )

            Spacer(Modifier.width(8.dp))

            // Send or Mic button — swaps based on text content
            //if (value.isNotBlank()) {
                IconButton (onClick = onSendClick) {
                    Icon(
                        imageVector        = Icons.Default.Send,
                        contentDescription = "Send",
                        tint               = BubbleSent,
                        modifier           = Modifier.size(28.dp)
                    )
                }
            /*} else {
                IconButton(onClick = onMicClick) {
                    Icon(
                        imageVector        = Icons.Default.Mic,//Icons.Default.Mic
                        contentDescription = "Voice message",
                        tint               = BubbleSent,
                        modifier           = Modifier.size(28.dp)
                    )
                }
            }*/
        }
    }
}