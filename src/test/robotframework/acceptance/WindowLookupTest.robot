*** Settings ***
Documentation     Tests to test javafxlibrary.keywords.WindowLookup related keywords
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-windowlookup

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestMultipleWindows

*** Test Cases ***
Find From Different Window
    [Tags]              smoke
    ${first}            Find            id=firstWindowLabel
    ${second}           Find            id=secondWindowLabel
    ${third}            Find            id=thirdWindowLabel
    ${root}             Find            id=secondWindowAnchorPane
    ${node4}            Find            id=thirdWindowLabel    false    ${root}
    Should Contain      ${first}        Label[id=firstWindowLabel, styleClass=label]'First window'
    Should Contain      ${second}       Label[id=secondWindowLabel, styleClass=label]'Second window'
    Should Contain      ${third}        Label[id=thirdWindowLabel, styleClass=label]'Third window'
    Should Not Contain  ${node4}        Label[id=thirdWindowLabel, styleClass=label]'Third window'

Window By Node
    [Tags]    smoke
    ${TARGET}         Get Window   Second window
    ${NODE}           Find         \#secondWindowLabel
    ${WINDOW}         Get Window   ${NODE}
    Should Be Equal   ${TARGET}    ${WINDOW}   msg=Window searched with title and node does not match!

Window By Scene
    [Tags]    smoke
    ${TARGET}          Get Window         Third window
    ${SCENE}           Get Nodes Scene    \#thirdWindowAnchorPane
    ${WINDOW}          Get Window         ${SCENE}
    Should Be Equal    ${TARGET}          ${WINDOW}   msg=Window searched with title and scene does not match!

Window By Pattern
    [Tags]    smoke
    ${TARGET}          Get Window   First window
    ${WINDOW}          Get Window   pattern=F[i-t]{4} window
    Should Be Equal    ${TARGET}    ${WINDOW}   msg=Window searched with title and pattern does not match!

# Windows by index numbers: 0-Second window 1-Third window 2-First window
Window By Index
    [Tags]    smoke
    ${TARGET}          Get Window         First window
    ${WINDOW}          Get Window         2
    ${TITLE}           Get Window Title   ${WINDOW}
    Should Be Equal    ${TARGET}          ${WINDOW}   msg=Window searched with title and index (string) does not match!

    ${TARGET}          Get Window         Second window
    ${WINDOW}          Get Window         ${0}
    ${TITLE}           Get Window Title   ${WINDOW}
    Should Be Equal    ${TARGET}          ${WINDOW}   msg=Window searched with title and index (integer) does not match!

List Windows
    [Tags]              smoke
    ${INDEX}            Set Variable    ${0}
    ${LIST_WINDOWS}     List Windows
    :FOR    ${WINDOW}    IN    @{LIST_WINDOWS}
        \    ${TARGET}         Get Window         ${INDEX}
        \    Should Be Equal   ${WINDOW}          ${TARGET}    msg=Window lists does not match (index ${INDEX})!
        \    ${INDEX}          Set Variable       ${INDEX+1}

List Target Windows
    [Tags]              smoke
    ${INDEX}            Set Variable    ${0}
    ${LIST_WINDOWS}     List Target Windows
    :FOR    ${WINDOW}    IN    @{LIST_WINDOWS}
        \    ${TARGET}         Get Window      ${INDEX}
        \    Should Be Equal   ${WINDOW}       ${TARGET}       msg=Window lists does not match (index ${INDEX})!
        \    ${INDEX}          Set Variable    ${INDEX+1}

# Keyword is located in ConvenienceKeywords
Bring Stage To Front
    [Tags]                  smoke
    ${SECOND_WINDOW}        Get Window    Second window
    ${THIRD_WINDOW}         Get Window    Third window

    Bring Stage To Front    ${SECOND_WINDOW}
    Sleep                   1    SECONDS
    ${SECOND_ISFOCUSED}     Call Object Method    ${SECOND_WINDOW}    isFocused
    Should Be True          ${SECOND_ISFOCUSED}   msg=Second window was not focused!

    Bring Stage To Front    ${THIRD_WINDOW}
    Sleep                   1    SECONDS
    ${THIRD_ISFOCUSED}      Call Object Method    ${THIRD_WINDOW}    isFocused
    Should Be True          ${THIRD_ISFOCUSED}    msg=Third window was not focused!

# On Mac the testing application had to be modified to register CMD + W.
# Close Current Window uses ALT + F4 on Windows, so it should work with no changes to the testing application.
#
# Keyword is located in TypeRobot
Close Current Window
    [Tags]                  smoke    set-todo
    ${START}                List Windows
    Activate window         @{START}[0]
    Close Current Window
    ${END}                  List Windows
    Should Not Be Equal     ${START}    ${END}     msg=Unable to close window!   values=False

*** Keywords ***
Activate window
    [Arguments]             ${window_node}
    Bring Stage To Front    ${window_node}
    Click On                ${window_node}

Setup all tests
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application