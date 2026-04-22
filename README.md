# NWS Weather

Refactored Android starter for weather.gov + Census geocoding.

## Architecture

- `data/` handles network, models, Room persistence, and repository logic
- `location/` wraps Google Play Services location access behind `DeviceLocationClient`
- `presentation/` owns UI state and screen actions in `WeatherViewModel`
- `ui/` contains Compose screens, components, and theme
- `di/` provides a lightweight application container without adding a full DI framework

## Features

- Search U.S. street addresses using the Census geocoder
- Use current device location through Google Play services
- Pull forecast data from the National Weather Service API
- Save favorite locations locally with Room
- Clean modern Material 3 UI

## Notes

- Update the NWS `User-Agent` contact in `ApiModule.kt` if you want to use your own app identity.
- Current-location lookup is more reliable on a real phone than on the emulator.
