package io.github.visiongem.cryptopulse.util

/**
 * Symbols with known &lt;X&gt;USDT trading pairs on Binance.
 *
 * Binance closes the combined-stream connection if *any* subscribed stream is
 * invalid, so we must filter against this allowlist before subscribing.
 *
 * Kept in approximate market-cap order; expand as needed.
 */
internal val BINANCE_USDT_SYMBOLS: Set<String> = setOf(
    "BTC", "ETH", "BNB", "SOL", "XRP", "DOGE", "ADA", "TRX", "AVAX", "SHIB",
    "DOT", "LINK", "TON", "MATIC", "LTC", "BCH", "NEAR", "ICP", "UNI", "APT",
    "ETC", "FIL", "ATOM", "ARB", "OP", "INJ", "TIA", "SUI", "SEI", "PEPE",
    "RNDR", "IMX", "STX", "LDO", "AAVE", "RUNE", "GRT", "ALGO", "MKR", "QNT",
    "FET", "VET", "FLOW", "EGLD", "THETA", "AXS", "SAND", "MANA", "CHZ", "GALA",
)
