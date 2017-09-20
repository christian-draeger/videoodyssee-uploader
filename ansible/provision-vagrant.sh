#!/bin/bash

# create /etc/idealo which is present in OPS's VM images per default
sudo mkdir -p /etc/idealo && sudo chmod 777 /etc/idealo
sudo mkdir -p /data && sudo chmod -R 777 /data
sudo mkdir -p /data/idealo && sudo chmod -R 777 /data/idealo
sudo ln -vfs /data/idealo /usr/share/idealo

# add idealo repos
sudo echo "
deb http://httpredir.debian.org/debian jessie main
deb http://security.debian.org/ jessie/updates main
deb http://aptrepo.ipx jessie main
deb http://aptrepo.ipx jessie-testing main
" > /etc/apt/sources.list

# activate idealo repo
sudo apt-get update

# install packages
sudo apt-get install attr

# meh
echo "never mind the public key error above, it's ok for a vagrant box installation"
