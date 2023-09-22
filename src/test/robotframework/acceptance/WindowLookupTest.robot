*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.WindowLookup related keywords
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Disable Embedded Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-windowlookup

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
    [Tags]              smoke
    ${TARGET}           Get Window      Second window
    ${NODE}             Find            id=secondWindowLabel
    ${WINDOW}           Get Window      ${NODE}
    Should Be Equal     ${TARGET}       ${WINDOW}   msg=Window searched with title and node does not match!

Window By Scene
    [Tags]              smoke
    ${TARGET}           Get Window      Third window
    ${SCENE}            Get Scene       id=thirdWindowAnchorPane
    ${WINDOW}           Get Window      ${SCENE}
    Should Be Equal     ${TARGET}       ${WINDOW}   msg=Window searched with title and scene does not match!

Window By Pattern
    [Tags]              smoke
    ${TARGET}           Get Window      First window
    ${WINDOW}           Get Window      pattern=F[i-t]{4} window
    Should Be Equal     ${TARGET}       ${WINDOW}   msg=Window searched with title and pattern does not match!

# Windows by index numbers: 0-Second window 1-Third window 2-First window
Window By Index
    [Tags]              smoke
    ${TARGET}           Get Window         First window
    ${WINDOW}           Get Window         0
    ${TITLE}            Get Window Title   ${WINDOW}
    Should Be Equal     ${TARGET}          ${WINDOW}   msg=Window searched with title and index (string) does not match!
    ${TARGET}           Get Window         Second window
    ${WINDOW}           Get Window         1
    ${TITLE}            Get Window Title   ${WINDOW}
    Should Be Equal     ${TARGET}          ${WINDOW}   msg=Window searched with title and index (integer) does not match!

List Windows
    [Tags]                      smoke
    ${INDEX}                    Set Variable        ${0}
    ${LIST_WINDOWS}             List Windows
    FOR     ${WINDOW}    IN    @{LIST_WINDOWS}
            ${TARGET}          Get Window          ${INDEX}
            Should Be Equal    ${WINDOW}           ${TARGET}    msg=Window lists does not match (index ${INDEX})!
            ${INDEX}           Set Variable        ${INDEX+1}
    END

List Target Windows
    [Tags]                      smoke
    ${INDEX}                    Set Variable        ${0}
    ${LIST_WINDOWS}             List Target Windows
    FOR     ${WINDOW}    IN    @{LIST_WINDOWS}
            ${TARGET}          Get Window          ${INDEX}
            Should Be Equal    ${WINDOW}           ${TARGET}       msg=Window lists does not match (index ${INDEX})!
            ${INDEX}           Set Variable        ${INDEX+1}
    END

Bring Stage To Front
    [Tags]                     smoke
    ${second_window}           Get Window           Second window
    Bring Stage To Front       ${second_window}
    Window Should Be Visible   ${second_window}
    Click On                   ${second_window}
    Window Should Be Focused   ${second_window}
    ${target}                  Get Target Window
    Should Be Equal            ${second_window}     ${target}
    ${third_window}            Get Window           Third window
    Bring Stage To Front       ${third_window}
    ${target}                  Get Target Window
    Should Be Equal            ${third_window}      ${target}
    Window Should Not Be Focused   ${second_window}
    Call Object Method In Fx Application Thread     ${second_window}     hide
    Window Should Not Be Visible                    ${second_window}

# On Mac the testing application had to be modified to register CMD + W.
# Close Current Window uses ALT + F4 on Windows, so it should work with no changes to the testing application.
#
# Keyword is located in TypeRobot
Close Current Window
    [Tags]                  smoke           set-todo
    Run Keyword If    ${headless}    Set Tags    monocle-issue
    ${START}                List Windows
    Activate window         ${START}[0]
    Close Current Window
    ${END}                  List Windows
    Should Not Be Equal     ${START}        ${END}     msg=Unable to close window!   values=False

*** Keywords ***
Activate window
    [Arguments]             ${window_node}
    Bring Stage To Front    ${window_node}
    Click On                ${window_node}

Setup all tests
    Import JavaFXLibrary
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application
