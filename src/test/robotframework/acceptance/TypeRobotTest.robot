*** Settings ***
Documentation     Tests to test javafxlibrary.keywords.TypeRobot related keywords
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-typerobot

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestKeyboardRobot

*** Test Cases ***
Push key combination
    [Tags]          smoke
    Click On        \#keyCombinationLabel
    Push            CONTROL    SHIFT    G
    Verify String   \#keyCombinationLabel    Passed

Push Many Times
    [Tags]              smoke
    Clear Textarea
    Create 5x5 Grid
    Push Many Times     2    LEFT
    Push Many Times     2    UP
    Erase Text          1
    Write               O
    Verify String       \#textAreaLabel    XXXXX\nXXXXX\nXXOXX\nXXXXX\nXXXXX

Erase Text
    [Tags]          smoke
    Clear Textarea
    Write           Robot Framework
    Erase Text      4
    Verify String   \#textAreaLabel    Robot Frame

Write To Test
    [Tags]              smoke
    Clear Textarea
    Write To            id=textAreaLabel    Robot Framework via Write To -keyword
    Verify String       id=textAreaLabel    Robot Framework via Write To -keyword

Write Fast Test
    [Tags]              smoke
    Clear Textarea
    Click On            \#textAreaLabel
    Write Fast          Robot Framework via Write Fast -keyword using clipboard
    Verify String       \#textAreaLabel    Robot Framework via Write Fast -keyword using clipboard


*** Keywords ***
Setup all tests
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application

Clear Textarea
    Click On    \#resetButton
    Click On    \#textArea

Create 5x5 Grid
    :FOR    ${INDEX}    IN RANGE    0    5
    \    LOG                ${index}
    \    Push Many Times    5    SHIFT    X
    \    Run Keyword If     ${INDEX} < 4    Push    ENTER
    
Verify String
    [Documentation]    Verifies that string is equal in location
    [Arguments]                   ${query}          ${string}
    ${target_node}                Find              ${query}
    ${text_label}                 Get Node Text     ${target_node}
    Should Be Equal As Strings    ${string}         ${text_label}