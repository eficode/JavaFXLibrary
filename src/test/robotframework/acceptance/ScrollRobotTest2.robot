*** Settings ***
Documentation     Tests to test javafxlibrary.keywords.ScrollRobot related keywords
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-scrollrobot

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestScrollRobot2

*** Test Cases ***
Scroll down
    [Tags]    smoke    demo-set
    Scroll Vertically    DOWN    50
    Verify String        \#verticalScrollLocation    max

Scroll up
    [Tags]    smoke    demo-set
    Scroll Vertically    UP    50
    Verify String        \#verticalScrollLocation    min

Scroll right
    [Tags]    smoke    demo-set
    Scroll Horizontally    RIGHT    50
    Verify String          \#horizontalScrollLocation    max

Scroll left
    [Tags]    smoke    demo-set
    Scroll Horizontally    LEFT    50
    Verify String          \#horizontalScrollLocation    min

Scroll down once
    [Tags]    smoke
    Reset Image If Necessary
    Scroll Vertically                 DOWN    1
    Verify String Should Not Match    \#verticalScrollLocation    min

Scroll up once
    [Tags]    smoke
    Scroll Vertically    UP    1
    Verify String        \#verticalScrollLocation    min

Scroll Right Once
    [Tags]    smoke
    Scroll Horizontally               RIGHT    1
    Verify String Should Not Match    \#horizontalScrollLocation    min

Scroll Left Once
    [Tags]    smoke
    Scroll Horizontally    LEFT    1
    Verify String          \#horizontalScrollLocation    min

*** Keywords ***
Setup all tests
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images
    Move To    \#scrollPane

Reset Image If Necessary
    ${VERTICAL}         Get Node Text    \#verticalScrollLocation
    ${HORIZONTAl}       Get Node Text    \#horizontalScrollLocation
    Run Keyword If      ${VERTICAL} != min    Reset Image Vertically
    Run Keyword If      ${HORIZONTAl} != min    Reset Image Horizontally

Reset Image Vertically
    Scroll Vertically    UP    50

Reset Image Horizontally
    Scroll Horizontally    LEFT    50

Teardown all tests
    Close Javafx Application

Verify String
    [Documentation]    Verifies that string is equal in location
    [Arguments]                   ${query}          ${string}
    ${target_node}=               Find              ${query}
    ${text_label}=                Get Node Text     ${target_node}
    Should Be Equal As Strings    ${string}         ${text_label}

Verify String Should Not Match
    [Documentation]    Verifies that string is equal in location
    [Arguments]                       ${query}          ${string}
    ${target_node}=                   Find              ${query}
    ${text_label}=                    Get Node Text     ${target_node}
    Should Not Be Equal As Strings    ${string}         ${text_label}