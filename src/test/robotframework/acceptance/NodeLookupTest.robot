*** Settings ***
Documentation     Tests to test javafxlibrary.keywords.NodeLookup related keywords
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-nodelookup

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestClickRobot

*** Test Cases ***
Root Node Of Window
    [Tags]              smoke
    ${WINDOW}           Get Window          ClickRobot Test
    ${TARGET}           Find    \.root
    ${NODE}             Get Root Node Of    ${WINDOW}
    Should Be Equal     ${NODE}    ${TARGET}

Root Node Of Scene
    [Tags]              smoke
    ${TARGET}           Find    \.root
    ${SCENE}            Get Nodes Scene    \#button
    ${ROOT_NODE}        Get Root Node Of    ${SCENE}
    Should Be Equal     ${ROOT_NODE}    ${TARGET}

Root Node Of Node
    [Tags]              smoke
    ${TARGET}           Find    \.root
    ${NODE}             Find    \#button
    ${ROOT_NODE}        Get Root Node Of    ${NODE}
    Should Be Equal     ${ROOT_NODE}    ${TARGET}

Root Node Of Query
    [Tags]              smoke
    ${TARGET}           Find    \.root
    ${ROOT_NODE}        Get Root Node Of    \#button
    Should Be Equal     ${ROOT_NODE}    ${TARGET}

Root Node Of XPath Query
    [Tags]              smoke
    ${TARGET}           Find    \.root
    ${ROOT_NODE}        Get Root Node Of    xpath=//Button
    Should Be Equal     ${ROOT_NODE}    ${TARGET}

Root Node Of Node That Does Not Exist
    [Tags]              smoke
    ${MSG}              Run Keyword And Expect Error    *    Get Root Node Of    \#non-existent-node-id
    Should Contain      ${MSG}    Unable to find any node with query: "\#non-existent-node-id"

*** Keywords ***
Setup all tests
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application