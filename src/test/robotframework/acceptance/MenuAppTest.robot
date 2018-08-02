*** Settings ***
Documentation     Tests to test javafxlibrary keywords
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-menuapp

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.MenuApp

*** Test Cases ***
Select Context Menu Item
    [Tags]          smoke
    ${menuitems}    Create List                 JavaFXLibrary    Is easy    And fun to use
    ${location}     Point To With Offset        \#bgRectangle    -300       0
    :FOR            ${menuitem}    IN           @{menuitems}
    \               Right Click On              ${location}
    \               Select Context Menu Item    ${menuitem}
    \               Verify String               .textLabel       ${menuitem}

Select Context Menu Item Using Click On Keyword
    [Tags]          smoke    demo-set
    ${menuitems}    Create List                 JavaFXLibrary    Is easy    And fun to use
    ${location}     Point To With Offset        \#bgRectangle    -300       0
    :FOR            ${menuitem}    IN           @{menuitems}
    \               Right Click On              ${location}
    \               Click On                    ${menuitem}
    \               Verify String               .textLabel       ${menuitem}

Menus - Navigate
    [Tags]                  smoke    demo-set
    Click On                Learn
    Click On                Test Automation & Robot Framework
    Verify String           .textLabel    Test Automation & Robot Framework

Use ComboBoxes With Text Values
    [Tags]                  smoke    demo-set
    Click On                Select amount
    Click On                50 pc
    Click On                Select price
    Click On                75 €
    Verify String           \#total     3750 €

Menus - Change Theme
    [Tags]                  smoke    demo-set
    Click On                Settings
    Click On                Theme
    Click On                JavaFX               HORIZONTAL_FIRST
    ${SCENE}                Get Nodes Scene      .textLabel
    @{STYLESHEET}           Call Object Method   ${SCENE}        getStylesheets
    Should Contain          @{STYLESHEET}[0]     Javastyle.css

Menus - Change Font Size
    [Tags]                  smoke    demo-set
    Click On                Settings
    Move To                 Font size
    Click On                26px                 HORIZONTAL_FIRST
    ${LABEL}                Find                 \.textLabel
    ${STYLE}                Call Object Method   ${LABEL}            getStyle
    Should Contain          ${STYLE}             -fx-font-size: 26px

Combined
    [Tags]                  smoke    demo-set
    Click On                Settings
    Move To                 Theme
    # Horizontal first is required because submenu closes if the cursor moves outside of menu bounds
    Click On                Gradient              HORIZONTAL_FIRST
    ${SCENE}                Get Nodes Scene       .textLabel
    @{STYLESHEET}           Call Object Method    ${SCENE}    getStylesheets
    Should Contain          @{STYLESHEET}[0]      Gradientstyle.css

    Click On                Services
    Click On                Analyze
    Verify String          .textLabel          Analyze

    # Using Find All instead of text-value based css-selector here to avoid dependencies with the second test case
    @{COMBOBOXES}           Find All            .combo-box
    Click On                @{COMBOBOXES}[0]
    Click On                25 pc
    Click On                @{COMBOBOXES}[1]
    Click On                50 €
    Verify String           \#total             1250 €

*** Keywords ***
Setup all tests
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
