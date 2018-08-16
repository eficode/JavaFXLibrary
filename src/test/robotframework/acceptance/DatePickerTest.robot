*** Settings ***
Documentation     Tests to test DatePicker related keywords
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Test Teardown     Clear Text Input    css=.text-field
Force Tags        set-datepicker

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.DatePickerApp

*** Test Cases ***
Select JavaFXLibrary Release Date
    [Tags]              smoke    demo-set
    Write To            css=.text-field                     JavaFXLibrary Open Source Release
    Click On            css=.arrow-button
    Set Month           March
    Set Year            2018
    Click On            text="23"
    ${date}             Get Selected Date Picker Date       css=.date-picker
    Should End With     ${date}                             2018-03-23

Select JavaFX Release Date
    [Tags]              smoke    demo-set
    Write To            css=.text-field                     JavaFX Release
    Click On            css=.arrow-button
    Set Month           December
    Set Year            2008
    Click On            text="4"
    ${date}             Get Selected Date Picker Date       css=.date-picker
    Should End With     ${date}                             2008-12-04

*** Keywords ***
Setup all tests
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application

Set Year
    [Arguments]                 ${year}
    ${time_labels}              Find All        css=.spinner-label
    ${year_label}               Set Variable    @{time_labels}[1]
    ${left_arrows}              Find All        css=.left-button
    ${prev_year}                Set Variable    @{left_arrows}[1]
    :FOR    ${i}    IN RANGE    99
    \       ${current}          Get Node Text   ${year_label}
    \       Exit For Loop If    ${current} == ${year}
    \       Click On            ${prev_year}

Set Month
    [Arguments]                 ${month}
    ${month_label}              Find            css=.spinner-label
    ${prev_month}               Find            css=.left-button
    :FOR    ${i}    IN RANGE    99
    \       ${current}          Get Node Text   ${month_label}
    \       Exit For Loop If    '${current}' == '${month}'
    \       Click On            ${prev_month}
