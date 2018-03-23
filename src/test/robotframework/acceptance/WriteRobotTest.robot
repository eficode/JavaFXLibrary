*** Settings ***
Documentation     Tests to test javafxlibrary.keywords.WriteRobot related keywords
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-writerobot

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestKeyboardRobot

*** Test Cases ***
Write text
    [Tags]    smoke
    Write    2.6.5 Embedding arguments
    Verify String           \#textAreaLabel    2.6.5 Embedding arguments

Write single characters
    [Tags]    smoke
    Reset Textarea
    Write    t
    Write    e
    Write    s
    Write    t
    Verify String           \#textAreaLabel    test

Write special characters
    [Tags]    smoke
    Reset Textarea
    Write    /@[*])(=?^_:;
    Verify String           \#textAreaLabel    /@[*])(=?^_:;

*** Keywords ***
Setup all tests
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application

Reset Textarea
    Click On    \#resetButton
    Click On    \#textArea
    
Verify String
    [Documentation]    Verifies that string is equal in location
    [Arguments]                   ${query}          ${string}
    ${target_node}=               Find              ${query}
    ${text_label}=                Get Node Text     ${target_node}
    Should Be Equal As Strings    ${string}         ${text_label}