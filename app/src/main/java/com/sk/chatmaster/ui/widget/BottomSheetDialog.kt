package com.sk.chatmaster.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sk.chatmaster.R
import com.sk.chatmaster.ui.theme.Blue40
import com.sk.chatmaster.ui.theme.Blue80

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetDialog(sheetState : Boolean, title : String, content : String, type : String,
                      titleShowFlag : Boolean) {
    //Control visibility with a simple boolean state
    val showBottomSheetState by remember { mutableStateOf(sheetState) }
    //Control the drag/collapse state of the sheet
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    Surface(shape = RoundedCornerShape(16.dp)) {

    }
    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle()}

    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(30.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (titleShowFlag) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Image(painterResource(R.drawable.ic_success), contentDescription = "Status",
                modifier = Modifier.size(120.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(onClick = {},
                modifier = Modifier.width(120.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Blue40)
            ) {
                Text(text = "Done",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowBottomPreview() {
    BottomSheetDialog(true,"Login Status","Login successfully completed","Success",false)
}