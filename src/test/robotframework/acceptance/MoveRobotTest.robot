*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.MoveRobot related keywords
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Setup test case
Test Teardown       Enable Image Logging
Force Tags          set-moverobot

*** Variables ***
${TEST_APPLICATION}         javafxlibrary.testapps.TestPointLocation
${SCENE_MINX}               ${EMPTY}
${SCENE_MINY}               ${EMPTY}
${L_DECORATION_WIDTH}       ${EMPTY}
${L_DECORATION_WIDTH}       ${EMPTY}
${T_DECORATION_HEIGHT}      ${EMPTY}
${B_DECORATION_HEIGHT}      ${EMPTY}

*** Test Cases ***
Move By
    [Tags]                  smoke                   demo-set
    Move By                 75                      75
    Verify String           id=locationLabel        75 | 75

Move To Coordinates
    [Tags]                  smoke                           demo-set                     negative
    ${X}                    Evaluate                        ${SCENE_MINX} + ${200}
    ${Y}                    Evaluate                        ${SCENE_MINY} + ${200}
    Move To Coordinates     ${X}                            ${Y}
    Verify String           id=locationLabel                200 | 200
    ${MSG}                  Run Keyword And Expect Error    *    Move To Coordinates     ${X}    ${Y}    NotValidMotion
    Should Contain          ${MSG}                          "NotValidMotion" is not a valid Motion. Accepted values are:

Move To Query
    [Tags]                  smoke               demo-set
    Move To                 id=rectangle
    Verify String           id=locationLabel    25 | 475

Move To XPath Query
    [Tags]                  smoke
    Move To                 xpath=//Rectangle[@id="rectangle"]
    Verify String           id=locationLabel    25 | 475

Move To ID Query
    [Tags]                  smoke
    Move To                 id=rectangle
    Verify String           id=locationLabel    25 | 475

Move To Point Query
    [Tags]                  smoke
    ${POINTQUERY}           Point To            id=rectangle
    Move To                 ${POINTQUERY}
    Verify String           id=locationLabel    25 | 475

Move To Point
    [Tags]                  smoke
    ${X}                    Evaluate            ${400} + ${SCENE_MINX}
    ${Y}                    Evaluate            ${150} + ${SCENE_MINY}
    ${POINT}                Create Point        ${X}    ${Y}
    Move To                 ${POINT}
    Verify String           id=locationLabel    400 | 150

Move To Bounds
    [Tags]                  smoke
    ${NODE}                 Find                id=rectangle
    ${BOUNDS}               Get Bounds          ${NODE}
    Move To                 ${BOUNDS}
    Verify String           id=locationLabel    25 | 475

Move To Scene
    [Tags]                  smoke
    ${SCENE}                Get Scene           id=rectangle
    Move To                 ${SCENE}
    Verify String           id=locationLabel    250 | 250

Move To Window
    [Tags]                  smoke
    ${WINDOW}               Get Window              PointLocation Test
    Move To                 ${WINDOW}
    ${WIDTH_OFFSET}         Evaluate                (${L_DECORATION_WIDTH} - ${R_DECORATION_WIDTH}) / 2
    ${HEIGHT_OFFSET}        Evaluate                (${T_DECORATION_HEIGHT} - ${B_DECORATION_HEIGHT}) / 2
    ${X}                    Evaluate                ${250} - ${WIDTH_OFFSET}
    ${Y}                    Evaluate                ${250} - ${HEIGHT_OFFSET}
    ${X}                    Convert To Integer      ${X}
    ${Y}                    Convert To Integer      ${Y}
    Verify String           id=locationLabel        ${X} | ${Y}

Move To Nonexistent Location
    [Tags]                           smoke
    Run Keyword And Expect Error     Given locator "id=rectangleNOTfound" was not found.
    ...                              Move To                 id=rectangleNOTfound

*** Keywords ***
Setup all tests
    Import JavaFXLibrary
    Launch Javafx Application       ${TEST_APPLICATION}
    Set Screenshot Directory        ${OUTPUT_DIR}${/}report-images
    Set Scene Bounds Values
    Set Decoration Values

Setup test case
    Move To Top Left Corner
    Disable Embedded Image Logging For Negative Tests

Teardown all tests
    Close Javafx Application

Move To Top Left Corner
    Move To Coordinates     ${SCENE_MINX}    ${SCENE_MINY}

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
    ${HEIGHT}               Call Object Method      ${SCENE}        getY
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

Set Scene Bounds Values
    ${SCENE}                Get Scene                       id=rectangle
    ${BOUNDS}               Get Bounds                      ${SCENE}
    ${MIN_X}                Call Object Method              ${BOUNDS}    getMinX
    ${MIN_Y}                Call Object Method              ${BOUNDS}    getMinY
    ${MIN_X}                Convert To Integer              ${MIN_X}
    ${MIN_Y}                Convert To Integer              ${MIN_Y}
    Set Suite Variable      ${SCENE_MINX}                   ${MIN_X}
    Set Suite Variable      ${SCENE_MINY}                   ${MIN_Y}

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
