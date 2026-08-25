/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

private val mode = DecimalMode(
    decimalPrecision = 50,
    roundingMode = RoundingMode.ROUND_HALF_TO_EVEN
)

operator fun BigDecimal.div(other: Float): BigDecimal {
    return this.divide(other.toBigDecimal(), mode)
}

operator fun BigDecimal.div(other: Double): BigDecimal {
    return this.divide(other.toBigDecimal(), mode)
}

fun BigDecimal.saveDiv(other: BigDecimal): BigDecimal {
    if (other == BigDecimal.ZERO)
        return this
    return this.divide(other, mode)
}

fun BigDecimal.saveDiv(other: Int): BigDecimal {
    if (other == 0)
        return this
    return this.divide(other.toBigDecimal(), mode)
}

