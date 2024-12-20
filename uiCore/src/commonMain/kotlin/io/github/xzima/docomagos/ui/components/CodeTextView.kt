/**
 * Copyright 2024 Alex Zima(xzima@ro.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
fun CodeTextView(highlights: Highlights, modifier: Modifier = Modifier.background(Color.Transparent)) = Surface {
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
