*** Variables ***
${appJar}    javafxlibrary-*-tests.jar
${headless}    ${False}
${host}      javafxcompile

*** Keywords ***
Import JavaFXLibrary
    Run Keyword If    sys.platform.startswith('java')    Import Library    JavaFXLibrary    ${headless}
    ...    ELSE    Import Library    Remote    http://${host}:8270    ${headless}    WITH NAME    RemoteJavaFXLibrary
    Set To Classpath    ${appJar}

Disable Embedded Image Logging For Negative Tests
    FOR    ${tag}    IN    @{TEST TAGS}
           Run Keyword If    '${tag}' == 'negative'    Set Image Logging    DISKONLY
    END

Enable Image Logging
    Set Image Logging    EMBEDDED

Set Test Application
    [Arguments]             ${application}
    Run Keyword Unless      '${CURRENT_APPLICATION}' == '${application}'    Change Current Application    ${application}

Change Current Application
    [Arguments]                     ${application}
    Run Keyword Unless              '${CURRENT_APPLICATION}' == 'NOT SET'    Close Javafx Application
    Set Suite Variable              ${CURRENT_APPLICATION}    ${application}
    Launch Javafx Application       ${application}
    Bring First Window To Front
    Set Screenshot Directory        ${OUTPUT_DIR}${/}report-images

Bring First Window To Front
    ${window_list}=     List Windows
    Set Target Window   ${window_list}[0]
