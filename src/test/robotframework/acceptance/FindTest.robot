*** Settings ***
Documentation       Tests to test javafxlibrary keywords
Library             JavaFXLibrary
Suite Setup         Setup all tests
Suite Teardown      Teardown all tests
Force Tags          set-find

*** Variables ***
${TEST_APPLICATION}   javafxlibrary.testapps.TestBoundsLocation

*** Test Cases ***
Find With TestFX Query
   [Tags]               smoke
   ${rectangle}         Find            \#green
   ${text}              Find            .whiteText
   Should Contain       ${rectangle}    Rectangle[id=green, x=300.0, y=0.0, width=150.0, height=150.0, fill=0x00a000ff]
   Should Contain       ${text}         Text[text="75x75", x=0.0, y=0.0, alignment=CENTER, origin=BASELINE

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

Find With Chained Selectors
    [Tags]              smoke
    ${lime}             Find        css=VBox HBox Pane id=lime
    ${blue}             Find        css=VBox HBox Pane xpath=//Rectangle[@width="600.0"]
    Should Contain      ${lime}     Rectangle[id=lime, x=500.0, y=200.0, width=75.0, height=75.0, fill=0x00ff00ff]
    Should Contain      ${blue}     Rectangle[id=blue, x=0.0, y=0.0, width=600.0, height=300.0, fill=0x00bfffff]

Find With Root
    [Tags]              smoke
    ${root}             Find            css=Pane
    ${rectangle}        Find            id=lime    true    ${root}
    Should Contain      ${rectangle}    Rectangle[id=lime, x=500.0, y=200.0, width=75.0, height=75.0, fill=0x00ff00ff]

Find All With TestFX Query
    [Tags]              smoke
    @{nodes}            Find All    .whiteText
    Length Should Be    ${nodes}    3

Find All With XPath
    [Tags]              smoke
    @{all_rectangles}   Find All            xpath=//Rectangle
    @{text_nodes}       Find All            xpath=//Text[@text="75x75"]
    Length Should Be    ${all_rectangles}   10
    Length Should Be    ${text_nodes}       6

Find All With CSS query
    [Tags]              smoke
    @{nodes1}           Find All        css=VBox HBox > StackPane Rectangle
    @{nodes2}           Find All        css=Pane Rectangle
    @{nodes3}           Find All        css=Pane > Rectangle
    Length Should Be    ${nodes1}       6
    Length Should Be    ${nodes2}       3
    Length Should Be    ${nodes3}       2

Find All With Chained Selectors
    [Tags]              smoke
    @{nodes1}           Find All        css=VBox HBox xpath=//Rectangle
    @{nodes2}           Find All        css=VBox HBox xpath=//Rectangle[@width="75.0"]
    Length Should Be    ${nodes1}       7
    Length Should Be    ${nodes2}       4

Find All With Root
    [Tags]              smoke
    ${xroot}            Find            css=VBox HBox VBox HBox
    ${croot}            Find            css=Pane
    @{xpath}            Find All        xpath=//Rectangle[@width="75.0"]    false    ${xroot}
    @{css}              Find All        css=StackPane > Rectangle           false    ${croot}
    Length Should Be    ${xpath}        4
    Length Should Be    ${css}          1

Find Nth Node With XPath
    [Tags]              smoke
    ${node1}            Find            xpath=/VBox/HBox/VBox/HBox/VBox/HBox/StackPane
    ${node2}            Find            xpath=/VBox/HBox/VBox/HBox/VBox/HBox/StackPane[2]
    ${child1}           Find            css=Rectangle    true    ${node1}
    ${child2}           Find            css=Rectangle    true    ${node2}
    Should Contain      ${child1}       Rectangle[id=yellow, x=450.0, y=0.0, width=75.0, height=75.0, fill=0xffff00ff]
    Should Contain      ${child2}       Rectangle[id=violet, x=525.0, y=0.0, width=75.0, height=75.0, fill=0x9400d3ff]

Find With Pseudo Class
    [Tags]              smoke
    ${root}             Find            css=VBox HBox VBox HBox StackPane
    ${target}           Find            xpath=//Text[@text="150x150"]
    Move To             ${target}
    ${result}           Find            pseudo=hover    false    ${root}
    Should Be Equal     ${result}       ${target}

Find All With Pseudo Class
    [Tags]              smoke
    ${node}             Find            xpath=//Text[@text="300x300"]
    Move To             ${node}
    @{hovered}          Find All        pseudo=hover
    # Nodes behind have the hover pseudostate too, Find All returns all of these except the one used as a root in lookup
    Length Should Be    ${hovered}      3
    Should Contain      ${hovered}      ${node}

# TODO: Add negative tests

*** Keywords ***
Setup all tests
    Launch Javafx Application    ${TEST_APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Teardown all tests
    Close Javafx Application
