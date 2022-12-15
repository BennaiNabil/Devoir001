#!/bin/sh

# Custom variables
mode=$1
folder=$2
isOk=0

# Colour variables
COLOROFF="\033[1;0m"    # standard color
GREENCOLOR="\033[1;32m" # green
REDCOLOR="\033[1;31m"   # red

#WHITECOLOR="\033[1;37m"    # white
#BLUECOLOR="\033[1;36m"     # blue
#DARKBLUECOLOR="\033[1;34m" # darkblue
#LILACOLOR="\033[1;35m"     # lilac
#YELLOWCOLOR="\033[1;33m"   # yellow

# ----------- Executes the jar file -----------
runJavaProgram() {
  echo "$GREENCOLOR Running the program with mode <$mode> and in the folder <$folder>...${COLOROFF}"
  if [ "$mode" != "numeric" ] && [ "$mode" != "presuf" ]; then
    echo "$REDCOLOR Incorrect mode, chose between presuf and numeric${COLOROFF}"
    isOk=0
  else
    java -cp commons-io-2.11.0.jar:devoirjar.jar -Dpropfilename=config_rename.properties org.devoir.Main $mode $folder
    export isOk=1
  fi
}

displaySuccessOrFailure() {
  if [ "$isOk" = 1 ]; then
    echo "$GREENCOLOR The files have been successfully renamed${COLOROFF}"
  else
    echo "$REDCOLOR The files have not be renamed, check your arguments${COLOROFF}"
  fi
}

displayHelp() {
  echo "Usage: $(basename "$0") <renaming_method> [numeric | presuf] <folder_name>"
  echo "numeric : renames all the files in the given folder with a$GREENCOLOR base name and an incrementing number${COLOROFF}"
  echo "presuf : renames all the files in the given folder by$GREENCOLOR prefixing and suffixing the existing files${COLOROFF}"
  echo "Examples : "
  echo "run.sh numeric testfolder"
  echo "run.sh presuf testfolder"
}

# Running the program
if [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
  displayHelp
  exit 0
fi
runJavaProgram
displaySuccessOrFailure
