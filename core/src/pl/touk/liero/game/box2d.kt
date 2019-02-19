package pl.touk.liero.game

private var next = 1
private fun next(): Short {
    val current = next
    next = next.shl(1)
    return current.toShort()
}

val cat_red = next()
val cat_blue = next()
val cat_bulletRed = next()
val cat_bulletBlue = next()
val cat_ground = next()
val cat_cos = next()

val mask_all = 0xffff.toShort()

//                            red blu  rb  bb gnd cos
val mask_red             = 0b0__1___1___0___1___1___1.toShort()
val mask_blue            = 0b0__1___1___1___0___1___1.toShort()
val mask_bulletRed       = 0b0__0___1___0___1___1___1.toShort()
val mask_bulletBlue      = 0b0__1___0___1___0___1___1.toShort()
val mask_ground          = 0b0__1___1___1___1___1___1.toShort()
val mask_cos             = 0b0__1___1___1___1___1___1.toShort()
