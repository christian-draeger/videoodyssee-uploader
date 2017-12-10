#!/bin/bash -e

if [ -n "$(git status --porcelain)" ]; then
	git status --porcelain
 	echo "there are changes";
  	echo "please commit everything so we have a reproducible git commit ID as LambdaCD version";
  	exit 1;
fi

USER=$1

mvn -f ../pom.xml clean install
ansible-playbook -i inventories --ask-sudo-pass --user $USER playbook.yml
