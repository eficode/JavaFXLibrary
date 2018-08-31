*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.ScrollRobot related keywords
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Disable Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-scrollrobot

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestScrollRobot
${VERTICAL_TOTAL}     id=totalDistanceVertical
${VERTICAL_ACTUAL}    id=actualDistanceVertical
${VERTICAL_EVENTS}    id=eventsVertical
${HORIZONTAL_TOTAL}   id=totalDistanceHorizontal
${HORIZONTAL_ACTUAL}  id=actualDistanceHorizontal
${HORIZONTAL_EVENTS}  id=eventsHorizontal
${SCROLL_LENGTH}      ${EMPTY}

*** Test Cases ***
Scroll down
    [Tags]                  smoke
    ${TARGET_DISTANCE}      Count Distance          25
    Scroll Vertically       DOWN                    25
    Verify String           ${VERTICAL_TOTAL}       ${TARGET_DISTANCE}
    Verify String           ${VERTICAL_ACTUAL}      -${TARGET_DISTANCE}
    Verify String           ${VERTICAL_EVENTS}      25

Scroll up
    [Tags]                  smoke
    Reset counters
    Move To Vertical Listener
    ${TARGET_DISTANCE}      Count Distance          25
    Scroll Vertically       UP                      25
    Verify String           ${VERTICAL_TOTAL}       ${TARGET_DISTANCE}
    Verify String           ${VERTICAL_ACTUAL}      ${TARGET_DISTANCE}
    Verify String           ${VERTICAL_EVENTS}      25

Scroll Once Vertically
    [Tags]    smoke
    Reset counters
    Move to vertical listener
    :FOR    ${index}    IN RANGE    5
    \       Scroll Vertically       DOWN                    1
    \       Sleep                   50milliseconds
    Verify String                   ${VERTICAL_EVENTS}      5

Scroll Left
    [Tags]                  smoke
    Skip Test On Linux
    Move to horizontal listener
    ${TARGET_DISTANCE}      Count Distance          25
    Scroll Horizontally     LEFT                    25
    Verify String           ${HORIZONTAL_TOTAL}     ${TARGET_DISTANCE}
    Verify String           ${HORIZONTAL_ACTUAL}    ${TARGET_DISTANCE}
    Verify String           ${HORIZONTAL_EVENTS}    25

Scroll Right
    [Tags]                  smoke
    Skip Test On Linux
    Reset counters
    Move to horizontal listener
    ${TARGET_DISTANCE}      Count Distance          10
    Scroll Horizontally     RIGHT                   10
    Verify String           ${HORIZONTAL_TOTAL}     ${TARGET_DISTANCE}
    Verify String           ${HORIZONTAL_ACTUAL}    -${TARGET_DISTANCE}
    Verify String           ${HORIZONTAL_EVENTS}    10

*** Keywords ***
Setup all tests
    Import JavaFXLibrary
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images
    Set Variables
    Move to vertical listener

Skip Test On Linux
    ${os}                   Get System Property     os.name
    Pass Execution If       '${os}'=='Linux'        This test can not be executed on Linux

Move to vertical listener
    Move To     id=greenLabel

Move to horizontal listener
    Move To     id=redLabel

Reset counters
    Click on    id=resetButton

Teardown all tests
    Close Javafx Application

Set Variables
    Move to vertical listener
    Scroll Vertically       DOWN                1
    ${DISTANCE}             Get Node Text       ${VERTICAL_TOTAL}
    Set Suite Variable      ${SCROLL_LENGTH}    ${DISTANCE}
    Reset counters

Count Distance
    [Arguments]    ${WHEEL_TICKS}
    ${DISTANCE}    Evaluate    ${WHEEL_TICKS} * ${SCROLL_LENGTH}
    [Return]       ${DISTANCE}

Verify String
    [Documentation]                 Verifies that string is equal in location
    [Arguments]                     ${query}          ${string}
    ${target_node}                  Find              ${query}
    ${text_label}                   Get Node Text     ${target_node}
    Should Be Equal As Strings      ${string}         ${text_label}
