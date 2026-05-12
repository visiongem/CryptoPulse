# Privacy Policy

**Effective date:** 2026-05-12
**App:** CryptoPulse
**Developer:** visiongem ([GitHub](https://github.com/visiongem))

## TL;DR

> **We don't collect anything.** No account, no analytics, no crash reports, no telemetry. Your watchlist and settings live only on your device. The app communicates only with public crypto market APIs to fetch prices.

---

## Information we collect

**None.** CryptoPulse has no backend. We do not operate any server that receives data from your device.

## Information stored on your device

The app stores the following locally using Android DataStore (a private file inside the app's sandbox):

- Your watchlist of crypto symbols (e.g., `["BTC", "ETH", "SOL"]`)
- Display preferences (e.g., whether price flash animation is enabled)
- A cached snapshot of the latest market data for home-screen widgets

This data **never leaves your device**. Uninstalling the app removes all of it.

## Network connections

To show live prices, the app communicates with two **public, no-auth** services:

| Service | Purpose | What we send |
|---------|---------|---------------|
| [CoinGecko Public API](https://www.coingecko.com/en/api) | Coin metadata, 24h prices, historical charts | An HTTP GET request — no identifiers, no cookies, no tokens |
| [Binance Public WebSocket](https://binance-docs.github.io/apidocs/spot/en/) | Real-time price ticker stream | A WebSocket connection — no identifiers, no auth headers |

These services may log your IP address as part of normal HTTPS/WebSocket traffic. Their privacy policies apply when you use them. We have no way to correlate your usage across sessions because we never identify you to them.

## Permissions used

| Permission | Why |
|------------|-----|
| `INTERNET` | Fetch price data |
| `ACCESS_NETWORK_STATE` | Detect offline state to show a "no network" message |

The app requests **no other permissions** — no location, no contacts, no storage, no notifications.

## Children

The app is not directed at children under 13 and does not knowingly collect any information from them.

## Changes

If this policy ever changes, an updated version will be committed to this repository. The git history serves as the authoritative changelog.

## Contact

Open an issue at https://github.com/visiongem/CryptoPulse/issues
