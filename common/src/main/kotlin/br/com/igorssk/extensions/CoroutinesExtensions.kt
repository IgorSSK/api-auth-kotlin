package br.com.igorssk.extensions

import kotlinx.coroutines.runBlocking
import java.io.Closeable

fun <T : Closeable?, R> T.safeAwait(block: suspend (T) -> R): R = this.use { runBlocking { block(it) } }
