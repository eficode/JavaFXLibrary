*** Settings ***
Documentation       Tests to test javafxlibrary.keywords.AdditionalKeywords.Find related keywords
Resource            ../resource.robot
Suite Setup         Setup All Tests
Suite Teardown      Teardown all tests
Test Setup          Disable Embedded Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-find

*** Variables ***
${CURRENT_APPLICATION}    NOT SET
${BOUNDS_APP}             javafxlibrary.testapps.TestBoundsLocation
${WINDOW_APP}             javafxlibrary.testapps.TestMultipleWindows
${FINDER_APP}             javafxlibrary.testapps.FinderApp

*** Test Cases ***
Find With TestFX Query
   [Tags]                  smoke
   Set Test Application    ${BOUNDS_APP}
   ${rectangle}            Find            \#green
   ${text}                 Find            .whiteText
   Should Contain          ${rectangle}    Rectangle[id=green, x=300.0, y=0.0, width=150.0, height=150.0, fill=0x00a000ff]
   Should Contain          ${text}         Text[text="75x75", x=0.0, y=0.0, alignment=CENTER, origin=BASELINE

Find With XPath
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${rect_by_id}          Find                xpath=//Rectangle[@id="lime"]
    ${rect_by_fill}        Find                xpath=//Rectangle[@fill="0xff1493ff"]
    ${text}                Find                xpath=//Text[@text="75x75"]
    Should Contain         ${rect_by_id}       Rectangle[id=lime, x=500.0, y=200.0, width=75.0, height=75.0, fill=0x00ff00ff]
    Should Contain         ${rect_by_fill}     Rectangle[id=pink, x=450.0, y=75.0, width=75.0, height=75.0, fill=0xff1493ff]
    Should Contain         ${text}             Text[text="75x75", x=0.0, y=0.0, alignment=CENTER, origin=BASELINE

Find With Class
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${rectangle}           Find            class=javafx.scene.shape.Rectangle
    ${text}                Find            class=javafx.scene.text.Text
    Should Contain         ${rectangle}    Rectangle[id=red, x=0.0, y=0.0, width=300.0, height=300.0, fill=0xff0000ff]
    Should Contain         ${text}         Text[text="300x300", x=0.0, y=0.0, alignment=CENTER, origin=BASELINE

Find With CSS Query
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${rectangle}           Find            css=\#violet
    ${text}                Find            css=VBox HBox StackPane Text.whiteText
    Should Contain         ${rectangle}    Rectangle[id=violet, x=525.0, y=0.0, width=75.0, height=75.0, fill=0x9400d3ff]
    Should Contain         ${text}         Text[text="75x75", x=0.0, y=0.0, alignment=CENTER, origin=BASELINE

Find With ID
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${rectangle}           Find            id=darkblue
    Should Contain         ${rectangle}    Rectangle[id=darkblue, x=300.0, y=150.0, width=300.0, height=150.0, fill=0x00008bff]

Find With Chained Selectors
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${lime}                Find        css=VBox HBox Pane id=lime
    ${blue}                Find        css=VBox HBox Pane xpath=//Rectangle[@width="600.0"]
    Should Contain         ${lime}     Rectangle[id=lime, x=500.0, y=200.0, width=75.0, height=75.0, fill=0x00ff00ff]
    Should Contain         ${blue}     Rectangle[id=blue, x=0.0, y=0.0, width=600.0, height=300.0, fill=0x00bfffff]

Find With Root
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${root}                Find            css=Pane
    ${rectangle}           Find            id=lime    true    ${root}
    Should Contain         ${rectangle}    Rectangle[id=lime, x=500.0, y=200.0, width=75.0, height=75.0, fill=0x00ff00ff]

Find All With TestFX Query
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    @{nodes}               Find All    .whiteText
    Length Should Be       ${nodes}    3

Find All With XPath
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    @{all_rectangles}      Find All            xpath=//Rectangle
    @{text_nodes}          Find All            xpath=//Text[@text="75x75"]
    Length Should Be       ${all_rectangles}   9
    Length Should Be       ${text_nodes}       6

Find All With CSS query
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    @{nodes1}              Find All        css=VBox HBox > StackPane Rectangle
    @{nodes2}              Find All        css=Pane Rectangle
    @{nodes3}              Find All        css=Pane > Rectangle
    Length Should Be       ${nodes1}       5
    Length Should Be       ${nodes2}       3
    Length Should Be       ${nodes3}       2

Find All With Class
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${nodes}               Find All        class=javafx.scene.shape.Rectangle
    Length Should Be       ${nodes}        10

Find All With Chained Selectors
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    @{nodes1}              Find All        css=VBox HBox VBox xpath=//Rectangle
    @{nodes2}              Find All        css=VBox HBox StackPane xpath=//Rectangle[@width="75.0"]
    Length Should Be       ${nodes1}       6
    Length Should Be       ${nodes2}       4

Find All With Root
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${xroot}               Find            css=VBox HBox VBox HBox
    ${croot}               Find            css=Pane
    @{xpath}               Find All        xpath=//Rectangle[@width="75.0"]    false    ${xroot}
    @{css}                 Find All        css=StackPane > Rectangle           false    ${croot}
    Length Should Be       ${xpath}        4
    Length Should Be       ${css}          1

Find Nth Node With XPath
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${node1}               Find            xpath=/VBox/HBox/VBox/HBox/VBox/HBox/StackPane
    ${node2}               Find            xpath=/VBox/HBox/VBox/HBox/VBox/HBox/StackPane[2]
    ${child1}              Find            css=Rectangle    true    ${node1}
    ${child2}              Find            css=Rectangle    true    ${node2}
    Should Contain         ${child1}       Rectangle[id=yellow, x=450.0, y=0.0, width=75.0, height=75.0, fill=0xffff00ff]
    Should Contain         ${child2}       Rectangle[id=violet, x=525.0, y=0.0, width=75.0, height=75.0, fill=0x9400d3ff]

Find With Pseudo Class
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${root}                Find            css=VBox HBox VBox HBox StackPane
    ${target}              Find            xpath=//Text[@text="150x150"]
    Move To                ${target}
    ${result}              Find            pseudo=hover    false    ${root}
    Should Be Equal        ${result}       ${target}

Find All With Pseudo Class
    [Tags]                 smoke
    Set Test Application   ${BOUNDS_APP}
    ${node}                Find            xpath=//Text[@text="300x300"]
    Move To                ${node}
    @{hovered}             Find All        pseudo=hover
    # Nodes behind have the hover pseudostate too, Find All returns all of these except the one used as a root in lookup
    Length Should Be       ${hovered}      3
    Should Contain         ${hovered}      ${node}

Find Text Node With Text
    [Tags]                      smoke
    Set Test Application        ${BOUNDS_APP}
    ${result}                   Find            text="300x150"
    Parents Should Be Equal     ${result}       id=darkblue

Find All Text Nodes With Text
    [Tags]                      smoke
    Set Test Application        ${BOUNDS_APP}
    @{result}                   Find All        text="75x75"
    Length Should Be            ${result}       6
    Get Length                  ${result}

Nothing Is Found
    [Tags]                      smoke       negative
    Set Test Application        ${BOUNDS_APP}
    ${node}                     Find        css=NoSuchSelector
    Should Be Empty             ${node}

Nothing Is Found When failIfNotFound Is True
    [Tags]                      smoke    negative
    Set Test Application        ${BOUNDS_APP}
    ${msg}                      Run Keyword And Expect Error    *    Find    css=NoSuchSelector    true
    Should Be Equal             Find operation failed for query: "css=NoSuchSelector"     ${msg}

Nothing Is Found With Find All
    [Tags]                      smoke       negative
    Set Test Application        ${BOUNDS_APP}
    ${nodes}                    Find All    css=NoSuchSelector
    Should Be Empty             ${nodes}

Nothing Is Found With Find All When failIfNotFound Is True
    [Tags]                      smoke    negative
    Set Test Application        ${BOUNDS_APP}
    ${msg}                      Run Keyword And Expect Error    *    Find All    css=NoSuchSelector    true
    Should Be Equal             Find operation failed for query: "css=NoSuchSelector"     ${msg}

Previous Query Returns Nothing In Chained Selector
    [Tags]                      smoke    negative
    Set Test Application        ${BOUNDS_APP}
    ${node}                     Find    css=VBox css=ZBox Pane id=lime
    Should Be Empty             ${node}

Previous Query Returns Nothing In Chained Selector With Find All
    [Tags]                      smoke    negative
    Set Test Application        ${BOUNDS_APP}
    ${nodes}                    Find All    css=VBox css=ZBox Pane id=lime
    Should Be Empty             ${nodes}

Previous Query Returns Nothing In Chained Selector When failIfNotFound Is True
    [Tags]                      smoke    negative
    Set Test Application        ${BOUNDS_APP}
    ${msg}                      Run Keyword And Expect Error    *    Find    css=VBox css=ZBox Pane id=lime    true
    Should Be Equal             Find operation failed for query: "css=VBox css=ZBox Pane id=lime"    ${msg}

Previous Query Returns Nothing In Chained Selector With Find All When failIfNotFound Is True
    [Tags]                      smoke    negative
    Set Test Application        ${BOUNDS_APP}
    ${msg}                      Run Keyword And Expect Error    *    Find All    css=VBox css=ZBox Pane id=lime    true
    Should Be Equal             Find operation failed for query: "css=VBox css=ZBox Pane id=lime"    ${msg}

Find Labeled Node With Text
    [Tags]                      smoke
    Set Test Application        javafxlibrary.testapps.TestWindowManagement
    ${target}                   Find            id=navigationDialog
    ${result}                   Find            text="Dialog Example"
    Should Be Equal             ${result}       ${target}

Find All Labeled Nodes With Text
    [Tags]                      smoke
    Set Test Application        javafxlibrary.testapps.TestWindowManagement
    Open Dialog In Window Management App
    Write To                    id=nameField            labeled text
    Write To                    id=phoneField           labeled text
    Click On                    text="Add"
    ${result}                   Find All                text="labeled text"
    # Lookup returns textareas and their text as separate nodes
    Length Should Be            ${result}               4

Find TextInputControl Node With Text
    [Tags]                      smoke
    Set Test Application        javafxlibrary.testapps.TestWindowManagement
    Open Dialog In Window Management App
    Write To            id=nameField            Text input
    ${result}           Find                    text="Text input"
    ${target}           Find                    id=nameField
    Click On            text="Add"
    Should Be Equal     ${result}               ${target}

Find All TextInputControl Nodes With Text
    [Tags]                      smoke
    Set Test Application        javafxlibrary.testapps.TestWindowManagement
    Open Dialog In Window Management App
    Write To            id=nameField            Finder test
    Write To            id=phoneField           Finder test
    ${result}           Find All                text="Finder test"
    Click On            text="Add"
    # Lookup returns textareas and their text as separate nodes
    Length Should Be    ${result}               4

Find From Another Window
    [Tags]                      smoke
    Set Test Application        ${WINDOW_APP}
    ${node}                     Find            id=thirdWindowLabel
    Should End With             ${node}         Label[id=thirdWindowLabel, styleClass=label]'Third window'

Find From Another Window Using Chained Selector
    [Tags]                      smoke
    Set Test Application        ${WINDOW_APP}
    ${node}                     Find            css=HBox id=thirdWindowLabel
    Should End With             ${node}         Label[id=thirdWindowLabel, styleClass=label]'Third window'

Find All From Multiple Windows
    [Tags]                      smoke
    Set Test Application        ${WINDOW_APP}
    ${nodes}                    Find All        css=.label
    Length Should Be            ${nodes}        3

Find All From Multiple Windows Using Chained Selector
    [Tags]                      smoke
    Set Test Application        ${WINDOW_APP}
    ${nodes}                    Find All        css=HBox css=.label
    Length Should Be            ${nodes}        3

Find All From Multiple Windows Containing Multiple Matches Using Chained Selector
    [Tags]                      smoke
    Set Test Application        ${FINDER_APP}
    ${nodes}                    Find All            css=VBox css=HBox css=.button
    Length Should Be            ${nodes}            24

Find With Index
    [Tags]                      smoke
    Set Test Application        ${FINDER_APP}
    ${root}                     Get Node Parent     id=firstButton
    ${target}                   Find                xpath=/HBox/Button[4]    true    ${root}
    ${button}                   Find                css=Button[4]
    Should Be Equal             ${button}           ${target}

Find With Index - Chained Selector
    [Tags]                      smoke
    Set Test Application        ${FINDER_APP}
    ${button}                   Find                id=firstButton
    Click On                    ${button}
    ${node}                     Find                css=VBox pseudo=hover[2]
    Should Be Equal             ${node}             ${button}

Find With Index - Chained Selector Contains Index
    [Tags]                      smoke
    Set Test Application        ${FINDER_APP}
    ${node}                     Find                css=VBox[2] css=HBox Button
    ${root}                     Get Node Parent     text="Scene type 1"
    ${target}                   Find                xpath=/VBox/VBox[2]/HBox/Button    true    ${root}
    Should Be Equal             ${node}             ${target}

Find All With Index
    [Tags]                          smoke
    Set Test Application            ${FINDER_APP}
    ${nodes}                        Find All        css=Button[2]
    Length Should Be                ${nodes}        4
    All Nodes Should Have Text      ${nodes}        2

Find All With Index - Chained Selector Contains Index
    [Tags]                          smoke
    Set Test Application            ${FINDER_APP}
    ${nodes}                        Find All        css=VBox[2] css=HBox Button[1]
    Length Should Be                ${nodes}        3
    All Nodes Should Have Text      ${nodes}        5

Find With Index Below Minimum Value
    [Tags]                      smoke                           negative
    Set Test Application        ${FINDER_APP}
    ${msg}                      Run Keyword And Expect Error    *           Find        css=VBox[0]
    Should Be Equal             ${msg}                          Invalid query "css=VBox[0]": Minimum index value is 1!

*** Keywords ***
Parents Should Be Equal
    [Arguments]         ${node1}            ${node2}
    ${parent1}          Get Node Parent     ${node1}
    ${parent2}          Get Node Parent     ${node2}
    Should Be Equal     ${parent1}          ${parent2}

Open Dialog In Window Management App
    Click On            id=navigationDialog
    Click On            id=addEmployeeButton

Setup All Tests
    Import JavaFXLibrary
    Set Timeout    0

Teardown all tests
    Close Javafx Application

All Nodes Should Have Text
    [Arguments]     ${nodes}            ${text}
    FOR            ${node}             IN                  @{nodes}
                   ${value}            Get Node Text       ${node}
                   Should Be Equal     ${value}            ${text}
    END