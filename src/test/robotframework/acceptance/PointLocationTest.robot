*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.PointLocation and PointOffset related keywords
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Disable Embedded Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-pointlocation    set-pointoffset

*** Variables ***
${TEST_APPLICATION}         javafxlibrary.testapps.TestPointLocation
${L_DECORATION_WIDTH}       ${EMPTY}
${L_DECORATION_WIDTH}       ${EMPTY}
${T_DECORATION_HEIGHT}      ${EMPTY}
${B_DECORATION_HEIGHT}      ${EMPTY}

*** Test Cases ***
Point To Node
    [Tags]                  smoke
    Move To Center
    ${NODE}                 Find                id=rectangle
    ${POINTQUERY}           Point To            ${NODE}
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel    25 | 475

Point To Bounds
    [Tags]                  smoke
    Move To Center
    ${NODE}                 Find                id=rectangle
    ${BOUNDS}               Get Bounds          ${NODE}
    ${POINTQUERY}           Point To            ${BOUNDS}
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel    25 | 475

Point To Scene
    [Tags]                  smoke
    Move To Top Left Corner
    ${SCENE}                Get Scene           id=rectangle
    ${POINTQUERY}           Point To            ${SCENE}
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel    250 | 250

Point To Coordinates
    [Tags]                  smoke
    Move To Top Left Corner
    ${SCENE}                Get Scene               id=rectangle
    ${SCENE_BOUNDS}         Get Bounds              ${SCENE}
    ${MINX}                 Call Object Method      ${SCENE_BOUNDS}    getMinX
    ${MINY}                 Call Object Method      ${SCENE_BOUNDS}    getMinY
    ${X}                    Evaluate                ${MINX} + ${475}
    ${Y}                    Evaluate                ${MINY} + ${475}
    ${X}                    Convert To Integer      ${X}
    ${Y}                    Convert To Integer      ${Y}
    Move To Coordinates     ${X}                    ${Y}
    Verify String           id=locationLabel        475 | 475

Point To Point
    [Tags]                  smoke
    Move To Top Left Corner
    ${SCENE}                Get Scene               id=rectangle
    ${SCENE_BOUNDS}         Get Bounds              ${SCENE}
    ${MINX}                 Call Object Method      ${SCENE_BOUNDS}    getMinX
    ${MINY}                 Call Object Method      ${SCENE_BOUNDS}    getMinY
    ${X}                    Evaluate                ${MINX} + ${475}
    ${Y}                    Evaluate                ${MINY} + ${125}
    ${POINT}                Create Point            ${X}    ${Y}
    ${POINTQUERY}           Point To                ${POINT}
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel        475 | 125

Point To Window
    [Tags]                  smoke
    Move To Top Left Corner
    ${WINDOW}               Get Window              PointLocation Test
    ${POINTQUERY}           Point To                ${WINDOW}
    Move To                 ${POINTQUERY}
    ${WIDTH_OFFSET}         Evaluate                (${L_DECORATION_WIDTH} - ${R_DECORATION_WIDTH}) / 2
    ${HEIGHT_OFFSET}        Evaluate                (${T_DECORATION_HEIGHT} - ${B_DECORATION_HEIGHT}) / 2
    ${X}                    Evaluate                ${250} - ${WIDTH_OFFSET}
    ${Y}                    Evaluate                ${250} - ${HEIGHT_OFFSET}
    ${X}                    Convert To Integer      ${X}
    ${Y}                    Convert To Integer      ${Y}
    Verify String           id=locationLabel        ${X} | ${Y}

Point To Query
    [Tags]                  smoke
    Move To Top Left Corner
    ${POINTQUERY}           Point To            id=rectangle
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel    25 | 475

Point To XPath Query
    [Tags]                  smoke
    Move To Top Left Corner
    ${POINTQUERY}           Point To            xpath=//Rectangle[@id="rectangle"]
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel    25 | 475

Point To Class
    [Tags]                  smoke
    Move To Top Left Corner
    ${POINTQUERY}           Point To            class=javafx.scene.shape.Rectangle
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel    25 | 475

Point To Empty String
    [Tags]                  smoke
    Move To Top Left Corner
    ${MSG}=                 Run Keyword And Expect Error    *          Point To            ${EMPTY}
    Should Start With       ${MSG}                          Illegal arguments for keyword 'pointTo'

# PointOffset test cases
Point To Point With Offset
    [Tags]                  smoke
    Move To Top Left Corner
    ${SCENE}                Get Scene               id=rectangle
    ${SCENE_BOUNDS}         Get Bounds              ${SCENE}
    ${POINT}                Create Point            ${475}              ${125}
    ${MINX}                 Call Object Method      ${SCENE_BOUNDS}     getMinX
    ${MINY}                 Call Object Method      ${SCENE_BOUNDS}     getMinY
    ${POINTQUERY}           Point To With Offset    ${POINT}            ${MINX}    ${MINY}
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel        475 | 125

Point To Bounds With Offset
    [Tags]                  smoke
    Move To Top Left Corner
    ${NODE}                 Find                    id=rectangle
    ${BOUNDS}               Get Bounds              ${NODE}
    ${POINTQUERY}           Point To With Offset    ${BOUNDS}    0    -25
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel        25 | 450

Point To Node With Offset
    [Tags]                  smoke
    Move To Top Left Corner
    ${NODE}                 Find                    id=rectangle
    ${POINTQUERY}           Point To With Offset    ${NODE}    -15    15
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel        10 | 490

