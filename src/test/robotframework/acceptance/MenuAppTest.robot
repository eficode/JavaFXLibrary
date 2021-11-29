*** Settings ***
Documentation       Tests to test javafxlibrary keywords
Resource            ../resource.robot
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Test Setup          Disable Embedded Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-menuapp

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.MenuApp

*** Test Cases ***
Select Context Menu Item
    [Tags]          smoke
    ${menuitems}    Create List                 JavaFXLibrary       Is easy    And fun to use
    ${location}     Point To With Offset        id=bgRectangle      -300       0
    FOR            ${menuitem}    IN           @{menuitems}
                   Right Click On              ${location}
                   Select Context Menu Item    ${menuitem}
                   Verify String               css=.textLabel      ${menuitem}
    END

Select Context Menu Item Using Click On Keyword
    [Tags]          smoke                       demo-set
    ${menuitems}    Create List                 JavaFXLibrary       Is easy    And fun to use
    ${location}     Point To With Offset        id=bgRectangle      -300       0
    FOR            ${menuitem}    IN           @{menuitems}
                   Right Click On              ${location}
                   Click On                    text="${menuitem}"
                   Verify String               css=.textLabel      ${menuitem}
    END

Menus - Navigate
    [Tags]                  smoke                   demo-set
    Click On                text="Learn"
    Click On                text="Test Automation & Robot Framework"
    Verify String           css=.textLabel          Test Automation & Robot Framework

Use ComboBoxes With Text Values
    [Tags]                  smoke                   demo-set
    Click On                text="Select amount"
    Click On                text="50 pc"
    Click On                text="Select price"
    Click On                text="75 €"
    Verify String           id=total                3750 €

Menus - Change Theme
    [Tags]                  smoke                   demo-set
    Click On                text="Settings"
    Click On                text="Theme"
    Click On                text="JavaFX"           HORIZONTAL_FIRST
    ${SCENE}                Get Scene               css=.textLabel
    @{STYLESHEET}           Call Object Method      ${SCENE}            getStylesheets
    Should Contain          ${STYLESHEET}[0]        Javastyle.css

Menus - Change Font Size
    [Tags]                        smoke                   demo-set
    Click On                      text="Settings"
    Wait Until Node Is Visible    text="Font size"
    Move To                       text="Font size"
    Wait Until Node Is Visible    text="26px"
    Click On                      text="26px"             HORIZONTAL_FIRST
    ${LABEL}                      Find                    css=.textLabel
    ${STYLE}                      Call Object Method      ${LABEL}            getStyle
    Should Contain                ${STYLE}                -fx-font-size: 26px

Combined
    [Tags]                        smoke                   demo-set
    Click On                      text="Settings"
    Wait Until Node Is Visible    text="Theme"
    Move To                       text="Theme"
    # Horizontal first is required because submenu closes if the cursor moves outside of menu bounds
    Wait Until Node is Visible    text="Gradient"
    Click On                      text="Gradient"         HORIZONTAL_FIRST
    ${SCENE}                      Get Scene               css=.textLabel
    @{STYLESHEET}                 Call Object Method      ${SCENE}            getStylesheets
    Should Contain                ${STYLESHEET}[0]        Gradientstyle.css
    Click On                      text="Services"
    Click On                      text="Analyze"
    Verify String                 css=.textLabel          Analyze

    # Using Find All instead of text-value based css-selector here to avoid dependencies with the second test case
    @{COMBOBOXES}           Find All                css=.combo-box
    Click On                ${COMBOBOXES}[0]
    Click On                text="25 pc"
    Click On                ${COMBOBOXES}[1]
    Click On                text="50 €"
    Verify String           id=total                1250 €

*** Keywords ***
Setup all tests
    Import JavaFXLibrary
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application

Verify String
    [Documentation]                 Verifies that string is equal in location
    [Arguments]                     ${query}          ${string}
    ${target_node}                  Find              ${query}
    ${text_label}                   Get Node Text     ${target_node}
    Should Be Equal As Strings      ${string}         ${text_label}
