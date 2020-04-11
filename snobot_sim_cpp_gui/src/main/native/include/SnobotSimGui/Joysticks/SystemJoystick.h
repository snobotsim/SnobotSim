
#pragma once

struct SystemJoystick
{
    bool present = false;

    int axisCount = 0;
    const float* axes = nullptr;

    int buttonCount = 0;
    const unsigned char* buttons = nullptr;

    int hatCount = 0;
    const unsigned char* hats = nullptr;

    const char* name = nullptr;
    bool isGamepad = false;
    //   GLFWgamepadstate gamepadState;

    bool anyButtonPressed = false;

    void Update(int i);
};
