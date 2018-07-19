*** Settings ***
Documentation     Tests to test javafxlibrary.keywords.KeyboardRobot related keywords
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-keyboardrobot

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestKeyboardRobot

*** Test Cases ***
Press and hold three buttons
    [Tags]                  smoke
    Click On                \#keyCombinationLabel
    Press                   CONTROL    SHIFT    G
    Verify String           \#keyCombinationLabel    Passed
    Release                 CONTROL    SHIFT    G

Press / Release
    [Tags]                  smoke
    Click On                \#textArea
    Press                   SHIFT
    Push in order           T    E    S
    Release                 SHIFT
    Push in order           T    I
    Verify String           \#textAreaLabel    TESti
    Push in order           BACK_SPACE    LEFT    BACK_SPACE
    Verify String           \#textAreaLabel    TEt

*** Keywords ***
Setup all tests
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application

Reset Combination Test
    Right Click On Query    \#keyCombinationLabel

Verify String
    [Documentation]                 Verifies that string is equal in location
    [Arguments]                     ${query}          ${string}
    ${target_node}=                 Find              ${query}
    ${text_label}=                  Get Node Text     ${target_node}
    Should Be Equal As Strings      ${string}         ${text_label}