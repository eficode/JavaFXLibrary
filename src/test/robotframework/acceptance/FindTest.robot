*** Settings ***
Documentation     Tests to test javafxlibrary keywords
Library           JavaFXLibrary
Suite Setup       Setup all tests
Suite Teardown    Teardown all tests
Force Tags        set-find

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestBoundsLocation

*** Test Cases ***
Find With TestFX Query
   [Tags]           smoke
   ${rectangle}     Find            \#green
   ${text}          Find            .whiteText
   Should Contain   ${rectangle}    Rectangle[id=green, x=300.0, y=0.0, width=150.0, height=150.0, fill=0x00a000ff]
   Should Contain   ${text}         Text[text="75x75", x=0.0, y=0.0, alignment=CENTER, origin=BASELINE

Find With XPath
    [Tags]              smoke
    ${rect_by_id}       Find                xpath=//Rectangle[@id="lime"]
    ${rect_by_fill}     Find                xpath=//Rectangle[@fill="0xff1493ff"]
    ${text}             Find                xpath=//Text[@text="75x75"]
    Should Contain      ${rect_by_id}       Rectangle[id=lime, x=500.0, y=200.0, width=75.0, height=75.0, fill=0x00ff00ff]
    Should Contain      ${rect_by_fill}     Rectangle[id=pink, x=450.0, y=75.0, width=75.0, height=75.0, fill=0xff1493ff]
    Should Contain      ${text}             Text[text="75x75", x=0.0, y=0.0, alignment=CENTER, origin=BASELINE

Find With Class
    [Tags]              smoke
    ${rectangle}        Find            class=javafx.scene.shape.Rectangle
    ${text}             Find            class=javafx.scene.text.Text
    Should Contain      ${rectangle}    Rectangle[id=red, x=0.0, y=0.0, width=300.0, height=300.0, fill=0xff0000ff]
    Should Contain      ${text}         Text[text="300x300", x=0.0, y=0.0, alignment=CENTER, origin=BASELINE

Find With CSS Query
    [Tags]              smoke
    ${rectangle}        Find            css=\#violet
    ${text}             Find            css=VBox HBox StackPane Text.whiteText
    Should Contain      ${rectangle}    Rectangle[id=violet, x=525.0, y=0.0, width=75.0, height=75.0, fill=0x9400d3ff]
    Should Contain      ${text}         Text[text="75x75", x=0.0, y=0.0, alignment=CENTER, origin=BASELINE

Find With ID
    [Tags]              smoke
    ${rectangle}        Find            id=darkblue
    Should Contain      ${rectangle}    Rectangle[id=darkblue, x=300.0, y=150.0, width=300.0, height=150.0, fill=0x00008bff]

New Types Chained
    [Tags]              smoke
    ${lime}             Find        css=VBox HBox Pane id=lime
    ${blue}             Find        css=VBox HBox Pane xpath=//Rectangle[@width="600.0"]
    Should Contain      ${lime}     Rectangle[id=lime, x=500.0, y=200.0, width=75.0, height=75.0, fill=0x00ff00ff]
    Should Contain      ${blue}     Rectangle[id=blue, x=0.0, y=0.0, width=600.0, height=300.0, fill=0x00bfffff]

Find With Root
    [Tags]          smoke
    ${root}    Find    css=Pane
    ${rectangle}    Find    id=lime    true    ${root}

*** Keywords ***
Setup all tests
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application
