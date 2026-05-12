package org.example.Combat

class Attack(
    var dmgValue: Int = 0,
    dmgType: String = "STA"
) {
    var dmgType: String = dmgType
        set(value) {
            val upper = value.uppercase()
            require( when (value) {
                "PHY", "MAG", "STA" -> true
                else -> false
            }) { "Damage type must be either PHY for physical, MAG for magical or STA for status change." }
            field = upper
        }
}