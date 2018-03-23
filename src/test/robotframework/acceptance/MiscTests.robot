*** Settings ***
Documentation       Tests for AdditionalKeywords
Library             JavaFXLibrary
Library             Collections
Suite Teardown      Close Javafx Application

*** Variables ***
${CURRENT_APPLICATION}    NOT SET

*** Test Cases ***
Find Id That Does Not Exist
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${MSG}=                 Run Keyword And Expect Error    *     Find        \#idThatDoesNotExist    failIfNotFound=True
    Should Be Equal         ${MSG}    Unable to find anything with query: "#idThatDoesNotExist"       msg=Find does not fail with expected error message when query not found
    ${MSG}=                 Run Keyword           Find      \#idThatDoesNotExist          failIfNotFound=False
    Should Be Equal         ${MSG}    ${EMPTY}    msg=Find does not return None value when query not found

Find All With Wrong Style Class
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${MSG}                  Run Keyword And Expect Error    *    Find All    .thisIsNotAStyleClass    failIfNotFound=True
    Should Be Equal         ${MSG}    Unable to find anything with query: ".thisIsNotAStyleClass"

Print Child Nodes Of Incompatible Node
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find    \#lime
    ${MSG}                  Run Keyword And Expect Error    *    Print Child Nodes    ${NODE}
    Should End With         ${MSG}    is not a subclass of javafx.scene.Parent

Call Method That Does Not Exist
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find    \#green
    ${MSG}                  Run Keyword And Expect Error    *    Call Object Method    ${NODE}    fakeMethod
    Should Be Equal         ${MSG}    class javafx.scene.shape.Rectangle has no method "fakeMethod()"

Call Method With Wrong Types
    [Tags]    negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}    Find    \#green
    @{ARGS}    Create List    ${20}
    @{ARG_TYPES}    Create List    int
    ${MSG}    Run Keyword And Expect Error    *    Call Object Method    ${NODE}    setWidth    ${ARGS}    ${ARG_TYPES}
    Should Be Equal    ${MSG}    class javafx.scene.shape.Rectangle has no method "setWidth" with arguments [int]

Call Method That Does Not Exist In Fx Application Thread
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find    \#green
    ${MSG}                  Run Keyword And Expect Error    *    Call Object Method In Fx Application Thread    ${NODE}    fakeMethod
    Should Be Equal         ${MSG}    class javafx.scene.shape.Rectangle has no method "fakeMethod()"

Call Method With Wrong Types In Fx Application Thread
    [Tags]    negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}    Find    \#green
    @{ARGS}    Create List    ${20}
    @{ARG_TYPES}    Create List    int
    ${MSG}    Run Keyword And Expect Error    *    Call Object Method In Fx Application Thread    ${NODE}    setWidth    ${ARGS}    ${ARG_TYPES}
    Should Be Equal    ${MSG}    class javafx.scene.shape.Rectangle has no method "setWidth" with arguments [int]

Find From Node
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find    \#yellow
    @{ARGS}                 Create List    HBox VBox HBox VBox HBox StackPane
    @{TYPES}                Create List    java.lang.String
    ${ROOT}                 Get Root Node Of    ${NODE}
    ${RESULT}               Call Object Method    ${ROOT}    lookup    ${ARGS}    ${TYPES}
    ${RECT}                 Find From Node    ${RESULT}    Rectangle
    Should Be Equal         ${NODE}    ${RECT}

Find All From Node
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${YELLOW}               Find    \#yellow
    ${VIOLET}               Find    \#violet
    @{ARGS}                 Create List    HBox VBox HBox VBox HBox
    @{TYPES}                Create List    java.lang.String
    ${ROOT}                 Get Root Node Of    ${YELLOW}
    ${RESULT}               Call Object Method    ${ROOT}    lookup    ${ARGS}    ${TYPES}
    @{RECT}                 Find All From Node    ${RESULT}    Rectangle
    Should Be Equal         ${YELLOW}    @{RECT}[0]
    Should Be Equal         ${VIOLET}    @{RECT}[1]

Get Node Children By Class Name
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${YELLOW}               Find    \#yellow
    ${VIOLET}               Find    \#violet
    @{ARGS}                 Create List    HBox VBox HBox VBox HBox
    @{TYPES}                Create List    java.lang.String
    ${ROOT}                 Get Root Node Of    ${YELLOW}
    ${RESULT}               Call Object Method    ${ROOT}    lookup    ${ARGS}    ${TYPES}
    @{RECT}                 Get Node Children By Class Name    ${RESULT}    Rectangle
    Should Contain          ${RECT}    ${YELLOW}
    Should Contain          ${RECT}    ${VIOLET}

Get Node Text Of Incompatible Node
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestBoundsLocation
    ${NODE}                 Find    \#green
    ${MSG}                  Run Keyword And Expect Error    *    Get Node Text    ${NODE}
    Should End With         ${MSG}    Node has no method getText().

Wait Until Node Is Enabled
    [Tags]                          smoke
    Set Test Application            javafxlibrary.testapps.TestWindowManagement
    Click On                        \#navigationAlert
    Click On                        .button
    Wait Until Node Is Enabled      .dialog-pane .button    ${5}
    Click On                        .dialog-pane .button

