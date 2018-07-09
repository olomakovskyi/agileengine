Command line parameters:
Absolute path to base file for searching element by id (mandatory);
Absolute path to diff file for searching element found in base file (mandatory);
Id for searching in base file (optional, by default "make-everything-ok-button")
Attributes for searching in diff file. Should be delimited by comma (optional, by default "class,href,onclick")

Last parameter is needed as I didn't get from task how should we detect if found element is the same or not.
For example, if buttons look the same but do different things.