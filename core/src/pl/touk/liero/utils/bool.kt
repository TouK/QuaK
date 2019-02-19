package pl.touk.liero.utils

/**
 * Extension functions for Boolean type
 */

inline infix fun Boolean.then(block: () -> Unit) {
    if (this) block()
}