*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.ClickRobot related keywords
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Setup test case
Test Teardown       Enable Image Logging
Force tags          set-clickrobot

*** Variables ***
${TEST_APPLICATION}         javafxlibrary.testapps.TestClickRobot
${SCENE_MINX}               ${EMPTY}
${SCENE_MINY}               ${EMPTY}
${SCENE_CENTERX}            ${EMPTY}
${SCENE_CENTERY}            ${EMPTY}
${WINDOW_MINX}              ${EMPTY}
${WINDOW_MINY}              ${EMPTY}
${WINDOW_CENTERX}           ${EMPTY}
${WINDOW_CENTERY}           ${EMPTY}

*** Test Cases ***
Click On TextFX Query
    [Tags]                  smoke            demo-set
    Click On                id=button
    Verify String           id=buttonLabel    Button has been clicked 1 times.

Click On ID
    [Tags]                  smoke
    Click On                id=button
    Verify String           id=buttonLabel    Button has been clicked 1 times.

Click On XPath Query
    [Tags]                  smoke
    Click On                xpath=//Button[@id="button"]
    Double Click On         xpath=//LabeledText[@text="Double-click me"]
    Verify String           id=buttonLabel                  Button has been clicked 1 times.
    Verify String           id=doubleClickButtonLabel       Button has been double-clicked 1 times.

Click On Bounds
    [Tags]                  smoke
    ${BUTTON}               Find                id=button
    ${BUTTON_BOUNDS}        Get Bounds          ${BUTTON}
    Click On                ${BUTTON_BOUNDS}
    Verify String           id=buttonLabel      Button has been clicked 1 times.

Click On Scene
    [Tags]                  smoke
    ${SCENE}                Get Scene               id=button
    Click On                ${SCENE}
    Verify String           id=coordinateLabel      click X${SCENE_CENTERX} Y${SCENE_CENTERY}

Click On Window
    [Tags]                  smoke
    ${WINDOW}               Get Window            ClickRobot Test
    Click On                ${WINDOW}
    Verify String           id=coordinateLabel    click X${WINDOW_CENTERX} Y${WINDOW_CENTERY}

Click On Point
    [Tags]                  smoke
    ${X}                    Evaluate                ${SCENE_MINX} + ${250}
    ${Y}                    Evaluate                ${SCENE_MINY} + ${150}
    ${POINT}                Create Point            ${X}    ${Y}
    Click On                ${POINT}
    Verify String           id=coordinateLabel      click X${X} Y${Y}

Click On Node
    [Tags]                  smoke
    ${NODE}                 Find                id=button
    Click On                ${NODE}
    Verify String           id=buttonLabel      Button has been clicked 1 times.

Click On Point Query
    [Tags]                  smoke
    ${POINTQUERY}           Point To            id=button
    Click On                ${POINTQUERY}
    Verify String           id=buttonLabel      Button has been clicked 1 times.

Double Click On Query
    [Tags]                  smoke    demo-set
    Double Click On         id=doubleClickButton
    Verify String           id=doubleClickButtonLabel    Button has been double-clicked 1 times.

Double Click On Bounds
    [Tags]                  smoke
    ${BUTTON}               Find                            id=doubleClickButton
    ${BUTTON_BOUNDS}        Get Bounds                      ${BUTTON}
    Double Click On         ${BUTTON_BOUNDS}
    Verify String           id=doubleClickButtonLabel       Button has been double-clicked 1 times.

Double Click On Scene
    [Tags]                  smoke
    ${SCENE}                Get Scene               id=doubleClickButton
    Double Click On         ${SCENE}
    Verify String           id=coordinateLabel      doubleclick X${SCENE_CENTERX} Y${SCENE_CENTERY}

Double Click On Window
    [Tags]                  smoke
    ${WINDOW}               Get Window              ClickRobot Test
    Double Click On         ${WINDOW}
    Verify String           id=coordinateLabel      doubleclick X${WINDOW_CENTERX} Y${WINDOW_CENTERY}

Double Click On Point
    [Tags]                  smoke
    ${X}                    Evaluate                ${SCENE_MINX} + ${200}
    ${Y}                    Evaluate                ${SCENE_MINY} + ${150}
    ${POINT}                Create Point            ${X}    ${Y}
    Double Click On         ${POINT}
    Verify String           id=coordinateLabel      doubleclick X${X} Y${Y}

Double Click On Node
    [Tags]                  smoke
    ${NODE}                 Find                        id=doubleClickButton
    Double Click On         ${NODE}
    Verify String           id=doubleClickButtonLabel   Button has been double-clicked 1 times.

Double Click On Point Query
    [Tags]                  smoke
    ${POINTQUERY}           Point To                        id=doubleClickButton
    Double Click On         ${POINTQUERY}
    Verify String           id=doubleClickButtonLabel       Button has been double-clicked 1 times.

Right Click On Query
    [Tags]                  smoke    demo-set
    Right Click On          id=rightClickButton
    Verify String           id=rightClickButtonLabel    Button has been right-clicked 1 times.

