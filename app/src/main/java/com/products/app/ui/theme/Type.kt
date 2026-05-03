package com.products.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font as GoogleDownloadableFont
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.products.app.R

private val dmSansProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private val dmSansName = GoogleFont("DM Sans")

val DmSansFamily: FontFamily = FontFamily(
    GoogleDownloadableFont(
        googleFont = dmSansName,
        fontProvider = dmSansProvider,
        weight = FontWeight.Normal,
    ),
    GoogleDownloadableFont(
        googleFont = dmSansName,
        fontProvider = dmSansProvider,
        weight = FontWeight.Medium,
    ),
    GoogleDownloadableFont(
        googleFont = dmSansName,
        fontProvider = dmSansProvider,
        weight = FontWeight.SemiBold,
    ),
    GoogleDownloadableFont(
        googleFont = dmSansName,
        fontProvider = dmSansProvider,
        weight = FontWeight.Bold,
    ),
)

fun typographyWithDmSans(): Typography {
    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.copy(fontFamily = DmSansFamily),
        displayMedium = base.displayMedium.copy(fontFamily = DmSansFamily),
        displaySmall = base.displaySmall.copy(fontFamily = DmSansFamily),
        headlineLarge = base.headlineLarge.copy(fontFamily = DmSansFamily),
        headlineMedium = base.headlineMedium.copy(fontFamily = DmSansFamily),
        headlineSmall = base.headlineSmall.copy(fontFamily = DmSansFamily),
        titleLarge = base.titleLarge.copy(fontFamily = DmSansFamily),
        titleMedium = base.titleMedium.copy(fontFamily = DmSansFamily),
        titleSmall = base.titleSmall.copy(fontFamily = DmSansFamily),
        bodyLarge = base.bodyLarge.copy(fontFamily = DmSansFamily),
        bodyMedium = base.bodyMedium.copy(fontFamily = DmSansFamily),
        bodySmall = base.bodySmall.copy(fontFamily = DmSansFamily),
        labelLarge = base.labelLarge.copy(fontFamily = DmSansFamily),
        labelMedium = base.labelMedium.copy(fontFamily = DmSansFamily),
        labelSmall = base.labelSmall.copy(fontFamily = DmSansFamily),
    )
}
