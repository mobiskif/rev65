package ru.rev65

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
    body1 body2 button caption
    h1 h2 h3 h4 h5 h6
    overline subtitle1 subtitle2
*/

val shapes = Shapes(
    /*
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
    */
)

val typography = Typography(
    /*
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = Color.Red
    ),
    */
)

val error = typography.overline

/*
val LightPalette = lightColors(
        //primary = Color(android.graphics.Color.parseColor("#c2185b")),
        primary = Color(ContextCompat.getColor(context!!, R.color.primaryColor)), //цвет Button и подписей
        primaryVariant = Color(ContextCompat.getColor(context!!, R.color.primaryDarkColor)), //цвет StatusBar
        secondary = Color(ContextCompat.getColor(context!!, R.color.secondaryColor)), //цвет FAB и RadioButton
        secondaryVariant = Color(ContextCompat.getColor(context!!, R.color.secondaryDarkColor)), //цвет Switch
        surface = Color(ContextCompat.getColor(context!!, R.color.secondaryLightColor))
        //background = Color(getColor(context!!, R.color.secondaryLightColor)) //цвет полотна приложения
)
*/

val LightPalette = lightColors(
    primary = Color(android.graphics.Color.parseColor("#00695c")), //цвет Button и подписей
    primaryVariant = Color(android.graphics.Color.parseColor("#003d33")), //цвет StatusBar
    secondary = Color(android.graphics.Color.parseColor("#00695c")), //цвет FAB и RadioButton
    secondaryVariant = Color(android.graphics.Color.parseColor("#005005")), //цвет Switch
    //surface = Color(android.graphics.Color.parseColor("#ffffff"))
)

val DarkPalette = darkColors(
    primary = Color.Gray, //цвет Button и подписей
    //primaryVariant = Color.DarkGray, //цвет StatusBar
    secondary = Color.Gray, //цвет FAB и RadioButton
    //secondaryVariant = Color.Gray, //цвет Switch
    //surface = Color.Gray, //в темной теме цвет ActionBar

    //onSurface = Color.Cyan,
    //onPrimary = Color.Red,
    //onSecondary = Color.Yellow,
    //onBackground = Color.Green
)

/*
@Composable
fun mod_back(): Modifier {
    return Modifier
    .background(color = MaterialTheme.colors.primary, shape = shapes.medium)
}

@Composable
fun mod_card(oncl: () -> Unit): Modifier {
    return Modifier
        .padding(8.dp)
        .fillMaxWidth()
        //.wrapContentWidth(Alignment.CenterHorizontally)
        //.background(color = MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium)
        .border(0.dp, MaterialTheme.colors.primary, shapes.small)
        .clickable(onClick = oncl)
}
*/

@Composable
fun myTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (dark) {
            DarkPalette
        } else {
            LightPalette
        },
        typography = typography,
        shapes = shapes,
        content = content
    )
}