Find All With Pseudo Class
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestClickRobot
    ${NODE}                 Find    \#rightClickButton
    Move To                 ${NODE}
    @{LIST}                 Find All With Pseudo Class    .button    :hover
    Should Be Equal         @{LIST}[0]    ${NODE}

Get Table Column Count
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    ${TABLE}                Find    \#table
    ${COLUMNS}              Get Table Column Count    ${TABLE}
    Should Be Equal         ${COLUMNS}    ${5}

Get Table Cell Value
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    ${TABLE}                Find    \#table
    ${NICK1}                Get Table Cell Value    ${TABLE}    ${0}    ${0}
    ${NICK2}                Get Table Cell Value    ${TABLE}    ${1}    ${0}
    Should Be Equal         ${NICK1}    Oskar
    Should Be Equal         ${NICK2}    Joseph
    ${RATING1}              Get Table Cell Value    ${TABLE}    ${0}    ${4}
    ${RATING2}              Get Table Cell Value    ${TABLE}    ${9}    ${4}
    Should Be Equal         ${RATING1}    ${1.33}
    Should Be Equal         ${RATING2}    ${1.15}

Get Table Column Values
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    ${table}                Find    \#table
    @{target}               Get List Of All Players
    @{values}               Get Table Column Values    ${table}    ${0}
    Lists Should Be Equal   ${target}    ${values}

Get Table Row Values
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    ${table}                Find    \#table
    @{target}               Create List    Joseph    ${264}    ${1749}    ${1.46}    ${1.28}
    @{values}               Get Table Row Values    ${table}    ${1}
    Lists Should Be Equal   ${target}    ${values}

Order Table Values By Different Columns
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    Click On                Player
    ${FIRST_NICK}           Get First Player
    Click On                Player
    ${LAST_NICK}            Get First Player
    Click On                Maps
    ${LEAST_MAPS}           Get First Player
    Click On                KDR
    ${WORST_KDR}            Get First Player
    Click On                Rating
    Click On                Rating
    ${BEST_RATING}          Get First Player
    Should Be Equal         ${FIRST_NICK}       Alice
    Should Be Equal         ${LAST_NICK}        Wallace
    Should Be Equal         ${LEAST_MAPS}       Rosa
    Should Be Equal         ${WORST_KDR}        Marko
    Should Be Equal         ${BEST_RATING}      Oskar

Get Table Cell Value Using Index That Is Out Of Bounds
    [Tags]                  negative    smoke
    Set Test Application    javafxlibrary.testapps.TestTableManagement
    ${TABLE}                Find    \#table
    ${MSG}                  Run Keyword And Expect Error    *    Get Table Cell Value    ${TABLE}    0    40
    Should Be Equal         ${MSG}    Out of table bounds: Index: 40, Size: 5

Get Object Property
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestClickRobot
    ${node}                 Find    \#button
    ${width}                Get Object Property    ${node}    _width
    Should Be Equal         ${width}    ${300.0}

    # Value can be obtained from the property wrapper too by calling Get Object Property again:
    ${wrapper}              Get Object Property    ${node}    width
    ${width2}               Get Object Property    ${wrapper}    value
    Should Be Equal         ${width2}    ${300.0}

Get Pseudostates With Get Object Property
    [Tags]                  smoke
    Set Test Application    javafxlibrary.testapps.TestClickRobot
    ${node}    Find         \#button
    ${pseudostates}         Get Object Property    ${node}    pseudoClassStates
    Should Contain          ${pseudostates}    focused
    Move To                 ${node}
    ${pseudostates}         Get Object Property    ${node}    pseudoClassStates
    Should Contain          ${pseudostates}    hover

Print Object Properties
    [Tags]                      smoke
    Set Test Application        javafxlibrary.testapps.TestClickRobot
    ${node}                     Find    \#button
    Print Object Properties     ${node}

*** Keywords ***
Set Test Application
    [Arguments]    ${APPLICATION}
    Run Keyword Unless    '${CURRENT_APPLICATION}' == '${APPLICATION}'    Change Current Application    ${APPLICATION}

Change Current Application
    [Arguments]    ${APPLICATION}
    Run Keyword Unless    '${CURRENT_APPLICATION}' == 'NOT SET'    Close Javafx Application
    Set Suite Variable    ${CURRENT_APPLICATION}    ${APPLICATION}
    Launch Javafx Application    ${APPLICATION}
    Set Screenshot Directory     ${OUTPUT_DIR}${/}report-images

Get First Player
    ${TABLE}        Find    \#table
    ${PLAYER}       Get Table Cell Value    ${TABLE}    0    0
    [Return]        ${PLAYER}

Get List Of All Players
    @{list}    Create List    Oskar    Joseph    Katrina    James    Wallace    Horton    Leila    Amber    Kirsten
    Append To List    ${list}    Rosa    Preston    Garrett    Mike    John    Donald    Michael    Kelly    Robin
    Append To List    ${list}    Alice    Johannes    Juhani    Tuukka    Mika    Petteri    Marko
    [return]    @{list}