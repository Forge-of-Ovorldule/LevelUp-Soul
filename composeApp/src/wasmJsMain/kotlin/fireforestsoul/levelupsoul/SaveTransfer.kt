@file:OptIn(ExperimentalWasmJsInterop::class)

package fireforestsoul.levelupsoul

import kotlin.js.ExperimentalWasmJsInterop

external class CssStyleDeclaration {
    var display: String
}

external class DomElement {
    var style: CssStyleDeclaration

    var href: String
    var download: String

    var type: String
    var accept: String
    var files: DomFileList?
    var onchange: (() -> Unit)?

    fun click()
    fun appendChild(child: DomElement)
    fun removeChild(child: DomElement)
}

external class DomFile

external class DomFileList {
    fun item(index: Int): DomFile?
}

external class DomFileReader {
    var onload: (() -> Unit)?
    var onerror: (() -> Unit)?
    var result: String?
    fun readAsText(file: DomFile)
}

external class WasmDocument {
    fun createElement(tag: String): DomElement
    val body: DomElement
}

private val browserDocument: WasmDocument = js("document")

private const val HEX_DIGITS = "0123456789ABCDEF"

private fun isUnreserved(c: Char): Boolean =
    c in 'A'..'Z' || c in 'a'..'z' || c in '0'..'9' ||
            c == '-' || c == '_' || c == '.' || c == '~'

private fun percentEncodeUtf8(s: String): String {
    val bytes = s.encodeToByteArray()
    val sb = StringBuilder(bytes.size * 3)
    for (b in bytes) {
        val code = b.toInt() and 0xFF
        val c = code.toChar()
        if (isUnreserved(c)) {
            sb.append(c)
        } else {
            sb.append('%')
                .append(HEX_DIGITS[code shr 4])
                .append(HEX_DIGITS[code and 0x0F])
        }
    }
    return sb.toString()
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object SaveTransfer {

    actual fun exportToFile(fileName: String, content: String) {
        val anchor = browserDocument.createElement("a")
        anchor.href = "data:application/json;charset=utf-8," + percentEncodeUtf8(content)
        anchor.download = fileName
        browserDocument.body.appendChild(anchor)
        anchor.click()
        browserDocument.body.removeChild(anchor)
    }

    actual fun importFromFile(onResult: (String?) -> Unit) {
        val input = browserDocument.createElement("input")
        input.type = "file"
        input.accept = ".json,.txt,application/json,text/plain"
        input.style.display = "none"

        input.onchange = {
            input.onchange = null
            browserDocument.body.removeChild(input)

            val file = input.files?.item(0)
            if (file == null) {
                onResult(null)
            } else {
                val reader = DomFileReader()
                reader.onload = {
                    onResult(reader.result)
                }
                reader.onerror = {
                    onResult(null)
                }
                reader.readAsText(file)
            }
        }

        browserDocument.body.appendChild(input)
        input.click()
    }
}