package fr.eseo.ld.e5e.android.robinedo.chores.ui

import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.captureToImage
import org.junit.jupiter.api.Assumptions
import kotlin.math.abs

fun SemanticsNodeInteraction.assertStatusColour(
    expectedColour : Color,
    tolerance : Int = 30
) {
    Assumptions.assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    val bitmap = captureToImage().asAndroidBitmap()
    val centerX = bitmap.width / 2
    val centerY = bitmap.height / 2
    val pixelColour = bitmap.getPixel(centerX, centerY)

    val actualR = (pixelColour shr 16) and 0xFF
    val actualG = (pixelColour shr 8) and 0xFF
    val actualB = pixelColour and 0xFF

    val expectR = (expectedColour.red * 255).toInt()
    val expectG = (expectedColour.green * 255).toInt()
    val expectB = (expectedColour.blue * 255).toInt()

    val rDiff = abs(actualR - expectR)
    val gDiff = abs(actualG - expectG)
    val bDiff = abs(actualB - expectB)

    assert(rDiff <= tolerance && gDiff <= tolerance && bDiff <= tolerance) {
        """
        Color mismatch at center pixel ($centerX, $centerY):
        Expected : r=${expectR}, g=${expectG}, b=${expectB}
        Actual   : r=${actualR},   g=${actualG},   b=${actualB}
        Diff     : Δr=$rDiff, Δg=$gDiff, Δb=$bDiff (tolerance=$tolerance)
        """.trimIndent()
    }
}
