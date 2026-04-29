# Privacy Policy

**Last updated: 23 April 2026**

---

## Overview

This application is designed with privacy as a core principle.

It does not collect, store, or share personal data beyond what is required for its core functionality.

---

## Information Collection

This app does **not** collect personally identifiable information.

- No analytics are used  
- No tracking technologies are included  
- No user accounts are required  

---

## Location Data

If you choose to use location-based features:

- Your device location is used solely to retrieve weather data  
- Location data is not stored, logged, or retained by the application  
- Location access is optional and can be denied at any time  

---

## Network Requests

This app makes network requests to retrieve weather and location data.

### National Weather Service (NWS)

- Weather forecasts and related data are retrieved from official National Weather Service APIs  
- These requests include location coordinates (latitude/longitude) to return relevant weather data  

### Address Lookup (Geocoding)

- Address search functionality uses Android’s built-in `Geocoder`  
- Depending on the device and Android version, geocoding requests may be handled by a system-provided backend (such as a Google location service)  
- This functionality is part of the Android operating system and not a third-party library included by this application  

---

## Data Handling

- The app does not store or log network requests  
- No data is sent to analytics, advertising, or tracking services  
- The app does not include any third-party SDKs for data collection  

Only the minimum data required to provide weather and location functionality is used.

---

## Data Flow

| Feature | Data involved | Where it goes | Stored? | Notes |
|---|---|---|---|---|
| Weather forecast lookup | Latitude/longitude or selected location | National Weather Service API | No remote storage by this app | Required to retrieve local forecast data |
| Current device location | Device latitude/longitude | Used locally, then sent to NWS for forecast lookup | Not stored remotely | Location permission is optional and user-controlled |
| Manual address search | Address/search text | Android Geocoder / system geocoding backend | Not stored unless saved as favorite | Backend may vary by Android device/OEM |
| Saved locations | Location name and coordinates | Local device database via Room | Yes, locally only | Used for favorites and quick access |
| Home screen widget | Selected/saved weather location and forecast data | NWS API during refresh | Locally cached as needed by Android/widget system | Used to update the widget |
| Background updates | Saved/widget location | NWS API | No remote storage by this app | Uses WorkManager for scheduled refreshes |
| App settings/preferences | User preferences | Local device only | Yes, locally only | Not transmitted externally |

---


## Data Storage

- Saved locations are stored locally on your device  
- No user data is transmitted to external servers for storage  

---

## Third-Party Services

This app does not include third-party SDKs that collect user data.

However, certain platform-level services (such as Android’s Geocoder) may rely on system-provided backends outside the control of this application.

---

## Children’s Privacy

This app does not knowingly collect personal data from users of any age.

---

## Changes to This Policy

If this policy changes in the future, updates will be reflected in this document.

---

## Contact

If you have any questions or concerns, please open an issue on the GitHub repository.
