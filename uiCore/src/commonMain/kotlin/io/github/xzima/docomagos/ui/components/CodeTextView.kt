package io.github.xzima.docomagos.ui.components

import androidx.compose.foundation.background
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight

@Composable
fun CodeTextView(modifier: Modifier = Modifier.background(Color.Transparent), highlights: Highlights) = Surface {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            append(highlights.getCode())

            highlights.getHighlights().forEach {
                val style = when (it) {
                    is BoldHighlight -> SpanStyle(fontWeight = FontWeight.Bold)
                    is ColorHighlight -> SpanStyle(color = Color(it.rgb).copy(alpha = 1f))
                }
                addStyle(
                    style,
                    start = it.location.start,
                    end = it.location.end,
                )
            }
        },
    )
}
