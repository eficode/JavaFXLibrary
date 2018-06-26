*** Settings ***
Documentation     Tests for handling Swing embedded JavaFX nodes
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-embedded

*** Testcases ***
Swing Embedded JavaFX Click Test
    [Tags]    smoke
    Wait Until Keyword Succeeds    15 sec    200ms    Find    .button     	failIfNotFound=True
    ${colors}    Create List    0xdc143cff    0x00fa9aff    0xee82eeff    0xffff00ff    0x00ffffff
    Text Value Should Be    Swing Embedded JavaFX
    :FOR    ${I}    IN RANGE    0    5
    \    Click On    .button
    \    Text Value Should Be    @{colors}[${i}]

Swing Embedded JavaFX Type Test
    [Tags]    smoke
    Wait Until Keyword Succeeds    15 sec    250ms    Find    \#textField     	failIfNotFound=True
    Write To    \#textField    JavaFXLibrary
    Text Value Should Be    JavaFXLibrary

*** Keywords ***
Setup all tests
    Launch Javafx Application     javafxlibrary.testapps.SwingApplicationWrapper

Teardown all tests
    Close Javafx Application

Text Value Should Be
    [Arguments]    ${value}
    ${text}    Get Node Text    \#textValue
    Should Be Equal    ${text}    ${value}