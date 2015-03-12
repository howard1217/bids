#!/bin/bash
# 20150311
# Getting 160000 file names from SDSS
# http://das.sdss.org/imaging/

# Get the RunIDs into runid.txt, which contains lines of integer IDs

wget http://das.sdss.org/imaging/ -q -O - > temp.txt
cat temp.txt | cut -d '"' -f 8 > runid.txt
cat runid.txt | sed 's/\///g' > temp.txt
egrep -x '[0-9]+' temp.txt > runid.txt
javac Helper.java
java Helper
rm temp.txt
rm runid.txt