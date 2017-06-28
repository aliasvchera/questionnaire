@set @x=0; /*
@cscript /e:jscript "%~f0"
@exit */
oShell = new ActiveXObject("WScript.Shell");
myDir = oShell.CurrentDirectory;
uShortcut = oShell.CreateShortcut("Weimarer Republik - Die Kampfbuende.lnk");
uShortcut.TargetPath = myDir + "\\questionnaire.jar";
// uShortcut.Arguments = "";
uShortcut.WorkingDirectory = myDir;
uShortcut.Description = "Fight for the streets and fear of civil war";
uShortcut.IconLocation =myDir + "\\data\\icons\\War-Ensign-Of-Germany.ico";
uShortcut.Save();