Point To Scene With Offset
    [Tags]                  smoke
    Move To Top Left Corner
    ${SCENE}                Get Scene               id=rectangle
    ${POINTQUERY}           Point To With Offset    ${SCENE}    -50    50
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel        200 | 300

Point To Window With Offset
    [Tags]                  smoke
    Move To Top Left Corner
    ${WINDOW}               Get Window              PointLocation Test
    ${WIDTH_OFFSET}         Evaluate                (${L_DECORATION_WIDTH} - ${R_DECORATION_WIDTH}) / 2
    ${HEIGHT_OFFSET}        Evaluate                (${T_DECORATION_HEIGHT} - ${B_DECORATION_HEIGHT}) / 2
    ${POINTQUERY}           Point To With Offset    ${WINDOW}    ${WIDTH_OFFSET}    ${HEIGHT_OFFSET}
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel        250 | 250

Point To Query With Offset
    [Tags]                  smoke
    Move To Top Left Corner
    ${POINTQUERY}           Point To With Offset    id=rectangle    50    -50
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel        75 | 425

Point To XPath Query With Offset
    [Tags]                  smoke
    Move To Top Left Corner
    ${POINTQUERY}           Point To With Offset    xpath=//Rectangle[@id="rectangle"]    50    -50
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel        75 | 425

Set New Target Position
    [Tags]                  smoke    negative
    ${MSG}                  Run Keyword And Expect Error    *    Set Target Position     UP
    Should Contain          ${MSG}    Position: "UP" is not a valid position. Accepted values are:

*** Keywords ***
Setup all tests
    Import JavaFXLibrary
    Launch Javafx Application       ${TEST_APPLICATION}
    Set Screenshot Directory        ${OUTPUT_DIR}${/}report-images
    Set Decoration Values

Teardown all tests
    Close Javafx Application

Move To Top Left Corner
    ${SCENE}                Get Scene               id=rectangle
    ${SCENE_BOUNDS}         Get Bounds              ${SCENE}
    ${MINX}                 Call Object Method      ${SCENE_BOUNDS}    getMinX
    ${MINY}                 Call Object Method      ${SCENE_BOUNDS}    getMinY
    ${X}                    Convert To Integer      ${MINX}
    ${Y}                    Convert To Integer      ${MINY}
    Move To Coordinates     ${X}                    ${Y}

Move To Center
    ${SCENE}                Get Scene               id=rectangle
    ${POINTQUERY}           Point To                ${SCENE}
    Move To                 ${POINTQUERY}

Get Left Decoration Width
    [Arguments]             ${WINDOW}
    ${ROOT}                 Get Root Node Of        ${WINDOW}
    ${SCENE}                Get Scene               ${ROOT}
    ${WIDTH}                Call Object Method      ${SCENE}    getX
    [Return]                ${WIDTH}

Get Right Decoration Width
    [Arguments]             ${WINDOW}
    ${ROOT}                 Get Root Node Of        ${WINDOW}
    ${SCENE}                Get Scene               ${ROOT}
    ${WINDOWWIDTH}          Call Object Method      ${WINDOW}       getWidth
    ${SCENEX}               Call Object Method      ${SCENE}        getX
    ${SCENEWIDTH}           Call Object Method      ${SCENE}        getWidth
    ${DECOWIDTH}            Evaluate                ${WINDOWWIDTH} - ${SCENEWIDTH} - ${SCENEX}
    [Return]                ${DECOWIDTH}

Get Top Decoration Height
    [Arguments]             ${WINDOW}
    ${ROOT}                 Get Root Node Of        ${WINDOW}
    ${SCENE}                Get Scene               ${ROOT}
    ${HEIGHT}               Call Object Method      ${SCENE}    getY
    [Return]                ${HEIGHT}

Get Bottom Decoration Height
    [Arguments]             ${WINDOW}
    ${ROOT}                 Get Root Node Of        ${WINDOW}
    ${SCENE}                Get Scene               ${ROOT}
    ${WINDOWHEIGHT}         Call Object Method      ${WINDOW}       getHeight
    ${SCENEY}               Call Object Method      ${SCENE}        getY
    ${SCENEHEIGHT}          Call Object Method      ${SCENE}        getHeight
    ${DECOHEIGHT}           Evaluate                ${WINDOWHEIGHT} - ${SCENEHEIGHT} - ${SCENEY}
    [Return]                ${DECOHEIGHT}

Set Decoration Values
    ${WINDOW}               Get Window                      PointLocation Test
    ${LEFT_WIDTH}           Get Left Decoration Width       ${WINDOW}
    ${RIGHT_WIDTH}          Get Right Decoration Width      ${WINDOW}
    ${TOP_HEIGHT}           Get Top Decoration Height       ${WINDOW}
    ${BOTTOM_HEIGHT}        Get Bottom Decoration Height    ${WINDOW}
    Set Suite Variable      ${L_DECORATION_WIDTH}           ${LEFT_WIDTH}
    Set Suite Variable      ${R_DECORATION_WIDTH}           ${RIGHT_WIDTH}
    Set Suite Variable      ${T_DECORATION_HEIGHT}          ${TOP_HEIGHT}
    Set Suite Variable      ${B_DECORATION_HEIGHT}          ${BOTTOM_HEIGHT}

Verify String
    [Documentation]                 Verifies that string is equal in location
    [Arguments]                     ${query}          ${string}
    ${target_node}                  Find              ${query}
    ${text_label}                   Get Node Text     ${target_node}
    Should Be Equal As Strings      ${string}         ${text_label}
