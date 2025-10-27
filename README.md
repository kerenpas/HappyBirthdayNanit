This project was created as a home assignment for the Android Developer position at Nanit.
It demonstrates practical skills in Jetpack Compose, MVVM architecture, WebSocket communication, and precise UI calculations for pixel-perfect design alignment.


⚠️ Known Issues

The current implementation of the camera icon positioning assumes a fixed 45° angle between the center of the baby image and the icon.
This makes the calculation pixel-perfect only for that specific angle.

If in future UX requirements the desired angle changes (for example, placing the icon at 30° or 60°), this calculation will no longer work correctly, since it’s hard-coded for 45° and based on isosceles right-triangle geometry.

I did implement a general version that calculates the position for any arbitrary angle using trigonometric functions (cos/sin),
but the result wasn’t perfectly aligned visually.
I believe the small offset stems from accumulated rounding errors — converting between dp ↔ px and rounding non-integer cos/sin results to whole numbers or integer multiplications.

Unit Test

Added  git hub action to
Runs all unit tests automatically — including your three test files under src/test/java/...

PresentationMappersTest

CalculateAgeTest

DataMappersTest

.
