*** Settings ***
Documentation     Tests to test javafxlibrary.keywords.DragRobot related keywords
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force tags        set-dragrobot

*** Variables ***
${TEST_APPLICATION}     javafxlibrary.testapps.TestDragRobot
${SCENE_BOUNDS}         ${EMPTY}
${COORD_X}              ${EMPTY}
${COORD_Y}              ${EMPTY}

*** Test Cases ***
Drop by left
    [Tags]                      smoke
    Drag From                   \#horizontalSlider .thumb
    Drop By                     -300    0
    Verify String               \#sliderLabel    0

Drop by up
    [Tags]                      smoke
    Drag From                   \#verticalSlider .thumb
    Drop By                     0    -200
    Verify String               \#verticalSliderLabel    100

Drop by right
    [Tags]                      smoke
    Drag From                   \#horizontalSlider .thumb
    Drop By                     500    0
    Verify String               \#sliderLabel    100

Drop by down
    [Tags]                      smoke
    Drag From                   \#verticalSlider .thumb
    Drop By                     0    150
    Verify String               \#verticalSliderLabel    0

Drag
    [Tags]                      smoke
    Set Coordinates To Use      240    210
    Move To                     \#circle
    Drag
    Drop To Coordinates         ${COORD_X}    ${COORD_Y}
    Verify String               \#circleScreenLocationLabel    X${COORD_X} Y${COORD_Y}

Drag, Drop
    [Tags]                      smoke
    Set Coordinates To Use      700    600
    Reset Circle
    Move To                     \#circle
    Drag
    Move To Coordinates         ${COORD_X}    ${COORD_Y}
    Drop
    Verify String               \#circleScreenLocationLabel    X${COORD_X} Y${COORD_Y}

Drag From Node (Node only)
    [Tags]                      smoke
    Reset Circle
    Set Coordinates To Use      125    375
    ${CIRCLE}    Find           \#circle
    Drag From                   ${CIRCLE}
    Drop To Coordinates         ${COORD_X}    ${COORD_Y}
    Verify String               \#circleScreenLocationLabel    X${COORD_X} Y${COORD_Y}

# To hold more than just one MouseButton while dragging, use Press/Release Mouse Button after any Drag/Drop keyword!
Drag From Node (Node + MouseButton)
    [Tags]                          smoke
    Reset Circle
    Set Coordinates To Use          100    400
    ${CIRCLE}                       Find    \#circle
    Drag From                       ${CIRCLE}    SECONDARY
    Press Mouse Button              PRIMARY
    ${CIRCLE_ATTRIBUTES}            find    \#circle
    # If both PRIMARY and SECONDARY MouseButtons are pressed, circles scale is 2.0 and fill is #00a000
    ${SCALE_X}                      Call Object Method    ${CIRCLE_ATTRIBUTES}    getScaleX
    ${SCALE_Y}                      Call Object Method    ${CIRCLE_ATTRIBUTES}    getScaleY
    Should Be Equal                 ${SCALE_X}     ${2.0}
    Should Be Equal                 ${SCALE_Y}     ${2.0}
    ${CIRCLE_ATTRIBUTES}            Convert To String    ${CIRCLE_ATTRIBUTES}
    Should Contain                  ${CIRCLE_ATTRIBUTES}    fill=0x00a000ff
    Drop To Coordinates             ${COORD_X}    ${COORD_Y}
    Release Mouse Button            PRIMARY
    Verify String                   \#circleScreenLocationLabel    X${COORD_X} Y${COORD_Y}

Drag From Point Query (PointQuery only)
    [Tags]                      smoke
    Reset Circle
    Set Coordinates To Use      200                 200
    ${CIRCLE}                   find                \#circle
    ${POINTQUERY}               Point To            ${CIRCLE}
    ${TARGET}                   Find                \#secondWindowLabel
    Drag From                   ${POINTQUERY}
    Drop To                     ${TARGET}
    Verify String               \#circleLabel       Second window

