package fr.eseo.ld.e5e.android.robinedo.chores.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun EditableText(
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current,
    singleLine: Boolean = true,
) {
    var isEditing by rememberSaveable { mutableStateOf(false) }

    if(isEditing) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            singleLine = singleLine,
            keyboardActions = KeyboardActions(
                onDone = { isEditing = false }
            )
        )
    }
    else {
        Text(
            text = value,
            style = textStyle,
            modifier = Modifier
                .clickable {isEditing = true}
        )
    }

}