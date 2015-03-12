import sys
import urllib
"""
Iuput SDSS Stripe ID (sid), run ID (rid), rerun ID (rrid), colunm (col) 
and output the resulting file names in [sid]_[rid]_[rrid]_[col].txt
argv[1] = sid
argv[2] = rid
argv[3] = rrid
argv[4] = col

http://das.sdss.org/www/html/imaging/dr-99.html


example: python getFileNames.py 82 5902 40 1
"""
def main():
    url = generateURL(sys.argv[2], sys.argv[3], sys.argv[4])
    f = urllib.urlopen(url)
    s = f.read()

def generateURL(rid, rrid, col):
    url = 'http://das.sdss.org/www/cgi-bin/wholeseg?RUN='
    url += rid
    url += '&RERUN='
    url += rrid
    url += '&CAMCOL='
    url += col
    url += '&DR=99'
    return url
    
if __name__ == "__main__":
    main()
