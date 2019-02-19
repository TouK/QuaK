package pl.touk.liero.utils

fun <E> List<E>.randomElement(): E = this.get(rnd(this.size - 1))