*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.ScrollRobot related keywords
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Disable Embedded Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-scrollrobot

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestScrollRobot2

*** Test Cases ***
Scroll down
    [Tags]                  smoke                           demo-set
    Verify String Should Not Match    id=verticalScrollLocation       max
    Scroll Vertically       DOWN                            50
    Verify String           id=verticalScrollLocation       max

Scroll up
    [Tags]                  smoke                           demo-set
    Verify String Should Not Match    id=verticalScrollLocation       min
    Scroll Vertically       UP                              50
    Verify String           id=verticalScrollLocation       min

Scroll right
    [Tags]                  smoke                           demo-set
    Skip Test On Linux
    Verify String Should Not Match           id=horizontalScrollLocation     max
    Scroll Horizontally     RIGHT                           50
    Verify String           id=horizontalScrollLocation     max

Scroll left
    [Tags]                  smoke                           demo-set
    Skip Test On Linux
    Verify String Should Not Match           id=horizontalScrollLocation     min
    Scroll Horizontally     LEFT                            50
    Verify String           id=horizontalScrollLocation     min

Scroll down once
    [Tags]                              smoke
    Reset Image If Necessary
    Scroll Vertically                   DOWN                            1
    Verify String Should Not Match      id=verticalScrollLocation       min

Scroll up once
    [Tags]              smoke
    Verify String Should Not Match           id=verticalScrollLocation     min
    Scroll Vertically   UP                             1
    Verify String       id=verticalScrollLocation      min

Scroll Right Once
    [Tags]                              smoke
    Skip Test On Linux
    Verify String      id=horizontalScrollLocation     min
    Scroll Horizontally                 RIGHT                           1
    Verify String Should Not Match      id=horizontalScrollLocation     min

Scroll Left Once
    [Tags]                  smoke
    Skip Test On Linux
    Verify String Should Not Match           id=horizontalScrollLocation     min
    Scroll Horizontally     LEFT                            1
    Verify String           id=horizontalScrollLocation     min

*** Keywords ***
Setup all tests
    Import JavaFXLibrary
    Run Keyword If    ${headless}    Set Tags    monocle-issue
    Launch Javafx Application   ${TEST_APPLICATION}
    Set Screenshot Directory    ${OUTPUT_DIR}${/}report-images
    Move To                     id=scrollPane

Skip Test On Linux
    ${os}                   Get System Property     os.name
    Pass Execution If       '${os}'=='Linux'        This test can not be executed on Linux

Reset Image If Necessary
    ${VERTICAL}         Get Node Text           id=verticalScrollLocation
    ${HORIZONTAl}       Get Node Text           id=horizontalScrollLocation
    Run Keyword If      ${VERTICAL} != min      Reset Image Vertically
    Run Keyword If      ${HORIZONTAl} != min    Reset Image Horizontally

Reset Image Vertically
    Scroll Vertically       UP      50

Reset Image Horizontally
    Scroll Horizontally     LEFT    50

Teardown all tests
    Close Javafx Application

Verify String
    [Documentation]                 Verifies that string is equal in location
    [Arguments]                     ${query}          ${string}
    ${target_node}                  Find              ${query}
    ${text_label}                   Get Node Text     ${target_node}
    Should Be Equal As Strings      ${string}         ${text_label}

Verify String Should Not Match
    [Documentation]                     Verifies that string is not equal in location
    [Arguments]                         ${query}          ${string}
    ${target_node}                      Find              ${query}
    ${text_label}                       Get Node Text     ${target_node}
    Should Not Be Equal As Strings      ${string}         ${text_label}
