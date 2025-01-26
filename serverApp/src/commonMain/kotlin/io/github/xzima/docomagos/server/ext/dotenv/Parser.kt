/**
 * Copyright 2025 Alex Zima(xzima@ro.ru)
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
package io.github.xzima.docomagos.server.ext.dotenv

fun parseDotEnvFromReader(
    reader: Sequence<String>,
    throwIfMissing: Boolean = true,
    throwIfMalformed: Boolean = true,
): Map<String, String> {
    val lines = try {
        reader
    } catch (e: DotenvException) {
        if (throwIfMissing) throw e
        emptySequence()
    } catch (e: Exception) {
        throw DotenvException(cause = e)
    }

    val entries = mutableMapOf<String, String>()

    var currentEntry = ""
    for (line in lines) {
        if (currentEntry == "" && (line.isBlank() || line.isWhiteSpace() || line.isComment())) continue

        currentEntry += line

        val entry = currentEntry.parseLine()
        if (entry == null) {
            if (throwIfMalformed) throw DotenvException(message = "Malformed entry $currentEntry")
            currentEntry = ""
            continue
        }

        var (key, value) = entry
        if (startsWithQuote(value) && !endsWithQuote(value)) {
            currentEntry += "\n"
            continue
        }
        if (!isValid(value)) {
            if (throwIfMalformed) throw DotenvException(message = "Malformed entry, unmatched quotes $line")
            currentEntry = ""
            continue
        }
        value = stripQuotes(value)
        entries[key] = value
        currentEntry = ""
    }

    return entries
}

private fun String.isWhiteSpace(): Boolean {
    // ^\s*${'$'}
    val whiteSpaceRegex = Regex("^\\s*$")
    return whiteSpaceRegex.matches(this)
}

private fun String.parseLine(): Pair<String, String>? {
    // The follow regex matches key values.
    // It supports quoted values surrounded by single or double quotes
    // -  Single quotes: ['][^']*[']
    //    The above regex snippet matches a value wrapped in single quotes.
    //    The regex snippet does not match internal single quotes. This is present to allow the trailing comment to include single quotes
    // -  Double quotes: same logic as single quotes
    // It ignores trailing comments
    // - Trailing comment: \s*(#.*)?$
    //   The above snippet ignore spaces, the captures the # and the trailing comment
    // "^\\s*([\\w.\\-]+)\\s*(=)\\s*([^#]*)?\\s*(#.*)?$"); // ^\s*([\w.\-]+)\s*(=)\s*([^#]*)?\s*(#.*)?$
    val dotenvEntryRegex = Regex("^\\s*([\\w.\\-]+)\\s*(=)\\s*('[^']*'|\"[^\"]*\"|[^#]*)?\\s*(#.*)?$")

    val matcherGroups = dotenvEntryRegex.find(this)?.groups ?: return null
    if (matcherGroups.size < 3) return null

    return matcherGroups[1]!!.value to matcherGroups[3]!!.value
}

private fun isValid(input: String): Boolean {
    val s = input.trim()
    if (isNotQuoted(s)) {
        return true
    }
    if (doesNotStartAndEndWithQuote(s)) {
        return false
    }

    return !hasUnescapedQuote(s) // No unescaped quotes found
}

private fun hasUnescapedQuote(s: String): Boolean {
    // remove start end quote
    val content = s.substring(1, s.length - 1)
    val quotePattern = Regex("\"")
    return quotePattern.findAll(content).any {
        val quoteIndex = it.range.start
        quoteIndex == 0 || content[quoteIndex - 1] != '\\'
    }
}

private fun doesNotStartAndEndWithQuote(s: String): Boolean = 1 == s.length || !(startsWithQuote(s) && endsWithQuote(s))

private fun endsWithQuote(s: String): Boolean = s.endsWith("\"")

private fun startsWithQuote(s: String): Boolean = s.startsWith("\"")

private fun isNotQuoted(s: String): Boolean = !startsWithQuote(s) && !endsWithQuote(s)

private fun stripQuotes(input: String): String {
    val tr = input.trim()
    return if (tr.isQuoted()) tr.substring(1, input.length - 1) else tr
}

private fun String.isComment(): Boolean = startsWith("#") || startsWith("////")

private fun String.isQuoted(): Boolean = length > 1 && startsWith("\"") && endsWith("\"")
