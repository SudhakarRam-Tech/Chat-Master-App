package com.sk.chatmaster.core.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppOutlinedTextField — single reusable component to replace all OutlinedTextField
// usages across screens.
// ─────────────────────────────────────────────────────────────────────────────

/**
 * A standardised outlined text field to be used on every screen.
 *
 * @param value          Current text value.
 * @param onValueChange  Called whenever the text changes.
 * @param modifier       Optional [Modifier].
 * @param label          Field label (e.g. "Email").
 * @param placeholder    Optional placeholder shown when field is empty.
 * @param leadingIcon    Optional leading [ImageVector].
 * @param trailingIcon   Optional trailing [ImageVector]. Ignored when [isPassword] = true
 *                       (the eye icon is managed automatically).
 * @param onTrailingIconClick  Click handler for a custom [trailingIcon].
 * @param isPassword     When true, toggles visibility with an eye icon.
 * @param capitalization Controls auto-capitalisation. Defaults to [KeyboardCapitalization.None].
 *                       Use [KeyboardCapitalization.Sentences] for name/bio fields,
 *                       [KeyboardCapitalization.Words] for title/name fields,
 *                       [KeyboardCapitalization.Characters] for codes/IDs.
 * @param keyboardType   Defaults to [KeyboardType.Text].
 * @param imeAction      Defaults to [ImeAction.Next].
 * @param keyboardActions Forwarded to [OutlinedTextField].
 * @param isError        Highlights the field in the error colour.
 * @param errorMessage   Shown below the field when [isError] is true.
 * @param supportingText Text shown below the field when there is no error.
 * @param enabled        Enables or disables the field.
 * @param readOnly       Makes the field non-editable while still focusable.
 * @param singleLine     Forces single-line input (default true).
 * @param maxLines       Maximum lines when [singleLine] is false.
 * @param shape          Corner shape; defaults to 12 dp rounded.
 * @param colors         Override the default [OutlinedTextFieldDefaults.colors].
 */
@Composable
fun AppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isPassword: Boolean = false,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    supportingText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    // Password visibility state — only relevant when isPassword = true
    var passwordVisible by remember { mutableStateOf(false) }

    val resolvedVisualTransformation = when {
        isPassword && !passwordVisible -> PasswordVisualTransformation()
        else                           -> VisualTransformation.None
    }

    val resolvedKeyboardType = when {
        isPassword -> KeyboardType.Password
        else       -> keyboardType
    }

    OutlinedTextField(
        value            = value,
        onValueChange    = onValueChange,
        modifier         = modifier.fillMaxWidth(),
        enabled          = enabled,
        readOnly         = readOnly,
        singleLine       = singleLine,
        maxLines         = maxLines,
        shape            = shape,
        colors           = colors,
        isError          = isError,
        visualTransformation = resolvedVisualTransformation,
        keyboardOptions  = KeyboardOptions(
            capitalization = capitalization,
            keyboardType   = resolvedKeyboardType,
            imeAction      = imeAction,
        ),
        keyboardActions  = keyboardActions,

        // ── Label ────────────────────────────────────────────────────────────
        label = label?.let {
            { Text(text = it) }
        },

        // ── Placeholder ───────────────────────────────────────────────────
        placeholder = placeholder?.let {
            { Text(text = it) }
        },

        // ── Leading icon ─────────────────────────────────────────────────
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector        = it,
                    contentDescription = null,
                )
            }
        },

        // ── Trailing icon ─────────────────────────────────────────────────
        trailingIcon = when {
            isPassword -> {
                {
                    val icon = if (passwordVisible) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                }
            }
            trailingIcon != null -> {
                {
                    if (onTrailingIconClick != null) {
                        IconButton(onClick = onTrailingIconClick) {
                            Icon(imageVector = trailingIcon, contentDescription = null)
                        }
                    } else {
                        Icon(imageVector = trailingIcon, contentDescription = null)
                    }
                }
            }
            else -> null
        },

        // ── Supporting / error text ───────────────────────────────────────
        supportingText = when {
            isError && errorMessage != null -> {
                {
                    Text(
                        text  = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            supportingText != null -> {
                {
                    Text(
                        text  = supportingText,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            else -> null
        },
    )
}


// ─────────────────────────────────────────────────────────────────────────────
// Convenience wrappers for the most common field types
// ─────────────────────────────────────────────────────────────────────────────

/** Pre-configured email field. */
@Composable
fun AppEmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Email",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: ImageVector? = null,
) {
    AppOutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        modifier        = modifier,
        label           = label,
        leadingIcon     = leadingIcon,
        capitalization  = KeyboardCapitalization.None,   // emails are always lowercase
        keyboardType    = KeyboardType.Email,
        imeAction       = imeAction,
        keyboardActions = keyboardActions,
        isError         = isError,
        errorMessage    = errorMessage,
    )
}

/** Pre-configured password field with automatic eye-icon toggle. */
@Composable
fun AppPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: ImageVector? = null,
) {
    AppOutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        modifier        = modifier,
        label           = label,
        leadingIcon     = leadingIcon,
        isPassword      = true,
        capitalization  = KeyboardCapitalization.None,   // passwords are case-sensitive
        imeAction       = imeAction,
        keyboardActions = keyboardActions,
        isError         = isError,
        errorMessage    = errorMessage,
    )
}

/** Pre-configured phone-number field. */
@Composable
fun AppPhoneTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Phone number",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: ImageVector? = null,
) {
    AppOutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        modifier        = modifier,
        label           = label,
        leadingIcon     = leadingIcon,
        capitalization  = KeyboardCapitalization.None,   // phone numbers need no capitalisation
        keyboardType    = KeyboardType.Phone,
        imeAction       = imeAction,
        keyboardActions = keyboardActions,
        isError         = isError,
        errorMessage    = errorMessage,
    )
}

/** Pre-configured multi-line text area. */
@Composable
fun AppTextAreaField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    maxLines: Int = 5,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
) {
    AppOutlinedTextField(
        value          = value,
        onValueChange  = onValueChange,
        modifier       = modifier,
        label          = label,
        placeholder    = placeholder,
        singleLine     = false,
        maxLines       = maxLines,
        isError        = isError,
        errorMessage   = errorMessage,
        capitalization = capitalization,
        imeAction      = ImeAction.Default,
    )
}


// ─────────────────────────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "All AppOutlinedTextField variants")
@Composable
private fun AppOutlinedTextFieldPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Basic
            var text by remember { mutableStateOf("") }
            AppOutlinedTextField(value = text, onValueChange = { text = it }, label = "Username")

            // Email
            var email by remember { mutableStateOf("") }
            AppEmailTextField(value = email, onValueChange = { email = it })

            // Password
            var password by remember { mutableStateOf("") }
            AppPasswordTextField(value = password, onValueChange = { password = it })

            // Phone
            var phone by remember { mutableStateOf("") }
            AppPhoneTextField(value = phone, onValueChange = { phone = it })

            // Error state
            AppOutlinedTextField(
                value         = "bad input",
                onValueChange = {},
                label         = "Email",
                isError       = true,
                errorMessage  = "Invalid email address",
            )

            // Text area
            var note by remember { mutableStateOf("") }
            AppTextAreaField(value = note, onValueChange = { note = it }, label = "Notes", maxLines = 4)

            // Disabled
            AppOutlinedTextField(value = "Cannot edit", onValueChange = {}, label = "Read-only field", enabled = false)
        }
    }
}



/*
@Preview(showBackground = true)
@Composable
fun AppFieldPreview() {
    MaterialTheme {

    }
}*/
