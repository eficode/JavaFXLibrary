*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.BoundsLocation related keywords
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Disable Embedded Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-boundslocation

*** Variables ***
${TEST_APPLICATION}         javafxlibrary.testapps.TestBoundsLocation
${X_OFFSET}                 ${EMPTY}
${Y_OFFSET}                 ${EMPTY}
${L_DECORATION_WIDTH}       ${EMPTY}
${L_DECORATION_WIDTH}       ${EMPTY}
${T_DECORATION_HEIGHT}      ${EMPTY}
${B_DECORATION_HEIGHT}      ${EMPTY}

*** Test Cases ***
Get Window Bounds
    [Tags]                      smoke
    ${SCREEN_BOUNDS}            Get Primary Screen Bounds
    ${WINDOW}                   Get Window              BoundsLocation Test
    Set Decoration Values       ${WINDOW}
    ${WIDTH}                    Evaluate                ${L_DECORATION_WIDTH}+${R_DECORATION_WIDTH}+${600}
    ${HEIGHT}                   Evaluate                ${T_DECORATION_HEIGHT}+${B_DECORATION_HEIGHT}+${600}
    ${TARGET_BOUNDS}            Create Bounds           ${X_OFFSET}    ${Y_OFFSET}    ${WIDTH}    ${HEIGHT}
    ${WINDOW_BOUNDS}            Get Bounds              ${WINDOW}
    Bounds Should Be Equal      ${WINDOW_BOUNDS}        ${TARGET_BOUNDS}

Get Scene Bounds
    [Tags]                      smoke
    ${SCENE}                    Get Scene           id=blue
    ${TARGET_X}                 Evaluate            ${X_OFFSET} + ${L_DECORATION_WIDTH}
    ${TARGET_Y}                 Evaluate            ${Y_OFFSET} + ${T_DECORATION_HEIGHT}
    ${TARGET_BOUNDS}            Create Bounds       ${TARGET_X}    ${TARGET_Y}    600    600
    ${SCENE_BOUNDS}             Get Bounds          ${SCENE}
    Bounds Should Be Equal      ${SCENE_BOUNDS}     ${TARGET_BOUNDS}

Get Node Bounds (BLUE)
    [Tags]                      smoke
    ${BLUE}                     Find                id=blue
    ${NODE_BOUNDS}              Get Bounds          ${BLUE}
    ${TARGET_Y}                 Evaluate            ${Y_OFFSET} + ${T_DECORATION_HEIGHT} + ${300}
    ${TARGET_X}                 Evaluate            ${X_OFFSET} + ${L_DECORATION_WIDTH}
    ${TARGET_BOUNDS}            Create Bounds       ${TARGET_X}   ${TARGET_Y}    600    300
    Bounds Should Be Equal      ${NODE_BOUNDS}      ${TARGET_BOUNDS}

Get Node Bounds (PINK)
    [Tags]                      smoke
    ${PINK}                     Find                id=pink
    ${NODE_BOUNDS}              Get Bounds          ${PINK}
    ${TARGET_X}                 Evaluate            ${X_OFFSET} + ${L_DECORATION_WIDTH} + ${450}
    ${TARGET_Y}                 Evaluate            ${Y_OFFSET} + ${T_DECORATION_HEIGHT} + ${75}
    ${TARGET_BOUNDS}            Create Bounds       ${TARGET_X}   ${TARGET_Y}    75    75
    Bounds Should Be Equal      ${NODE_BOUNDS}      ${TARGET_BOUNDS}

Get Point Bounds
    [Tags]                      smoke
    ${POINT}                    Create Point        150         150
    ${BOUNDS}                   Get Bounds          ${POINT}
    ${TARGET}                   Create Bounds       150         150    0    0
    Bounds Should Be Equal      ${BOUNDS}           ${TARGET}

Get Query Bounds
    [Tags]                      smoke
    ${BOUNDS}                   Get Bounds          id=blue
    ${TARGET_Y}                 Evaluate            ${Y_OFFSET} + ${T_DECORATION_HEIGHT} + ${300}
    ${TARGET_X}                 Evaluate            ${X_OFFSET} + ${L_DECORATION_WIDTH}
    ${TARGET}                   Create Bounds       ${TARGET_X}   ${TARGET_Y}    600    300
    Bounds Should Be Equal      ${BOUNDS}           ${TARGET}

Get Bounds Using XPath Query
    [Tags]                      smoke
    ${BOUNDS}                   Get Bounds          xpath=//Rectangle[@id="blue"]
    ${TARGET_Y}                 Evaluate            ${Y_OFFSET} + ${T_DECORATION_HEIGHT} + ${300}
    ${TARGET_X}                 Evaluate            ${X_OFFSET} + ${L_DECORATION_WIDTH}
    ${TARGET}                   Create Bounds       ${TARGET_X}   ${TARGET_Y}    600    300
    Bounds Should Be Equal      ${BOUNDS}          ${TARGET}

Compare Different Size Bounds
    [Tags]                      smoke
    ${BOUNDS_BLUE}              Get Bounds          id=blue
    ${BOUNDS_GREEN}             Get Bounds          id=green
    Bounds Should Not Be Equal  ${BOUNDS_BLUE}      ${BOUNDS_GREEN}

Get Bounds Of Id That Does Not Exist
    [Tags]              smoke    negative
    ${MSG}              Run Keyword And Expect Error    *    Get Bounds    id=idThatDoesNotExist
    Should Be Equal     ${MSG}    Given element "id=idThatDoesNotExist" was not found within given timeout of 1 SECONDS

*** Keywords ***
Setup all tests
    Import JavaFXLibrary
    Set Timeout                     1
    Launch Javafx Application       ${TEST_APPLICATION}
    Set Screenshot Directory        ${OUTPUT_DIR}${/}report-images
    Set Offsets

Teardown all tests
    Close Javafx Application


Set Offsets
    ${SCREEN_BOUNDS}        Get Primary Screen Bounds
    ${XOFF}                 Call Object Method              ${SCREEN_BOUNDS}    getMinX
    ${YOFF}                 Call Object Method              ${SCREEN_BOUNDS}    getMinY
    Set Suite Variable      ${X_OFFSET}                     ${XOFF}
    Set Suite Variable      ${Y_OFFSET}                     ${YOFF}

Set Decoration Values
    [Arguments]             ${WINDOW}
    ${LEFT_WIDTH}           Get Left Decoration Width       ${WINDOW}
    ${RIGHT_WIDTH}          Get Right Decoration Width      ${WINDOW}
    ${TOP_HEIGHT}           Get Top Decoration Height       ${WINDOW}
    ${BOTTOM_HEIGHT}        Get Bottom Decoration Height    ${WINDOW}
    Set Suite Variable      ${L_DECORATION_WIDTH}           ${LEFT_WIDTH}
    Set Suite Variable      ${R_DECORATION_WIDTH}           ${RIGHT_WIDTH}
    Set Suite Variable      ${T_DECORATION_HEIGHT}          ${TOP_HEIGHT}
    Set Suite Variable      ${B_DECORATION_HEIGHT}          ${BOTTOM_HEIGHT}

Get Left Decoration Width
    [Arguments]             ${WINDOW}
    ${ROOT}                 Get Root Node Of        ${WINDOW}
    ${SCENE}                Get Scene               ${ROOT}
    ${WIDTH}                Call Object Method      ${SCENE}        getX
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
