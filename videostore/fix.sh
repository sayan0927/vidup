for f in *.webm; do ffmpeg -i $f -c copy -fflags +genpts ${f%%.*}_fixed.webm; done
# Prompt user for keypress
read -n 1 -s -r -p "Press any key to continue..."