Right Click On Bounds
    [Tags]                  smoke
    ${BUTTON}               Find                        id=rightClickButton
    ${BUTTON_BOUNDS}        Get Bounds                  ${BUTTON}
    Right Click On          ${BUTTON_BOUNDS}
    Verify String           id=rightClickButtonLabel    Button has been right-clicked 1 times.

Right Click On Scene
    [Tags]                  smoke
    ${SCENE}                Get Scene               id=rightClickButton
    Right Click On          ${SCENE}
    Verify String           id=coordinateLabel      rightclick X${SCENE_CENTERX} Y${SCENE_CENTERY}

Right Click On Window
    [Tags]                  smoke
    ${WINDOW}               Get Window              ClickRobot Test
    Right Click On          ${WINDOW}
    Verify String           id=coordinateLabel      rightclick X${WINDOW_CENTERX} Y${WINDOW_CENTERY}

Right Click On Point
    [Tags]                  smoke
    ${X}                    Evaluate                ${SCENE_MINX} + ${175}
    ${Y}                    Evaluate                ${SCENE_MINY} + ${175}
    ${POINT}                Create Point            ${X}    ${Y}
    Right Click On          ${POINT}
    Verify String           id=coordinateLabel      rightclick X${X} Y${Y}

Right Click On Node
    [Tags]                  smoke
    ${NODE}                 Find                        id=rightClickButton
    Right Click On          ${NODE}
    Verify String           id=rightClickButtonLabel    Button has been right-clicked 1 times.

Right Click On Point Query
    [Tags]                  smoke
    ${POINTQUERY}           Point To                    id=rightClickButton
    Right Click On          ${POINTQUERY}
    Verify String           id=rightClickButtonLabel    Button has been right-clicked 1 times.

Click On Mouse Button (Primary)
    [Tags]                          smoke
    Move To                         id=button
    Click On Mouse Button           PRIMARY
    Verify String                   id=buttonLabel    Button has been clicked 1 times.

Click On Mouse Button (Secondary)
    [Tags]                          smoke
    Move To                         id=rightClickButton
    Click On Mouse Button           SECONDARY
    Verify String                   id=rightClickButtonLabel    Button has been right-clicked 1 times.

Double Click On Mouse Button
    [Tags]                          smoke
    Move To                         id=doubleClickButton
    Double Click On Mouse Button    PRIMARY
    Verify String                   id=doubleClickButtonLabel    Button has been double-clicked 1 times.

Right Click On Mouse Button
    [Tags]                          smoke
    Move To                         id=rightClickButton
    Right Click On Mouse Button
    Verify String                   id=rightClickButtonLabel    Button has been right-clicked 1 times.

Click On Coordinates
    [Tags]                          smoke                   demo-set
    ${COORD_X}                      Evaluate                ${SCENE_MINX} + ${150}
    ${COORD_Y}                      Evaluate                ${SCENE_MINY} + ${150}
    ${COORD_X}                      Convert To Integer      ${COORD_X}
    ${COORD_Y}                      Convert To Integer      ${COORD_Y}
    Click On Coordinates            ${COORD_X}              ${COORD_Y}
    Verify String                   id=coordinateLabel      click X${COORD_X} Y${COORD_Y}

Double Click On Coordinates
    [Tags]                          smoke
    ${COORD_X}                      Evaluate                ${SCENE_MINX} + ${300}
    ${COORD_Y}                      Evaluate                ${SCENE_MINY} + ${150}
    ${COORD_X}                      Convert To Integer      ${COORD_X}
    ${COORD_Y}                      Convert To Integer      ${COORD_Y}
    Double Click On Coordinates     ${COORD_X}              ${COORD_Y}
    Verify String                   id=coordinateLabel      doubleclick X${COORD_X} Y${COORD_Y}

Right Click On Coordinates
    [Tags]                          smoke
    ${COORD_X}                      Evaluate                ${SCENE_MINX} + ${160}
    ${COORD_Y}                      Evaluate                ${SCENE_MINY} + ${160}
    ${COORD_X}                      Convert To Integer      ${COORD_X}
    ${COORD_Y}                      Convert To Integer      ${COORD_Y}
    Right Click On Coordinates      ${COORD_X}              ${COORD_Y}
    Verify String                   id=coordinateLabel      rightclick X${COORD_X} Y${COORD_Y}

Click On ID That Does Not Exist
    [Tags]                  smoke    negative
    ${MSG}                  Run Keyword And Expect Error    *    Click On    id=idThatDoesNotExist
    Should Be Equal         ${MSG}    Given element "id=idThatDoesNotExist" was not found within given timeout of 1 SECONDS

Click On Unreachable Point
    [Tags]                  smoke    negative
    ${POINT}                Create Point    ${0}    ${-20}
    ${MSG}                  Run Keyword And Expect Error    *    Click On    ${POINT}
    Should Start With       ${MSG}    Can't click Point2D at [0.0, -20.0]: out of window bounds.

Click On Unreachable Coordinates
    [Tags]                  smoke    negative
    ${MSG}                  Run Keyword And Expect Error    *    Click On Coordinates    ${0}    ${-20}
    Should Start With       ${MSG}    Can't click Point2D at [0.0, -20.0]: out of window bounds.

