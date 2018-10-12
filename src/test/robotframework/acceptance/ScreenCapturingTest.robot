*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.ScreenCapturing related keywords
Library             String
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Disable Embedded Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-screencapturing

*** Variables ***
${TEST_APPLICATION}     javafxlibrary.testapps.TestScreenCapturing
${COMPARISON}           ${EMPTY}

*** Test Cases ***
Capture Node
    [Tags]                      smoke
    ${NODE}                     Find            id=subRectangles
    ${RESULT}                   Capture Image   ${NODE}
    ${TARGET}                   Load Image      ${COMPARISON}bounds.png
    Images Should Match         ${RESULT}       ${TARGET}    ${99}

Capture Screen Region
    [Tags]                      smoke
    ${NODE}                     Find                    css=.root
    ${BOUNDS}                   Get Bounds              ${NODE}
    ${MINX}                     Call Object Method      ${BOUNDS}       getMinX
    ${MINY}                     Call Object Method      ${BOUNDS}       getMinY
    ${RECTANGLE}                Create Rectangle        ${MINX}         ${MINY}    240    240
    ${IMAGE1}                   Capture Image           ${RECTANGLE}
    ${IMAGE2}                   Load Image              ${COMPARISON}screen_region.png
    Images Should Match         ${IMAGE1}               ${IMAGE2}       ${99}

Capture Bounds
    [Tags]                      smoke
    ${SCENE}                    Get Scene               id=rectangleContainer
    ${SCENE_BOUNDS}             Get Bounds              ${SCENE}
    ${SCENE_MINX}               Call Object Method      ${SCENE_BOUNDS}    getMinX
    ${SCENE_MINY}               Call Object Method      ${SCENE_BOUNDS}    getMinY
    ${MIN_X}                    Evaluate                ${SCENE_MINX} + ${80.0}
    ${MIN_Y}                    Evaluate                ${SCENE_MINY} + ${80.0}
    ${BOUNDS}                   Create Bounds           ${MIN_X}        ${MIN_Y}    160    160
    ${IMAGE1}                   Capture Image           ${BOUNDS}
    ${IMAGE2}                   Load Image              ${COMPARISON}bounds.png
    Images Should Match         ${IMAGE1}               ${IMAGE2}       ${99}

Save And Load Image With Path
    [Tags]                  smoke
    ${NODE}                 Find            id=rectangleContainer
    ${IMAGE1}               Capture Image   ${NODE}
    Save Image As           ${IMAGE1}       ${TEMPDIR}${/}image.png
    Log                     <img src="${TEMPDIR}${/}image.png" widht="800">   html=true
    ${IMAGE2}               Load Image      ${TEMPDIR}${/}image.png
    Images Should Match     ${IMAGE1}       ${IMAGE2}

Images Should Match
    [Tags]                  smoke
    ${image1}               Load Image      ${COMPARISON}bounds.png
    ${image2}               Load Image      ${COMPARISON}bounds.png
    Images Should Match     ${image1}       ${image2}

Images Should Match With Percentage
    [Tags]                  smoke
    ${image1}               Load Image      ${COMPARISON}node.png
    ${image2}               Load Image      ${COMPARISON}node_desaturated.png
    Images Should Match     ${image1}       ${image2}    ${80}

Images Should Not Match With Percentage
    [Tags]                      smoke
    ${image1}                   Load Image      ${COMPARISON}node.png
    ${image2}                   Load Image      ${COMPARISON}node_colored.png
    Images Should Not Match     ${image1}       ${image2}    ${30}

Negative Test For Images Should Match
    [Tags]              negative        smoke
    ${image1}           Load Image      ${COMPARISON}node.png
    ${image2}           Load Image      ${COMPARISON}node_desaturated.png
    ${msg}              Run Keyword And Expect Error    *    Images Should Match    ${image1}    ${image2}    ${90}
    Should Be Equal     ${msg}          Images do not match - Expected at least 90% similarity, got 82%

Negative Test For Images Should Not Match
    [Tags]              negative        smoke
    ${image1}           Load Image      ${COMPARISON}node.png
    ${image2}           Load Image      ${COMPARISON}node_desaturated.png
    ${msg}              Run Keyword And Expect Error    *    Images Should Not Match    ${image1}    ${image2}    ${20}
    Should Be Equal     ${msg}          Images are too similar - Expected at least 20% difference, got 17%

Try To Compare Different Size Images
    [Tags]              negative        smoke
    ${image1}           Load Image      ${COMPARISON}screen_region.png
    ${image2}           Load Image      ${COMPARISON}url.png
    ${msg}              Run Keyword And Expect Error    *    Images Should Match    ${image2}    ${image1}
    Should Be Equal     ${msg}          Images must be same size to compare: Image1 is 378x116 and Image2 is 240x240

*** Keywords ***
Setup all tests
    Import JavaFXLibrary
    Set Timeout                     0
    Launch Javafx Application       ${TEST_APPLICATION}
    Set Screenshot Directory        ${OUTPUT_DIR}${/}report-images
    Set comparison path

Teardown all tests
    Close Javafx Application

Set comparison path
    ${dir_path}             Fetch From Left     ${CURDIR}           acceptance
    ${comparison_path}      Catenate            SEPARATOR=          ${dir_path}    resources/screencapturingtest/
    Set Suite Variable      ${COMPARISON}       ${comparison_path}
