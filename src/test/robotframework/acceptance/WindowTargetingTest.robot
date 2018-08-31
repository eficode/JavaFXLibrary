*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.WindowTargeting related keywords
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Disable Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-windowtargeting

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestMultipleWindows

*** Test Cases ***
Target Window By Window
    [Tags]                  smoke
    ${TARGET}               Get Window      Second window
    Set Target Window       ${TARGET}
    ${WINDOW}               Get Target Window
    Should Be Equal         ${TARGET}       ${WINDOW}    msg=Were not able to set target window according to target window!

Target Window By Window Index
    [Tags]                  smoke
    ${TARGET}               Get Window      1
    Set Target Window       1
    ${WINDOW}               Get Target Window
    Should Be Equal         ${TARGET}       ${WINDOW}    msg=Were not able to set target window according to target window index!

Target Window By Title
    [Tags]                  smoke
    ${TARGET}               Get Window    title=First window
    Set Target Window       First window
    ${WINDOW}               Get Target Window
    Should Be Equal         ${TARGET}     ${WINDOW}   msg=Were not able to set target window according to target window title!

Target Window By Title Pattern
    [Tags]                  smoke
    ${TARGET}               Get Window    title=First window
    Set Target Window       pattern=Fi[r-t]{3} [a-z]{6}
    ${WINDOW}               Get Target Window
    Should Be Equal         ${TARGET}     ${WINDOW}   msg=Were not able to set target window according to target window title pattern!

Target Window By Scene
    [Tags]                  smoke
    ${TARGET}               Get Window          Second window
    ${SCENE}                Get Scene           id=secondWindowAnchorPane
    Set Target Window       ${SCENE}
    ${WINDOW}               Get Target Window
    Should Be Equal         ${TARGET}           ${WINDOW}    msg=Were not able to set target window according to target window scene!

Target Window By Node
    [Tags]                  smoke
    ${TARGET}               Get Window          Third window
    ${NODE}                 Find                id=thirdWindowLabel
    Set Target Window       ${NODE}
    ${WINDOW}               Get Target Window
    Should Be Equal         ${TARGET}           ${WINDOW}    msg=Were not able to set target window according to target window node!

*** Keywords ***
Setup all tests
    Import JavaFXLibrary
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application
