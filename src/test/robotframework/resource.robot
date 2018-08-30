*** Keywords ***
Import JavaFXLibrary
    Run Keyword If    sys.platform.startswith('java')    Import Library    JavaFXLibrary
    ...    ELSE    Import Library    Remote    http://javafxcompile:8270    WITH NAME    RemoteJavaFXLibrary
