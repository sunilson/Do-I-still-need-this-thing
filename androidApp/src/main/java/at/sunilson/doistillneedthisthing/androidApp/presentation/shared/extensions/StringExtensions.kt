package at.sunilson.doistillneedthisthing.androidApp.presentation.shared.extensions

fun String?.orElse(block: () -> String): String {
    return if (this == null) block() else this
}