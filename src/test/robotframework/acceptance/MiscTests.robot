*** Settings ***
Documentation       Tests for AdditionalKeywords
Resource            ../resource.robot
Library             Collections
Library             String
Suite Setup         Setup All Tests
Suite Teardown      Close Javafx Application
Test Setup          Disable Embedded Image Logging For Negative Tests
Test Teardown       Enable Image Logging
Force Tags          set-misc

*** Variables ***
${CURRENT_APPLICATION}    NOT SET

*** Test Cases ***
Find Id That Does Not Exist
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${MSG}                  Run Keyword And Expect Error    *     Find    id=idThatDoesNotExist    failIfNotFound=True
    Should Be Equal         Find operation failed for query: "id=idThatDoesNotExist"       ${MSG}  msg=Find does not fail with expected error message when query not found
    ${MSG}                  Run Keyword     Find        id=idThatDoesNotExist    failIfNotFound=False
    Should Be Equal         ${EMPTY}        ${MSG}      msg=Find does not return None value when query not found

Find All With Wrong Style Class
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${MSG}                  Run Keyword And Expect Error    *    Find All    css=.thisIsNotAStyleClass    failIfNotFound=True
    Should Be Equal         Find operation failed for query: "css=.thisIsNotAStyleClass"     ${MSG}

Print Child Nodes Of Incompatible Node
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find    id=lime
    ${MSG}                  Run Keyword And Expect Error    *    Print Child Nodes    ${NODE}
    Should End With         ${MSG}    is not a subclass of javafx.scene.Parent

Call Method That Does Not Exist
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find    id=green
    ${MSG}                  Run Keyword And Expect Error    *    Call Object Method    ${NODE}    fakeMethod
    Should Be Equal         ${MSG}    class javafx.scene.shape.Rectangle has no method "fakeMethod" with arguments []

Call Method With Wrong Types
    [Tags]                  negative        smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find            id=green
    ${MSG}                  Run Keyword And Expect Error    *    Call Object Method    ${NODE}    setWidth    20
    Should End With         ${MSG}          has no method "setWidth" with arguments [class java.lang.String]

Call Method With Empty Object
    [Tags]                  negative        smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${MSG}                  Run Keyword And Expect Error    *    Call Object Method    ${EMPTY}    setWidth    20
    Should End With         ${MSG}          has no method "setWidth" with arguments [class java.lang.String]

Call Method That Does Not Exist In Fx Application Thread
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find    id=green
    ${MSG}                  Run Keyword And Expect Error    *    Call Object Method In Fx Application Thread    ${NODE}    fakeMethod
    Should Be Equal         ${MSG}    class javafx.scene.shape.Rectangle has no method "fakeMethod" with arguments []

Call Method With Wrong Types In Fx Application Thread
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find    id=green
    ${MSG}                  Run Keyword And Expect Error    *    Call Object Method In Fx Application Thread    ${NODE}    setWidth    20
    Should End With         ${MSG}    has no method "setWidth" with arguments [class java.lang.String]

