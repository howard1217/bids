#!/bin/bash
# 20150311
# Getting 160000 file names from SDSS
# http://das.sdss.org/imaging/
# http://das.sdss.org/www/html/imaging/dr-99.html

# Get the RunIDs into runid.txt, which contains lines of integer IDs

wget http://das.sdss.org/www/html/imaging/dr-99.html -q -O - > temp.txt
cat temp.txt | sed 's/\///g' > temp2.txt
cat temp2.txt | cut -d '<' -f 2 > temp.txt
cat temp.txt | sed 's/td>//g' > temp2.txt
egrep -x '[-]?[0-9]+\.?[0-9]*' temp2.txt > stripeToRun.txt
rm temp.txt
rm temp2.txt
javac Helper.java
java Helper $1
rm stripeToRun.txt