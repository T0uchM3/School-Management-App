package com.example.schoolmanagementsystem.ui.theme

//import androidx.compose.material.Typography
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.schoolmanagementsystem.R

// Set of Material typography styles to start with
val Typography = Typography(
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.interlight)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.intermedium)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.interbold)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black
    )
    /* Other default text styles to override
button = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp
),

*/

)
val caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
val InterMedium = FontFamily(
    Font(R.font.inter),
)
