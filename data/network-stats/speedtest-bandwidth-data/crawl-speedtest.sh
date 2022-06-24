#!/bin/bash
echo -n > data.csv
while read line; do
	echo $line
	echo $line,$(wget -qO- "https://www.speedtest.net/global-index/$line" | grep 'class="number">' | cut -d'>' -f2 | cut -d'<' -f1 | tr '\n' ', ') >> data.csv
done < countries.csv
