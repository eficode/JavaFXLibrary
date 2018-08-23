*** Settings ***
Documentation     Tests for Window Management
Resource          ../resource.robot
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-windowmanagement

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestWindowManagement

*** Test Cases ***
Close Alert
    [Tags]                      smoke           demo-set
    Click On                    id=navigationAlert
    Click On                    css=.button
    Sleep                       3s
    ${WINDOWS}                  List Windows
    List Length Should Be       ${WINDOWS}      ${2}
    Click On                    css=.dialog-pane .button
    ${WINDOWS}                  List Windows
    List Length Should Be       ${WINDOWS}      ${1}

Add an employee
    [Tags]                      smoke                   demo-set
    Click On                    id=navigationDialog
    Click On                    id=addEmployeeButton
    ${TEXTFIELDS}               Find All                css=.dialog-pane .text-field
    Write To                    @{TEXTFIELDS}[0]        Pasi
    Write To                    @{TEXTFIELDS}[1]        1452754765
    Click On                    text="Add"
    Employee Should Be Added    Pasi                    1452754765

Add Multiple Employees
    [Tags]                              smoke
    Click On                            id=navigationDialog
    ${DATA}                             Get Employee Data
    :FOR    ${ITEM}     IN              @{DATA}
    \       Click On                    text="Add employee"
    \       ${FIELDS}                   Find All        css=.dialog-pane .text-field
    \       Write To                    @{FIELDS}[0]    ${ITEM.name}
    \       Write To                    @{FIELDS}[1]    ${ITEM.phone}
    \       Click On                    text="Add"
    \       Employee Should Be Added    ${ITEM.name}    ${ITEM.phone}

Find From Node
    [Tags]              smoke
    Click On            id=navigationDialog
    ${NODE}             Find                    id=secondRow
    ${LABEL}            Find From Node          ${NODE}         .employeeDataCell
    ${RESULT}           Call Object Method      ${LABEL}        getText
    Should Be Equal     ${RESULT}               John

Find All From Node
    [Tags]              smoke
    Click On            id=navigationDialog
    ${NODE}             Find                    id=secondRow
    ${TEXTFIELDS}       Find All From Node      ${NODE}             .employeeDataCell
    ${PHONE}            Set Variable            @{TEXTFIELDS}[1]
    ${RESULT}           Call Object Method      ${PHONE}            getText
    Should Be Equal     ${RESULT}               0401231234

*** Keywords ***
Setup all tests
    Launch Javafx Application       ${TEST_APPLICATION}
    Set Screenshot Directory        ${OUTPUT_DIR}${/}report-images

Employee Should Be Added
    [Arguments]         ${NAME}                 ${PHONE}
    ${CELLS}            Find All                css=.employeeDataCell
    ${SIZE}             Get Length              ${CELLS}
    ${NAMEINDEX}        Evaluate                ${SIZE}-${2}
    ${PHONEINDEX}       Evaluate                ${SIZE}-${1}
    ${NAMELABEL}        Set Variable            @{CELLS}[${NAMEINDEX}]
    ${PHONELABEL}       Set Variable            @{CELLS}[${PHONEINDEX}]
    ${NAMEVALUE}        Call Object Method      ${NAMELABEL}            getText
    ${PHONEVALUE}       Call Object Method      ${PHONELABEL}           getText
    Should Be Equal     ${NAME}                 ${NAMEVALUE}
    Should Be Equal     ${PHONE}                ${PHONEVALUE}

List Length Should Be
    [Arguments]         ${LIST}             ${TARGETSIZE}
    ${ACTUALSIZE}       Get Length          ${LIST}
    Should Be Equal     ${ACTUALSIZE}       ${TARGETSIZE}

Get Employee Data
    &{EMPLOYEE1}    Create Dictionary       name=Sami       phone=1231232233
    &{EMPLOYEE2}    Create Dictionary       name=Jukka      phone=1231233322
    ${DATA}         Create List             ${EMPLOYEE1}    ${EMPLOYEE2}
    [Return]        ${DATA}

Teardown all tests
    Close Javafx Application