Click On Mouse Button That Does Not Exist
    [Tags]                  smoke    negative
    ${MSG}                  Run Keyword And Expect Error    *    Click On Mouse Button    THE HUGE RED ONE
    Should Start With       ${MSG}    "THE HUGE RED ONE" is not a valid MouseButton. Accepted values are:

Click On Using Motion That Does Not Exist
    [Tags]                  smoke    negative
    ${MSG}                  Run Keyword And Expect Error    *    Click On    id=button    ZIGZAG
    Should Start With       ${MSG}    "ZIGZAG" is not a valid Motion. Accepted values are:

Click On ID That Does Not Exist With Safe Clicking Off
    [Tags]                  smoke    negative
    Set Safe Clicking       OFF
    ${MSG}                  Run Keyword And Expect Error    *    Click On    id=idThatDoesNotExist
    Set Safe Clicking       ON
    Should Be Equal         ${MSG}    Given element "id=idThatDoesNotExist" was not found within given timeout of 1 SECONDS

Click On Unsupported Type
    [Tags]                  smoke    negative
    ${NODE}                 Find                  id=button
    ${IMAGE}                Capture Image         ${NODE}
    ${PIXELREADER}          Call Object Method    ${IMAGE}    getPixelReader
    ${MSG}                  Run Keyword And Expect Error    *    Click On       ${PIXELREADER}
    Should Start With       ${MSG}    Unsupported parameter type:

*** Keywords ***
Setup all tests
    Import JavaFXLibrary
    Launch Javafx Application       ${TEST_APPLICATION}
    Set Screenshot Directory        ${OUTPUT_DIR}${/}report-images
    Set Scene Values
    Set Window Values
    Set Timeout                     1

Setup test case
    Reset Counters
    Disable Embedded Image Logging For Negative Tests

Teardown all tests
    Close Javafx Application

Set Scene Values
    ${SCENE}                        Get Scene               id=button
    ${BOUNDS}                       Get Bounds              ${SCENE}
    ${MIN_X}                        Call Object Method      ${BOUNDS}    getMinX
    ${MIN_Y}                        Call Object Method      ${BOUNDS}    getMinY
    ${WIDTH}                        Call Object Method      ${BOUNDS}    getWidth
    ${HEIGHT}                       Call Object Method      ${BOUNDS}    getHeight
    ${MIN_X}                        Convert To Integer      ${MIN_X}
    ${MIN_Y}                        Convert To Integer      ${MIN_Y}
    ${CENTERX}                      Evaluate                ${MIN_X} + (${WIDTH} / 2)
    ${CENTERY}                      Evaluate                ${MIN_Y} + (${HEIGHT} / 2)
    ${CENTERX}                      Convert To Integer      ${CENTERX}
    ${CENTERY}                      Convert To Integer      ${CENTERY}
    Set Suite Variable              ${SCENE_MINY}           ${MIN_Y}
    Set Suite Variable              ${SCENE_MINX}           ${MIN_X}
    Set Suite Variable              ${SCENE_CENTERX}        ${CENTERX}
    Set Suite Variable              ${SCENE_CENTERY}        ${CENTERY}

Set Window Values
    ${WINDOW}                       Get Window              ClickRobot Test
    ${BOUNDS}                       Get Bounds              ${WINDOW}
    ${MIN_X}                        Call Object Method      ${BOUNDS}    getMinX
    ${MIN_Y}                        Call Object Method      ${BOUNDS}    getMinY
    ${WIDTH}                        Call Object Method      ${BOUNDS}    getWidth
    ${HEIGHT}                       Call Object Method      ${BOUNDS}    getHeight
    ${MIN_X}                        Convert To Integer      ${MIN_X}
    ${MIN_Y}                        Convert To Integer      ${MIN_Y}
    ${CENTERX}                      Evaluate                ${MIN_X} + (${WIDTH} / 2)
    ${CENTERY}                      Evaluate                ${MIN_Y} + (${HEIGHT} / 2)
    ${CENTERX}                      Convert To Integer      ${CENTERX}
    ${CENTERY}                      Convert To Integer      ${CENTERY}
    Set Suite Variable              ${WINDOW_MINY}          ${MIN_Y}
    Set Suite Variable              ${WINDOW_MINX}          ${MIN_X}
    Set Suite Variable              ${WINDOW_CENTERX}       ${CENTERX}
    Set Suite Variable              ${WINDOW_CENTERY}       ${CENTERY}

Reset Counters
    Click On         id=resetButton
    Verify String    id=buttonLabel               Button has been clicked 0 times.
    Verify String    id=rightClickButtonLabel     Button has been right-clicked 0 times.
    Verify String    id=doubleClickButtonLabel    Button has been double-clicked 0 times.

Verify String
    [Documentation]                 Verifies that string is equal in location
    [Arguments]                     ${query}          ${string}
    ${target_node}                  Find              ${query}
    ${text_label}                   Get Node Text     ${target_node}
    Should Be Equal As Strings      ${string}         ${text_label}