Change Node ID Using Call Method
    [Tags]                  smoke    demo-set
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${original}             Find                id=yellow
    Call Object Method      ${original}         setId          importantNode
    ${modified}             Find                id=importantNode
    ${original_hash}        Fetch From Left     ${original}    [
    ${modified_hash}        Fetch From Left     ${modified}    [
    Should Be Equal         ${original_hash}    ${modified_hash}
    Wait Until Keyword Succeeds    10 s    1 s    Reset Node Id To Yellow    ${modified}    ${original}

Change Node Fill Using Call Method In JavaFX Application Thread
    [Tags]                                          smoke    demo-set
    Set Test Application                            javafxlibrary.testapps.TestBoundsLocation
    ${node}                                         Find                    id=turquoise
    ${fill}                                         Call Object Method      ${node}         getFill
    ${target}                                       Find                    id=yellow
    ${original}                                     Call Object Method      ${target}       getFill
    Call Object Method In Fx Application Thread     ${target}               setFill         ${fill}
    Wait For Events In Fx Application Thread
    ${result}                                       Call Object Method      ${target}       getFill
    Should End With                                 ${result}               00ffe9ff
    # Reset original fill value
    Call Object Method In Fx Application Thread     ${target}               setFill         ${original}
    Wait For Events In Fx Application Thread
    ${after_reset}                                  Call Object Method      ${target}       getFill
    Should End With                                 ${after_reset}          ffff00ff

Set Node Visibility (Call Method With Argument Types That Require Casting)
    [Tags]                                          smoke       demo-set    negative
    Set Test Application                            javafxlibrary.testapps.TestBoundsLocation
    ${node}    Find                                 id=yellow
    Node Should Be Visible                          ${node}
    Run Keyword And Expect Error                    *          Node Should Not Be Visible      ${node}
    Call Object Method In Fx Application Thread     ${node}     setVisible                  (boolean)false
    Wait For Events In Fx Application Thread
    Run Keyword And Expect Error                    *           Node Should Be Visible      ${node}
    Node Should Not Be Visible                      ${node}
    #Reset visibility to true
    Call Object Method In Fx Application Thread     ${node}     setVisible                  (boolean)true
    Wait For Events In Fx Application Thread
    Node Should Be Visible                          ${node}

Check That Element Is Hoverable
    [Tags]                                          smoke    demo-set      hoverable
    Set Test Application                            javafxlibrary.testapps.TestClickRobot
    ${target_node}=                                 Find    id=resetButton
    Call Object Method In Fx Application Thread     ${target_node}    setVisible     (boolean)false
    Move To Coordinates                             x=0    y=0
    Run Keyword And Expect Error                    *          Node Should Be Hoverable      ${target_node}
    Call Object Method In Fx Application Thread     ${target_node}    setVisible     (boolean)true
    Move To Coordinates                             x=0    y=0
    Node Should Be Hoverable                        id=resetButton

Check That Element Is Not Hoverable
    [Tags]                      smoke   demo-set    negative             hoverable
    Set Test Application                            javafxlibrary.testapps.TestClickRobot
    ${target_node}=                                 Find    id=resetButton
    Move To Coordinates                             x=0    y=0
    Run Keyword And Expect Error                    *          Node Should Not Be Hoverable      ${target_node}
    Call Object Method In Fx Application Thread     ${target_node}    setVisible     (boolean)false
    Move To Coordinates                             x=0    y=0
    Node Should Not Be Hoverable                    id=resetButton

Test Verify Keywords With Non-existing Locator
    [Tags]                                          smoke    demo-set      negative
    Set Test Application                            javafxlibrary.testapps.TestClickRobot
    Run Keyword And Expect Error                    Given locator "id=doesNotExist" was not found.    Node Should Be Hoverable       id=doesNotExist
    Run Keyword And Expect Error                    Given locator "id=doesNotExist" was not found.    Node Should Not Be Hoverable   id=doesNotExist

Find From Node
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find                    id=yellow
    ${ROOT}                 Get Root Node Of        ${NODE}
    ${RESULT}               Call Object Method      ${ROOT}         lookup    HBox VBox HBox VBox HBox StackPane
    ${RECT}                 Find                    class=javafx.scene.shape.Rectangle       root=${RESULT}
    Should Be Equal         ${NODE}                 ${RECT}

Find All From Node
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${YELLOW}               Find                    id=yellow
    ${VIOLET}               Find                    id=violet
    ${ROOT}                 Get Root Node Of        ${YELLOW}
    ${RESULT}               Call Object Method      ${ROOT}         lookup      HBox VBox HBox VBox HBox
    @{RECT}                 Find All                css=Rectangle       root=${RESULT}
    Should Be Equal         ${YELLOW}               ${RECT}[0]
    Should Be Equal         ${VIOLET}               ${RECT}[1]

Get Node Children By Class Name
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${YELLOW}               Find                                id=yellow
    ${VIOLET}               Find                                id=violet
    ${ROOT}                 Get Root Node Of                    ${YELLOW}
    ${RESULT}               Call Object Method                  ${ROOT}         lookup    HBox VBox HBox VBox HBox
    @{RECT}                 Find All                            class=javafx.scene.shape.Rectangle       root=${RESULT}
    Should Contain          ${RECT}                             ${YELLOW}
    Should Contain          ${RECT}                             ${VIOLET}

Get Node Text Of Incompatible Node
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find        id=green
    ${MSG}                  Run Keyword And Expect Error    *    Get Node Text    ${NODE}
    Should End With         ${MSG}      Node has no getText method

Wait Until Element Exists
    [Tags]                          negative   smoke
    Set Test Application            javafxlibrary.testapps.TestWindowManagement
    Click On                        id=navigationAlert
    Run Keyword And Expect Error    Given element "css=.dialog-pane .button" was not found within given timeout of 2 SECONDS
    ...                             Wait Until Element Exists     css=.dialog-pane .button    ${2}
    Click On                        css=.button
    Wait Until Element Exists       css=.dialog-pane .button    ${5}
    Click On                        css=.dialog-pane .button

Wait Until Element Does Not Exists
    [Tags]                              negative   smoke
    Set Test Application                javafxlibrary.testapps.TestWindowManagement
    Click On                            id=navigationAlert
    Wait Until Element Does Not Exists  css=.dialog-pane .button    ${5}
    Click On                            css=.button
    Wait Until Element Exists           css=.dialog-pane .button    ${5}
    Run Keyword And Expect Error        Given element "css=.dialog-pane .button" was still found within given timeout of 2 SECONDS
    ...                                 Wait Until Element Does Not Exists     css=.dialog-pane .button    ${2}
    Click On                            css=.dialog-pane .button

Wait Until Node Is Visible
    [Tags]                          smoke
    Set Test Application            javafxlibrary.testapps.TestWindowManagement
    Click On                        id=navigationAlert
    Run Keyword And Expect Error    Given element "css=.dialog-pane .button" was not found within given timeout of 2 SECONDS
    ...                             Wait Until Node Is Visible     css=.dialog-pane .button    ${2}
    Click On                        css=.button
    Wait Until Node Is Visible      css=.dialog-pane .button    ${5}
    Click On                        css=.dialog-pane .button

Wait Until Node Is Not Visible
    [Tags]                          smoke
    Set Test Application            javafxlibrary.testapps.TestWindowManagement
    Click On                        id=navigationAlert
    ${target_node}=                 Find    css=.button
    Call Object Method In Fx Application Thread     ${target_node}    setVisible     (boolean)false
    Wait Until Node Is Not Visible                  ${target_node}    ${5}
    Call Object Method In Fx Application Thread     ${target_node}    setVisible     (boolean)true
    Click On                        css=.button
    Run Keyword And Expect Error    REGEXP:Given target ".*" did not become invisible within given timeout of 2 SECONDS
    ...                             Wait Until Node Is Not Visible      css=.dialog-pane .button    ${2}
    Click On                        css=.dialog-pane .button

Wait Until Node Is Enabled
    [Tags]                          smoke
    Set Test Application            javafxlibrary.testapps.TestWindowManagement
    Click On                        id=navigationAlert
    ${target_node}=                 Find    css=.button
    Call Object Method In Fx Application Thread     ${target_node}    setDisable     (boolean)true
    ${msg}=                         Run Keyword And Expect Error    REGEXP:Given target.*did not become enabled within given timeout of 2 seconds.
    ...                             Wait Until Node Is Enabled     css=.button    ${2}
    Call Object Method In Fx Application Thread     ${target_node}    setDisable     (boolean)false
    Wait Until Node Is Enabled      css=.button    ${5}

Wait Until Node Is Not Enabled
    [Tags]                          smoke
    Set Test Application            javafxlibrary.testapps.TestWindowManagement
    Click On                        id=navigationAlert
    ${target_node}=                 Find    css=.button
    ${msg}=     Run Keyword And Expect Error    REGEXP:Given target.*did not become disabled within given timeout of 2 seconds.
    ...                             Wait Until Node Is Not Enabled     css=.button    ${2}
    Call Object Method In Fx Application Thread     ${target_node}    setDisable     (boolean)true
    Wait Until Node Is Not Enabled                  css=.button    ${5}
    [Teardown]       Call Object Method In Fx Application Thread     ${target_node}    setDisable     (boolean)false

Find All With Pseudo Class
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestClickRobot
    ${NODE}                 Find    id=rightClickButton
    Move To                 ${NODE}
    @{LIST}                 Find All      css=HBox pseudo=hover    failIfNotFound=True
    Should Be Equal         ${NODE}       ${LIST}[0]

Get Table Column Count
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    ${TABLE}                Find    id=table
    ${COLUMNS}              Get Table Column Count    ${TABLE}
    Should Be Equal         ${COLUMNS}    ${5}

Get Table Cell Value
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    ${TABLE}                Find                    id=table
    ${NICK1}                Get Table Cell Value    ${TABLE}    ${0}    ${0}
    ${NICK2}                Get Table Cell Value    ${TABLE}    ${1}    ${0}
    Should Be Equal         ${NICK1}                Oskar
    Should Be Equal         ${NICK2}                Joseph
    ${RATING1}              Get Table Cell Value    ${TABLE}    ${0}    ${4}
    ${RATING2}              Get Table Cell Value    ${TABLE}    ${9}    ${4}
    Should Be Equal         ${RATING1}              ${1.33}
    Should Be Equal         ${RATING2}              ${1.15}

Get Table Column Values
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    ${table}                Find                        id=table
    @{target}               Get List Of All Players
    @{values}               Get Table Column Values     ${table}    ${0}
    Lists Should Be Equal   ${target}                   ${values}

Get Table Row Values
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    ${table}                Find                    id=table
    @{target}               Create List             Joseph      ${264}    ${1749}    ${1.46}    ${1.28}
    @{values}               Get Table Row Values    ${table}    ${1}
    Lists Should Be Equal   ${target}               ${values}

Order Table Values By Different Columns
    [Tags]                  smoke    demo-set
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    Click On                text="Player"
    ${FIRST_NICK}           Get First Player
    Click On                text="Player"
    ${LAST_NICK}            Get First Player
    Click On                text="Maps"
    ${LEAST_MAPS}           Get First Player
    Click On                text="KDR"
    ${WORST_KDR}            Get First Player
    Click On                text="Rating"
    Click On                text="Rating"
    ${BEST_RATING}          Get First Player
    Should Be Equal         ${FIRST_NICK}       Alice
    Should Be Equal         ${LAST_NICK}        Wallace
    Should Be Equal         ${LEAST_MAPS}       Rosa
    Should Be Equal         ${WORST_KDR}        Marko
    Should Be Equal         ${BEST_RATING}      Oskar

Get Table Cell Value Using Index That Is Out Of Bounds
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    ${TABLE}                Find    id=table
    ${MSG}                  Run Keyword And Expect Error    *    Get Table Cell Value    ${TABLE}    0    40
    Should Be Equal         ${MSG}    Out of table bounds: Index: 40, Size: 5

Get Object Property
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestClickRobot
    ${node}                 Find                    id=button
    ${width}                Get Object Property     ${node}         _width
    Should Be Equal         ${width}                ${300.0}

    # Value can be obtained from the property wrapper too by calling Get Object Property again:
    ${wrapper}              Get Object Property     ${node}         width
    ${width2}               Get Object Property     ${wrapper}      value
    Should Be Equal         ${width2}               ${300.0}

Get Pseudostates With Get Object Property
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestClickRobot
    ${node}                 Find                    id=button
    ${pseudostates}         Get Object Property     ${node}    pseudoClassStates
    Should Contain          ${pseudostates}         focused
    Move To                 ${node}
    ${pseudostates}         Get Object Property     ${node}    pseudoClassStates
    Should Contain          ${pseudostates}         hover

Print Object Properties
    [Tags]                      smoke
    Set Test Application        javafxlibrary.testapps.TestClickRobot
    ${node}                     Find        id=button
    Print Object Properties     ${node}

Get Node Image Url With Get Object Property
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestScrollRobot2
    ${node}                 Find                    id=imageView
    ${image}                Get Object Property     ${node}             oldImage
    ${url}                  Get Object Property     ${image}            url
    Should End With         ${url}                  /fxml/javafxlibrary/ui/uiresources/ejlogo.png

Get Scene (Node)
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestScrollRobot2
    ${node}                 Find                id=imageView
    ${scene}                Get Scene           ${node}
    ${target}               Get Root Node Of    ${node}
    ${result}               Get Root Node Of    ${scene}
    Should Be Equal         ${target}           ${result}

Get Scene (String)
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestScrollRobot2
    ${scene}                Get Scene           id=imageView
    ${target}               Get Root Node Of    id=imageView
    ${result}               Get Root Node Of    ${scene}
    Should Be Equal         ${target}           ${result}

Get Scene (Window)
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestScrollRobot2
    ${window}               Get Window          title=ScrollRobot Test 2
    ${scene}                Get Scene           ${window}
    ${target}               Get Root Node Of    ${window}
    ${result}               Get Root Node Of    ${scene}
    Should Be Equal         ${target}           ${result}

Is not Java agent
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${IS_JAVA_AGENT} =      Is Java Agent
    Should Be Equal         ${False}            ${IS_JAVA_AGENT}

Library Keyword Timeout Should Not Happen
    [Tags]    smoke
    ${old_timeout}=   Set Timeout      2
    Set Test Application    javafxlibrary.testapps.TestKeyboardRobot
    Run Keyword And Expect Error    Given element "id=doesNotExist" was not found within given timeout of 3 SECONDS
    ...                             Wait Until Element Exists          id=doesNotExist     timeout=3
    [Teardown]   Set Timeout           ${old_timeout}

Library Keyword Timeout Should Happen
    [Tags]    smoke
    ${old_timeout}=        Set Timeout       2
    ${old_write_speed}=    Set Write Speed   1000
    Set Test Application   javafxlibrary.testapps.TestKeyboardRobot
    Clear Textarea
    Run Keyword And Expect Error    Library keyword timeout (2s) for keyword: write
    ...                             Write                   Robot Framework
    [Teardown]   Run Keywords       Set Timeout           ${old_timeout}     AND     Set Write Speed     ${old_write_speed}

*** Keywords ***
Setup All Tests
    Import JavaFXLibrary

Get First Player
    ${TABLE}        Find                    id=table
    ${PLAYER}       Get Table Cell Value    ${TABLE}    0    0
    [Return]        ${PLAYER}

Get List Of All Players
    @{list}    Create List    Oskar    Joseph    Katrina    James    Wallace    Horton    Leila    Amber    Kirsten
    Append To List    ${list}    Rosa    Preston    Garrett    Mike    John    Donald    Michael    Kelly    Robin
    Append To List    ${list}    Alice    Johannes    Juhani    Tuukka    Mika    Petteri    Marko
    [return]    @{list}

Reset Node Id To Yellow
    [Arguments]             ${node}             ${original}
    Call Object Method      ${node}             setId       yellow
    ${after_reset}          Find                id=yellow
    Should Be Equal         ${after_reset}      ${original}

Clear Textarea
    Click On    id=resetButton
    Click On    id=textArea