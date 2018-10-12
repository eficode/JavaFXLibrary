*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.NodeLookup related keywords
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Disable Embedded Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-nodelookup

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestClickRobot

*** Test Cases ***
Root Node Of Window
    [Tags]              smoke
    ${WINDOW}           Get Window          ClickRobot Test
    ${TARGET}           Find                css=.root
    ${NODE}             Get Root Node Of    ${WINDOW}
    Should Be Equal     ${NODE}             ${TARGET}

Root Node Of Scene
    [Tags]              smoke
    ${TARGET}           Find                css=.root
    ${SCENE}            Get Scene           id=button
    ${ROOT_NODE}        Get Root Node Of    ${SCENE}
    Should Be Equal     ${ROOT_NODE}        ${TARGET}

Root Node Of Node
    [Tags]              smoke
    ${TARGET}           Find                css=.root
    ${NODE}             Find                id=button
    ${ROOT_NODE}        Get Root Node Of    ${NODE}
    Should Be Equal     ${ROOT_NODE}        ${TARGET}

Root Node Of Query
    [Tags]              smoke
    ${TARGET}           Find                css=.root
    ${ROOT_NODE}        Get Root Node Of    id=button
    Should Be Equal     ${ROOT_NODE}        ${TARGET}

Root Node Of XPath Query
    [Tags]              smoke
    ${TARGET}           Find                css=.root
    ${ROOT_NODE}        Get Root Node Of    xpath=//Button
    Should Be Equal     ${ROOT_NODE}        ${TARGET}

Root Node Of Node That Does Not Exist
    [Tags]              smoke    negative
    ${MSG}              Run Keyword And Expect Error    *    Get Root Node Of    id=non-existent-node-id
    Should Contain      ${MSG}    Unable to find any node with query: "id=non-existent-node-id"

*** Keywords ***
Setup all tests
    Import JavaFXLibrary
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images
    Set Timeout                     1

Teardown all tests
    Close Javafx Application