Drag From Point Query (PointQuery + MouseButton)
    [Tags]                      smoke
    Reset Circle
    Set Coordinates To Use      185                 215
    ${CIRCLE}                   Find                \#circle
    ${POINTQUERY}               Point To            ${CIRCLE}
    ${TARGET}                   Find                \#secondWindowLabel
    Drag From                   ${POINTQUERY}       PRIMARY
    Drop To                     ${TARGET}
    Verify String               \#circleLabel       Second window

Drag From Window (Window Only)
    [Tags]    smoke
    Reset Circle
    Set Coordinates To Use    150    150
    Drag From                 \#circle
    ${WINDOW}                 Get Window    DragRobot Test
    Drop To                   ${WINDOW}
    Drag From                 ${WINDOW}
    Drop To Coordinates       ${COORD_X}    ${COORD_Y}
    Verify String             \#circleScreenLocationLabel    X${COORD_X} Y${COORD_Y}

Drag From Window (Window + MouseButton)
    [Tags]                          smoke
    Reset Circle
    Set Coordinates To Use          145    155
    Drag From                       \#circle
    ${WINDOW}                       Get Window   DragRobot Test
    Drop To                         ${WINDOW}
    Drag From                       ${WINDOW}    SECONDARY
    Drop To Coordinates             ${COORD_X}    ${COORD_Y}
    Verify String                   \#circleScreenLocationLabel    X${COORD_X} Y${COORD_Y}

Drag From Scene (Scene Only)
    [Tags]                          smoke
    Reset Circle
    Set Coordinates To Use          700    150
    ${SCENE}    Get Nodes Scene     \#circle
    Drag From                       \#circle
    Drop To                         ${SCENE}
    Drag From                       ${SCENE}
    Drop To Coordinates             ${COORD_X}    ${COORD_Y}
    Verify String                   \#circleScreenLocationLabel    X${COORD_X} Y${COORD_Y}

Drag From Scene (Scene + MouseButton)
    [Tags]                          smoke
    Reset Circle
    Set Coordinates To Use          700    150
    ${SCENE}    Get Nodes Scene     \#circle
    Drag From                       \#circle
    Drop To                         ${SCENE}
    Drag From                       ${SCENE}    SECONDARY
    Drop To Coordinates             ${COORD_X}    ${COORD_Y}
    Verify String                   \#circleScreenLocationLabel    X${COORD_X} Y${COORD_Y}

Drop To Node
    [Tags]                  smoke
    Reset Circle
    ${TARGET}               find    \#secondWindowLabel
    Drag From               \#circle
    Drop To                 ${TARGET}
    Verify String           \#circleLabel    Second window

Drop To Scene
    [Tags]                  smoke
    Reset Circle
    ${SCENE}                Get Nodes Scene    \#secondWindowLabel
    Drag From               \#circle
    Drop To                 ${SCENE}
    Verify String           \#circleLabel    Second window

Drop To Query
    [Tags]                  smoke
    Reset Circle
    Drag From               \#circle
    Drop To                 \#secondWindowLabel
    Verify String           \#circleLabel    Second window

Drop To
    [Tags]                  smoke
    Reset Circle
    ${TARGET}               find    \#secondWindowLabel
    ${POINTQUERY}           Point To    ${TARGET}
    Drag From               \#circle
    Drop To                 ${POINTQUERY}
    Verify String           \#circleLabel    Second window

Drop to coordinates
    [Tags]                      smoke
    Reset Circle
    Drag From                   \#circle
    Set Coordinates To Use      100    100
    Drop To Coordinates         ${COORD_X}    ${COORD_Y}
    Verify String               \#circleScreenLocationLabel    X${COORD_X} Y${COORD_Y}

Drag from coordinates, drop to window
    [Tags]                      smoke
    ${SECOND_WINDOW}            Get Window    Second window
    Drag From Coordinates       ${COORD_X}    ${COORD_Y}
    Drop To                     ${SECOND_WINDOW}
    Verify String               \#circleLabel    Second window

