*** Settings ***
Documentation       Tests for handling Swing embedded JavaFX nodes
Resource            ../resource.robot
Force Tags          set-embedded
Suite Setup         Import JavaFXLibrary
Test Setup          Disable Embedded Image Logging For Negative Tests
Test Teardown       Teardown Test Case

*** Testcases ***
Swing Embedded JavaFX Click Test
    [Tags]                              smoke           demo-set
    Run Keyword If    ${headless}    Set Tags    monocle-issue
    Launch Swing Application            javafxlibrary.testapps.SwingApplication
    Wait Until Keyword Succeeds         15 sec          250ms           Find          css=.button     	failIfNotFound=True
    ${colors}    Create List            0xdc143cff      0x00fa9aff      0xee82eeff    0xffff00ff        0x00ffffff
    Text Value Should Be                Swing Embedded JavaFX
    :FOR    ${I}    IN RANGE            0               5
    \    Click On                       css=.button
    \    Wait Until Keyword Succeeds    3 sec           250ms           Text Value Should Be       @{colors}[${i}]

Swing Embedded JavaFX Type Test
    [Tags]                          smoke
    Run Keyword If    ${headless}    Set Tags    monocle-issue
    Launch Swing Application        javafxlibrary.testapps.SwingApplication
    Wait Until Keyword Succeeds     15 sec          250ms           Find    id=textField     	failIfNotFound=True
    Write To                        id=textField    JavaFXLibrary
    Wait Until Keyword Succeeds     3 sec           250ms       Text Value Should Be    JavaFXLibrary

Launch Swing Application Using External Wrapper Class
    [Tags]                          smoke
    Run Keyword If    ${headless}    Set Tags    monocle-issue
    Launch Javafx Application       javafxlibrary.testapps.SwingApplicationWrapper
    Wait Until Keyword Succeeds     15 sec          250ms       Find                    id=textField    failIfNotFound=True
    Write To                        id=textField    JavaFXLibrary
    Wait Until Keyword Succeeds     3 sec           250ms       Text Value Should Be    JavaFXLibrary

*** Keywords ***
Teardown Test Case
    Close Test Application
    Enable Image Logging

Text Value Should Be
    [Arguments]         ${value}
    ${text}             Get Node Text       id=textValue
    Should Be Equal     ${text}             ${value}

Close Test Application
    Run Keyword If      '${TEST NAME}' == 'Launch Swing Application Using External Wrapper Class'    Close Javafx Application
    ...                 ELSE    Close Swing Application
