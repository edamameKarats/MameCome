#!/bin/bash
SHL_DIR=$(cd $(dirname $0);pwd)
cd $SHL_DIR
. ./MameCommentStart.ini
${JAVA_HOME}/java --module-path=${JFX_HOME}/ --add-modules=javafx.base --add-modules=javafx.controls --add-modules=javafx.fxml --add-modules=javafx.web -jar MameComment.jar
