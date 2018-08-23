*** Settings ***
Documentation     Tests to test javafxlibrary.keywords.KeyboardRobot related keywords
Resource          ../resource.robot
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-keyboardrobot

*** Variables ***
${TEST_APPLICATION}         javafxlibrary.testapps.TestKeyboardRobot

*** Test Cases ***
Press and hold three buttons
    [Tags]                  smoke
    Reset Combination Test
    Click On                id=keyCombinationLabel
    Press                   CONTROL    SHIFT    G
    Verify String           id=keyCombinationLabel    Passed
    Release                 CONTROL    SHIFT    G

Press / Release
    [Tags]                  smoke
    Click On                id=textArea
    Press                   SHIFT
    Push in order           T    E    S
    Release                 SHIFT
    Push in order           T    I
    Verify String           id=textAreaLabel    TESti
    Push in order           BACK_SPACE    LEFT    BACK_SPACE
    Verify String           id=textAreaLabel    TEt

Push key combination
    [Tags]                  smoke    demo-set
    Reset Combination Test
    Click On                id=keyCombinationLabel
    Push                    CONTROL    SHIFT    G
    Verify String           id=keyCombinationLabel    Passed

Push Many Times
    [Tags]                  smoke    demo-set
    Clear Textarea
    Create 5x5 Grid
    Push Many Times         2    LEFT
    Push Many Times         2    UP
    Erase Text              1
    Write                   O
    Verify String           id=textAreaLabel    XXXXX\nXXXXX\nXXOXX\nXXXXX\nXXXXX

Erase Text
    [Tags]                  smoke    demo-set
    Clear Textarea
    Write                   Robot Framework
    Erase Text              4
    Verify String           id=textAreaLabel    Robot Frame

Write To Test
    [Tags]                  smoke
    Clear Textarea
    Write To                id=textAreaLabel    Robot Framework via Write To -keyword
    Verify String           id=textAreaLabel    Robot Framework via Write To -keyword

Write Fast Test
    [Tags]                  smoke    demo-set
    Clear Textarea
    Click On                id=textAreaLabel
    Write Fast              Robot Framework via Write Fast -keyword using clipboard
    Verify String           id=textAreaLabel    Robot Framework via Write Fast -keyword using clipboard

Write text
    [Tags]                  smoke
    Clear Textarea
    Write                   2.6.5 Embedding arguments
    Verify String           id=textAreaLabel    2.6.5 Embedding arguments

Write special characters
    [Tags]                  smoke    demo-set
    Clear Textarea
    Write                   /@[*])(=?^_:;
    Verify String           id=textAreaLabel    /@[*])(=?^_:;


*** Keywords ***
Setup all tests
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application

Reset Combination Test
    Right Click On    id=keyCombinationLabel

Clear Textarea
    Click On    id=resetButton
    Click On    id=textArea

Create 5x5 Grid
    :FOR    ${INDEX}    IN RANGE    0    5
    \    LOG                ${index}
    \    Push Many Times    5    SHIFT    X
    \    Run Keyword If     ${INDEX} < 4    Push    ENTER

Verify String
    [Documentation]                 Verifies that string is equal in location
    [Arguments]                     ${query}          ${string}
    ${target_node}                  Find              ${query}
    ${text_label}                   Get Node Text     ${target_node}
    Should Be Equal As Strings      ${string}         ${text_label}