Drag from point, Drop to point
    [Tags]                      smoke
    Set Coordinates To Use      550    150
    Reset Circle
    ${CIRCLE}                   Find                 \#circle
    ${CIRCLE_BOUNDS}            Get Bounds           ${CIRCLE}
    ${CIRCLE_MINX}              Call Object Method     ${CIRCLE_BOUNDS}    getMinX
    ${CIRCLE_MINY}              Call Object Method     ${CIRCLE_BOUNDS}    getMinY
    ${CIRCLE_WIDTH}             Call Object Method     ${CIRCLE_BOUNDS}    getWidth
    ${CIRCLE_HEIGHT}            Call Object Method     ${CIRCLE_BOUNDS}    getHeight
    ${POINT_X}                  Evaluate             ${CIRCLE_MINX} + (${CIRCLE_WIDTH} / 2)
    ${POINT_Y}                  Evaluate             ${CIRCLE_MINY} + (${CIRCLE_HEIGHT} / 2)
    ${POINT_X}                  Convert To Integer   ${POINT_X}
    ${POINT_Y}                  Convert To Integer   ${POINT_Y}
    ${POINT}                    Create Point         ${POINT_X}    ${POINT_Y}
    ${DROP_POINT}               Create Point         ${COORD_X}    ${COORD_Y}
    Drag From                   ${POINT}
    Drop To                     ${DROP_POINT}
    Verify String               \#circleScreenLocationLabel    X${COORD_X} Y${COORD_Y}

Drag from bounds, Drop to bounds
    [Tags]                      smoke
    Set Coordinates To Use      675    125
    Reset Circle
    ${CIRCLE}                   Find                 \#circle
    ${CIRCLE_BOUNDS}            Get Bounds           ${CIRCLE}
    ${CIRCLE_WIDTH}             Call Object Method     ${CIRCLE_BOUNDS}    getWidth
    ${CIRCLE_HEIGHT}            Call Object Method     ${CIRCLE_BOUNDS}    getHeight
    ${DROP_BOUNDS}              Create Bounds        ${COORD_X}     ${COORD_Y}    ${CIRCLE_WIDTH}    ${CIRCLE_HEIGHT}
    ${TARGET_X}                 Evaluate             ${COORD_X} + (${CIRCLE_WIDTH} / 2)
    ${TARGET_Y}                 Evaluate             ${COORD_Y} + (${CIRCLE_HEIGHT} / 2)
    ${TARGET_X}                 Convert To Integer   ${TARGET_X}
    ${TARGET_Y}                 Convert To Integer   ${TARGET_Y}
    Drag From                   ${CIRCLE_BOUNDS}
    Drop To                     ${DROP_BOUNDS}
    Verify String               \#circleScreenLocationLabel    X${TARGET_X} Y${TARGET_Y}

*** Keywords ***
Setup all tests
    Launch Javafx Application     ${TEST_APPLICATION}
    Set Screenshot Directory      ${OUTPUT_DIR}${/}report-images
    ${SCENE}                      Get Nodes Scene     \.button
    ${BOUNDS}                     Get Bounds          ${SCENE}
    Set Suite Variable            ${SCENE_BOUNDS}     ${BOUNDS}

Teardown all tests
    Close Javafx Application

Reset Circle
    Click On    \.button

Set Coordinates To Use
    [arguments]           ${TARGET_X}          ${TARGET_Y}
    ${MINX}               Call Object Method   ${SCENE_BOUNDS}    getMinX
    ${MINY}               Call Object Method   ${SCENE_BOUNDS}    getMinY
    ${SET_X}              Evaluate             ${MINX} + ${TARGET_X}
    ${SET_Y}              Evaluate             ${MINY} + ${TARGET_Y}
    ${SET_X}              Convert To Integer   ${SET_X}
    ${SET_Y}              Convert To Integer   ${SET_Y}
    Set Suite Variable    ${COORD_X}           ${SET_X}
    Set Suite Variable    ${COORD_Y}           ${SET_Y}
    
Verify String
    [Documentation]    Verifies that string is equal in location
    [Arguments]                   ${query}          ${string}
    ${target_node}=               Find              ${query}
    ${text_label}=                Get Node Text     ${target_node}
    Should Be Equal As Strings    ${string}         ${text_label}