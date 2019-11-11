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
    :FOR    ${tag}    IN    @{TEST TAGS}
    \       Run Keyword If    '${tag}' == 'negative'    Set Image Logging    DISKONLY

Enable Image Logging
    Set Image Logging    EMBEDDED
