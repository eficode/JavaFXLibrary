#!/bin/bash
EXIT_VALUE=0

function local() {
  echo "INFO: Local execution:"
  file=$(ls -1 /javafxbinary/javafxlibrary-*-jar-with-dependencies.jar)
  java -cp "${file}" org.robotframework.RobotFramework -d /robot/results/local --include smoke $@ /robot/acceptance
#  $@ #just to testing script
  if [[ "$?" != "0" ]]; then
    EXIT_VALUE=$((EXIT_VALUE+1))
  fi
}

function remote() {
  echo "INFO: Remote execution:"
  robot -d /robot/results/remote --include smoke $@ /robot/acceptance
#  $@ #just to testing script
  if [[ "$?" != "0" ]]; then
    EXIT_VALUE=$((EXIT_VALUE+2))
  fi
}


case $1 in
  local | LOCAL )
    shift
    local $@
    ;;
  remote | REMOTE | demo | DEMO)
    shift
    remote $@
    ;;
  "" | all | ALL )
    echo "INFO: All execution:"
      shift
      local $@
      remote $@
    ;;
  * )
    echo "ERROR:$@ is not supported parameter"
    EXIT_VALUE=$((EXIT_VALUE+4))
    ;;
esac
echo "*************************************************"
case ${EXIT_VALUE} in
  0 )
    echo "INFO: All fine, tests PASS"
    ;;
  1 )
    echo "ERROR: Local library tests fail"
    ;;
  2 )
    echo "ERROR: Remote library tests fail"
    ;;
  3 )
    echo "ERROR: Local and Remote library tests fails"
  ;;
esac
exit ${EXIT_VALUE}
