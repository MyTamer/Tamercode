    private static final void makeWF_BasicRebolWriterFormat_jwf(Hashtable pWriterFormats) {
        pWriterFormats.put("BasicRebolWriterFormat.jwf", "!<header>\nREBOL [\n!<rebolheader>\n]\n\n\n\n!<{import>; section:import\n!<import>!<}import>\n\n\n\n; Constants << ;\n \n; Command line arguments\nWRITER__ARGUMENT_CONTROL_PREFIX:         \"$<main.CodeWriter.argument.ControlPrefix>\"\nWRITER__ARGUMENT_OutputFolder:           \"$<main.CodeWriter.argument.name.OutputFolder>\"\nWRITER__ARGUMENT_MetaPropFile:           \"$<main.CodeWriter.argument.name.MetaPropFile>\"\nWRITER__ARGUMENT_BackupFolder:           \"$<main.CodeWriter.argument.name.BackupFolder>\"\nWRITER__ARGUMENT_NoBackup:               \"$<main.CodeWriter.argument.name.NoBackup>\"\n\n; User Interface text message parts\nWRITER__UITEXT_Method:                   \"method \"\nWRITER__UITEXT_Main:                     \"main \"\nWRITER__UITEXT_ExceptionIn:              \"Exception in \"\nWRITER__UITEXT_ColonNewLine:             \":^/\"\nWRITER__UITEXT_NewLine:                  \"^/\"\nWRITER__UITEXT_Section:                  \"section \"\nWRITER__UITEXT_SavedFile:                \"Saved file:       \"\nWRITER__UITEXT_UnableToSaveFile:         \"Unable to save file: \"\nWRITER__UITEXT_UnableToBackupFile:       \"Unable to backup file: \"\nWRITER__UITEXT_ToBackupFolder:           \" to backup folder: \"\nWRITER__UITEXT_BackupFolderColon:        \"Backup folder: \"\nWRITER__UITEXT_BackupFolderExistFailure: \" does not exist and cannot be created.\"\nWRITER__UITEXT_BackupFolderNotAFolder:   \" is not a folder.\"\nWRITER__UITEXT_BackupFolderNotWritable:  \" is not writable.\"\nWRITER__UITEXT_CodeWriterState:          \"Code Writer State: \"\nWRITER__UITEXT_GetFileIndexEquals:       \"^/_getFileIndex()    \"\nWRITER__UITEXT_GetFullFileNameEquals:    \"^/_getFullFileName() \"\nWRITER__UITEXT_GetOutputFolderEquals:    \"^/_getOutputFolder() \"\nWRITER__UITEXT_ErrorHeader:              \"^/^/--- CodeWriter Error Description Start ---^/^/\"\nWRITER__UITEXT_ErrorFooter:              \"^/--- CodeWriter Error Description End -----^/^/\"\nWRITER__UITEXT_UnableToLoadMetaProps:    \"Unable to load metadata from file: \"\nWRITER__UITEXT_PlaceHolderException:     \"This placeholder Exception should never be thrown: there is an error in the WriterFormat.\"\n\n; String constants\nWRITER__STRING_empty:                    \"\"\nWRITER__STRING_dot:                      \".\"\nWRITER__STRING_separator:                \"$<\\jostraca.system.fileSeparator>\"\n\n; Constants >> ;\n\n\n\n\n; Writer Variables << ;\n\nwriter__iFileNameRoots:      []                   ; generated file name roots\nwriter__iNumFiles:           0                    ; number of generated files\n  \nwriter__iFileNamePrefix:     WRITER__STRING_empty ; failsafe default\nwriter__iFileNameSuffix:     WRITER__STRING_empty ; failsafe default\n\nwriter__iBackupPrefix:       WRITER__STRING_empty ; failsafe default\nwriter__iBackupSuffix:       WRITER__STRING_empty ; failsafe default\n\nwriter__iCurrentText:        WRITER__STRING_empty ; current text\nwriter__iCurrentFileIndex:   0                    ; current file index\n\nwriter__iArgs:               []                   ; cmd line args\nwriter__iNumArgs:            0                    ; number of cmd line args\n\nwriter__iSave:               true                 ; save generated code to disk\nwriter__iBackup:             true                 ; make backups\n\nwriter__iOutputFolder:       WRITER__STRING_dot   ; written code is output to this folder\nwriter__iBackupFolder:       WRITER__STRING_dot   ; overwritten files are placed here\nwriter__iMetaPropFile:       WRITER__STRING_empty ; metadata properties file path\n\nwriter__iProperties:         []                   ; lookup table for compile time properties\nwriter__iPropertiesInit:     false                ; true => lookup table initialiased\n\n; Writer Variables >> ;\n\n\n\n\n!<{declare>; section:declare\n!<declare>!<}declare>\n\n\n\n\n; Template Services (File Generation) << ;\n\n; Set the prefix of the files to be generated. \n_setFileNamePrefix: func [ pPrefix ] [\n   writer__iFileNamePrefix: pPrefix\n]\n\n\n\n; Get prefix of files to be generated.\n_getFileNamePrefix: func [][\n  writer__iFileNamePrefix\n]\n\n\n\n; Set the suffix of the files to be generated. \n_setFileNameSuffix: func [ pSuffix ] [\n  writer__iFileNameSuffix: pSuffix\n]\n\n\n\n; Get suffix of files to be generated.\n_getFileNameSuffix: func[] [\n  writer__iFileNameSuffix\n]\n\n\n\n; Set the full name of the file to be generated. \n; Prefix and Suffix are set to empty.\n_setFullFileName: func [ pName ] [\n  _setFileNamePrefix  WRITER__STRING_empty\n  _setFileNameRoot    pName\n  _setFileNameSuffix  WRITER__STRING_empty\n]\n\n\n\n; Get the full name of current file being generated.\n_getFullFileName: func[] [\n   rejoin reduce [ _getFileNamePrefix _getFileNameRoot _getFileNameSuffix ]\n]\n\n\n\n; Set the names of the files to be generated. \n; Prefix and Suffix are set to empty.\n_setFullFileNames: func [ pNames ] [\n  _setFileNamePrefix WRITER__STRING_empty\n  _setFileNameRoots  pNames\n  _setFileNameSuffix WRITER__STRING_empty\n]\n\n\n\n; Get the full names of the files to be generated.\n_getFullFileNames: func [ /local fileNameRoot fileNameRoots fullFileNames fileNamePrefix fileNameSuffix ] [\n\n  fileNameRoots:  _getFileNameRoots\n  fullFileNames:  copy []\n  fileNamePrefix: _getFileNamePrefix\n  fileNameSuffix: _getFileNameSuffix\n\n  foreach fileNameRoot fileNameRoots [\n    append fullFileNames   rejoin reduce [ fileNamePrefix fileNameRoot fileNameSuffix ]\n  ]\n\n  fullFileNames\n]\n\n\n\n; Set the root of the name of the file to be generated. \n_setFileNameRoot: func [ pFileNameRoot ] [\n  _setFileNameRoots reduce [ pFileNameRoot ]\n]\n\n\n\n; Get the root of the name of current file being generated.\n_getFileNameRoot: func [] [\n   pick writer__iFileNameRoots ( writer__iCurrentFileIndex + 1 )\n]\n\n\n\n; Get roots of the names of files to be generated.\n_getFileNameRoots: func [] [\n  head writer__iFileNameRoots\n]\n\n\n\n; Set the roots of the names of the files to be generated. \n_setFileNameRoots: func [ pFileNameRoots ] [\n  writer__iFileNameRoots: pFileNameRoots\n  writer__iNumFiles:      length? writer__iFileNameRoots\n]\n\n\n\n; Get index of file currently being generated. \n_getFileIndex: func[] [\n  writer__iCurrentFileIndex\n]\n\n\n\n; Get number of files to generate.\n_getNumFiles: func [] [\n  writer__iNumFiles\n]\n\n\n\n; Set output folder.\n_setOutputFolder: func [ pFolder ] [\n  writer__iOutputFolder: pFolder\n]\n\n\n\n; Get output folder.\n_getOutputFolder: func [] [\n  writer__iOutputFolder\n]\n\n\n\n; Set backup folder.\n_setBackupFolder: func [ pBackupFolder ] [\n  writer__iBackupFolder: pBackupFolder\n]\n\n\n\n; Get backup folder.\n_getBackupFolder: func[] [\n  writer__iBackupFolder\n]\n\n\n\n; Set the prefix of backup files.\n_setBackupPrefix: func [ pBackupPrefix ] [\n  writer__iBackupPrefix: pBackupPrefix\n]\n\n\n\n; Set the suffix of backup files.\n_setBackupSuffix: func [ pBackupSuffix ] [\n  writer__iBackupSuffix: pBackupSuffix\n]\n\n\n\n; Set to true if generated files are to be backed up to disk automatically.\n_backup: func [ pBackup ] [\n  writer__iBackup: pBackup\n]\n\n\n\n; Set to true if generated files are to be saved to disk automatically. \n_save: func [ pSave ] [\n  writer__iSave: pSave\n]\n\n\n\n; Save a text file.\n_saveTextFile: func [ pFilePath pContent ] [\n  write  make file! pFilePath pContent\n  true\n]\n\n\n\n; Load a text file.\n_loadTextFile: func [ pFilePath /local content ] [\n  content: read make file! pFilePath\n  content\n]\n\n; Template Services (File Generation) >> ;\n\n\n\n\n; Template Services (Control) << ;\n\n; Get the value of a compile-time property.\n_getProperty: func [ pName ] [\n  if not writer__iPropertiesInit [\n    writer__initProperties\n  ]\n\n  select writer__iProperties pName\n]\n\n\n\n; Get first user arg.\n_getFirstUserArg: func [] [\n   _getUserArg 0\n]\n\n\n\n; Get second user arg.\n_getSecondUserArg: func [] [\n  _getUserArg 1\n]\n\n\n\n; Get third user arg.\n_getThirdUserArg: func [] [\n  _getUserArg 2\n]\n\n\n\n; Get nth (n=0,1,2,...) user arg (the nth arg with no WRITER__ARGUMENT_CONTROL_PREFIX).\n_getUserArg: func [ pOrdinal /local userArgs ] [\n  either pOrdinal < _getNumUserArgs [\n    userArgs: _getUserArgs\n    pick userArgs ( pOrdinal + 1 )\n  ][\n    WRITER__STRING_empty\n  ]\n]\n\n\n\n; Get user command line arguments to CodeWriter. \n_getUserArgs: func [] [\n  writer__iUserArgs\n]\n\n\n\n; Get number of user command line arguments to CodeWriter.\n_getNumUserArgs: func [] [\n  writer__iNumUserArgs\n]\n\n\n\n; Get command line arguments to CodeWriter. \n_getArgs: func [] [\n  writer__iArgs\n]\n\n\n\n; Get number of command line arguments to CodeWriter.\n_getNumArgs: func [] [\n  writer__iNumArgs;\n]\n\n; Template Services (Control) >> ;\n\n\n\n\n; Template Services (Text Production) << #\n\n; Insert text into generated file.\n_insert: func [ pText ] [\n  writer__iCurrentText:  rejoin [ writer__iCurrentText pText ]\n]\n\n\n\n; Create a String containing specified number of spaces.\n_spaces: func [ pNumSpaces ] [\n  numSpaces: either pNumSpaces < 0 [ -1 * pNumSpaces][ pNumSpaces ]\n  spaces:    copy WRITER__STRING_empty\n  while [ numSpaces > 0 ] [\n    spaces:    rejoin [ spaces \" \" ]\n    numSpaces: numSpaces - 1\n  ]\n  spaces\n]\n\n\n\n; Left align String with spaces. \n_left: func [ pText pColWidth ] [\n  _align pText \" \" pColWidth \"l\"\n] \n\n\n\n; Right align String with spaces. \n_right: func [ pText pColWidth ] [\n  _align pText \" \" pColWidth \"r\"\n] \n\n\n; Center align String with spaces. \n_center: func [ pText pColWidth ] [\n  _align pText \" \" pColWidth \"c\"\n] \n\n\n\n; Align text within background text with specified column width.\n; Alignment can be 'l': left, 'c': center, 'r': right\n_align: func[ pText pBackText pColWidth pAlignment /local result \n                                                          textLen \n                                                          backTextLen\n                                                          remainWidth\n                                                          backTextRepeats\n                                                          backTextRemain\n                                                          back\n                                                          backTextI ] [\n\n  result: pText\n  \n  if error? e: try [\n    textLen: length? pText\n    if pColWidth > textLen [\n      backTextLen:     length? pBackText\n      remainWidth:     pColWidth - textLen\n      backTextRepeats: make integer! remainWidth / backTextLen\n      backTextRemain:  remainWidth // backTextLen\n      back:            WRITER__STRING_empty\n      backTextI:       0\n      while [ backTextI < backTextRepeats ] [\n        back:      rejoin [ back pBackText ]\n        backTextI: backTextI + 1\n      ]\n      if 0 < backTextRemain [\n        back: rejoin [ back copy/part pBackText backTextRemain ]\n      ]\n\n      if \"l\" = pAlignment [\n        result: rejoin [ result back ]\n      ]\n      if \"c\" = pAlignment [\n        result: rejoin [ copy/part back (make integer! divide length? back 2)  result  at back (1 + make integer! divide length? back 2) ]\n      ]\n      if \"r\" = pAlignment [\n        result: rejoin [ back result ]\n      ]\n    ]\n    true\n  ][\n    result: pText\n  ]\n\n  result\n]\n\n\n\n; Get text of file currently being generated.\n_getText: func [] [\n  writer__iCurrentText\n]\n\n\n\n; Set text of file currently being generated.\n_setText: func [ pText ] [\n  writer__iCurrentText: pText\n]\n\n\n; Set build properties file.\n_setMetaPropFile: func [ pMetaPropFile ] [\n  writer__iMetaPropFile:   pMetaPropFile\n  writer__iPropertiesInit: $<lang.FalseString>\n]\n\n\n; Get build properties file.\n_getMetaPropFile: func [] [\n  writer__iMetaPropFile\n]\n\n\n\n; Template Services (Text Production) >> ;\n\n\n\n\n; Writer Services <<\n\n; Initialize.\nwriter__initialize: func [] [\n  writer__iCurrentFileIndex: 0\n  writer__setDefaults\n]\n\n\n\n; Main file generation loop. Template script is placed here in the body section.\nwriter__write: func [ /local writer__fileNameRoots \n                             writer__numFiles \n                             writer__fileI \n                             writer__currentSection \n                    ] [\n\n  ; initialize\n  writer__currentSection: \"init\"\n\n\n  if error? e: try [\n\n    !<{init>; section:init\n    !<init>!<}init> \n\n\n    ; write files loop\n    writer__numFiles:      _getNumFiles\n    writer__fileI:         0\n    writer__fileNameRoots: _getFileNameRoots\n\n    forall writer__fileNameRoots [\n\n      if error? e: try [\n\n        !<{prewrite>; section:prewrite\n        writer__currentSection: \"prewrite\"\n        !<prewrite>!<}prewrite> \n\n\n        if writer__startFile [\n\n          !<{body>; section:body\n          writer__currentSection: \"body\"\n          !<body>!<}body> \n\n\n          if writer__endFile [ \n\n\n            !<{postwrite>; section:postwrite\n            writer__currentSection: \"postwrite\"\n            !<postwrite>!<}postwrite> \n\n          ] ; writer__endFile\n        ] ; writer__startFile\n\n        true\n      ][\n        writer__handleException  rejoin [ WRITER__UITEXT_ExceptionIn WRITER__UITEXT_Section writer__currentSection ]  disarm e      \n      ]\n\n      writer__nextFile\n    ]\n \n\n    !<{cleanup>; section:cleanup\n    writer__currentSection: \"cleanup\"\n    !<cleanup>!<}cleanup> \n\n    true\n  ][\n    writer__handleException rejoin [ WRITER__UITEXT_ExceptionIn WRITER__UITEXT_Section writer__currentSection ]  disarm e\n  ]\n]\n\n\n\n; Start writing a file.\nwriter__startFile: func [] [\n  writer__iCurrentText: WRITER__STRING_empty\n  true\n]\n\n\n\n; End writing a file.\nwriter__endFile: func [ /local endOK fileName filePath ] [\n  endOK: true\n\n  fileName: _getFullFileName\n  filePath: rejoin [ writer__iOutputFolder\n                     WRITER__STRING_separator\n                     fileName ]\n\n  if writer__iBackup [\n    if error? e: try [\n      writer__backup  filePath fileName writer__iBackupFolder\n      true\n    ][\n      writer__handleException  rejoin [ WRITER__UITEXT_UnableToBackupFile \n                                        filePath \n                                        WRITER__UITEXT_ToBackupFolder\n                                        writer__iBackupFolder ]\n                               disarm e\n      endOK: false\n    ]\n   ]\n \n  if all [ endOK writer__iSave ] [\n    either error? e: try [\n      _saveTextFile filePath writer__iCurrentText\n    ][\n      writer__handleException  rejoin [WRITER__UITEXT_UnableToSaveFile filePath ]  disarm e\n      endOK: false\n    ][\n      writer__userMessage  rejoin [ WRITER__UITEXT_SavedFile filePath WRITER__UITEXT_NewLine ] false\n    ]\n  ]\n\n  endOK\n]\n\n\n\n; Move to next file\nwriter__nextFile: func [] [\n  writer__iCurrentFileIndex: writer__iCurrentFileIndex + 1\n]\n\n\n\n; Handle command line arguments to CodeWriter.\nwriter__handleArgs: func[ pArgs /local argName_OutputFolder\n                                       argName_MetaPropFile\n                                       argName_BackupFolder\n                                       argName_NoBackup \n                                       args ] [\n\n  ; set arg names\n  argName_OutputFolder: rejoin [ WRITER__ARGUMENT_CONTROL_PREFIX WRITER__ARGUMENT_OutputFolder ]\n  argName_MetaPropFile: rejoin [ WRITER__ARGUMENT_CONTROL_PREFIX WRITER__ARGUMENT_MetaPropFile ]\n  argName_BackupFolder: rejoin [ WRITER__ARGUMENT_CONTROL_PREFIX WRITER__ARGUMENT_BackupFolder ]\n  argName_NoBackup:     rejoin [ WRITER__ARGUMENT_CONTROL_PREFIX WRITER__ARGUMENT_NoBackup     ]\n\n  ; parse args\n  ; ...\n\n  foreach arg pArgs [\n    if find/match arg argName_OutputFolder [\n      _setOutputFolder  find/match arg argName_OutputFolder\n    ]\n    if find/match arg argName_MetaPropFile [\n      _setMetaPropFile  find/match arg argName_MetaPropFile\n    ]\n    if find/match arg argName_BackupFolder [\n      _setBackupFolder  find/match arg argName_BackupFolder\n    ]\n    if argName_NoBackup = arg [\n      _backup $<lang.FalseString> ;; NOTE: -B => don't make backups\n    ]\n  ]\n\n  writer__initArgs  make integer! length? pArgs  pArgs\n]\n\n\n\n; Set defaults from configuration property set.\nwriter__setDefaults: func [] [\n\n  $<{main.FileNameRoot>_setFileNameRoot \"$<\\main.FileNameRoot>\" $<}main.FileNameRoot>\n  $<{main.FileNamePrefix>_setFileNamePrefix( \"$<\\main.FileNamePrefix>\" );$<}main.FileNamePrefix>\n  $<{main.FileNameSuffix>_setFileNameSuffix( \"$<\\main.FileNameSuffix>\" );$<}main.FileNameSufffix>\n\n  $<{main.OutputFolder>_setOutputFolder( \"$<\\main.OutputFolder>\" );$<}main.OutputFolder>\n\n  $<{main.BackupFolder>_setBackupFolder( \"$<\\main.BackupFolder>\" );$<}main.BackupFolder>\n  $<{main.BackupPrefix>_setBackupPrefix( \"$<\\main.BackupPrefix>\" );$<}main.BackupPrefix>\n  $<{main.BackupSuffix>_setBackupSuffix( \"$<\\main.BackupSuffix>\" );$<}main.BackupSufffix>\n  _backup( \"$<lang.TrueString>\" = \"$<jostraca.MakeBackup>\" );\n\n]\n\n\n\n; Initialize command line arguments.\nwriter__initArgs: func [ pNumArgs pArgs /local argI userArgI ] [\n  argI:     0\n  userArgI: 0\n\n  writer__iNumArgs:     0 + pNumArgs\n  writer__iArgs:        pArgs\n  writer__iNumUserArgs: writer__iNumArgs\n\n  argI: 1\n  while [ argI <= writer__iNumArgs ] [\n    if find/match  make string! at writer__iArgs argI  WRITER__ARGUMENT_CONTROL_PREFIX [\n      writer__iNumUserArgs: writer__iNumUserArgs - 1\n    ]\n    argI: argI + 1\n  ]\n\n  writer__iUserArgs: copy []\n\n  argI: 1\n  while [ argI <= writer__iNumArgs ] [\n    if not find/match  make string! at writer__iArgs argI  WRITER__ARGUMENT_CONTROL_PREFIX [\n      append writer__iUserArgs pick writer__iArgs argI\n      userArgI: userArgI + 1\n    ]\n    argI: argI + 1\n  ]\n]\n\n\n\n; Print a user readable message.\nwriter__userMessage: func [ pMessage pError ] [\n  if pError [\n    ; output to stderr not supported\n    print rejoin [ \"ERROR: \" pMessage ]\n  ][\n  print pMessage\n  ]\n]\n\n\n\n; Handle exceptions: print an explanation for user.\nwriter__handleException: func [ pMessage pException /local userMsg ] [\n\n  userMsg: rejoin [ WRITER__UITEXT_ErrorHeader\n                    writer__describeState \n                    pMessage \n                    WRITER__UITEXT_ColonNewLine\n                    probe pException\n                    WRITER__UITEXT_ErrorFooter ]\n\n  writer__userMessage userMsg true \n]\n\n\n\n; Provide a concise description of the state of the CodeWriter.\nwriter__describeState: func [ /local currentState ] [\n  currentState: rejoin [ \n    WRITER__UITEXT_CodeWriterState\n    WRITER__UITEXT_GetFileIndexEquals    _getFileIndex\n    WRITER__UITEXT_GetFullFileNameEquals _getFullFileName\n    WRITER__UITEXT_GetOutputFolderEquals _getOutputFolder\n    WRITER__UITEXT_NewLine\n  ] \n  currentState\n]\n\n\n\n; Backup overwritten files, if they exist.\n; Backups have the format: [YYYYMMDDhhmmss][prefix][filename][suffix].\nwriter__backup: func[ pFilePath pFileName pBackupFolder /local backupFolder\n                                                               dateTime\n                                                               backupFileName\n                                                               backupFilePath \n                                                               dateTimeNow\n                                                               year_yyyy\n                                                               month_mm \n                                                               day_dd   \n                                                               hour_hh  \n                                                               minute_mm\n                                                               second_ss\n\n] [\n\n  backupFolder: make file! pBackupFolder\n\n  ; check backup folder ( create if necessary )\n  if not exists? backupFolder [\n    if error? e: try [    \n      make-dir backupFolder\n      true\n    ][\n      make error!  rejoin [ WRITER__UITEXT_BackupFolderColon\n                            backupFolder\n                            WRITER__UITEXT_BackupFolderExistFailure ]\n    ]\n  ]\n\n  if not dir? backupFolder [\n    make error! rejoin [ WRITER__UITEXT_BackupFolderColon \n                         backupFolder\n                         WRITER__UITEXT_BackupFolderNotAFolder ]\n  ]\n\n;  don't know how to check if folder is writable\n;  if( !( -w $backupFolder ) ) {\n;    die( $WRITER__UITEXT_BackupFolderColon\n;         . $backupFolder\n;         . $WRITER__UITEXT_BackupFolderNotWritable );\n;  }\n\n\n  ; create backup file path\n  dateTimeNow: now\n  year_yyyy: _align make string! dateTimeNow/year     \"0\" 4 \"r\"\n  month_mm:  _align make string! dateTimeNow/month    \"0\" 2 \"r\"\n  day_dd:    _align make string! dateTimeNow/day      \"0\" 2 \"r\"\n  hour_hh:   _align make string! dateTimeNow/time/1   \"0\" 2 \"r\"\n  minute_mm: _align make string! dateTimeNow/time/2   \"0\" 2 \"r\"\n  second_ss: _align make string! dateTimeNow/time/3   \"0\" 2 \"r\"\n  dateTime:  rejoin [ year_yyyy month_mm day_dd hour_hh minute_mm second_ss ]\n  backupFileName: rejoin [ dateTime writer__iBackupPrefix pFileName writer__iBackupSuffix ]\n  backupFilePath: rejoin [ pBackupFolder WRITER__STRING_separator backupFileName ]\n\n  ; save backup file\n  if exists? make file! pFilePath [\n    _saveTextFile backupFilePath  read make file! pFilePath\n  ]\n]\n\n\n\n; set compile time properties\nwriter__initProperties: func [ /local metaPropFile content lines line nv n v ] [\n  metaPropFile: _getMetaPropFile()\n\n  if not \"\" == metaPropFile [\n      writer__iProperties: make hash! []\n\n      if error? e: try [\n\n        content: _loadTextFile( metaPropFile )\n        replace/all content rejoin[ \"\\\" newline ] \"\"\n\n        lines: parse/all content rejoin [ newline ]\n        foreach line lines [\n          nv: parse/all line \"=:\"\n          n:  rejoin [ pick nv 1 ]\n          v:  rejoin [ pick nv 2 ]\n          if not \"#\" == first n [\n            insert writer__iProperties reduce [ trim n trim v ]\n          ] \n        ]\n\n        writer__iPropertiesInit: $<lang.TrueString>\n\n      ][\n        writer__handleException  rejoin [ WRITER__UITEXT_UnableToLoadMetaProps _getMetaPropFile ]  disarm e      \n      ]\n   ]\n]\n\n; Writer Services >> ;\n\n\n\n\n; Execute.\nmain: func [ pArgs /local e ] [\n\n  if error? e: try [\n    writer__initialize\n    writer__handleArgs pArgs\n    writer__write\n  ][\n    writer__handleException  rejoin [ WRITER__UITEXT_ExceptionIn WRITER__UITEXT_Method WRITER__UITEXT_Main ]  disarm e      \n  ]\n]\n\n\n\n; Start execution\nmain system/options/args\n\n\n!<footer>\n\n");
    }