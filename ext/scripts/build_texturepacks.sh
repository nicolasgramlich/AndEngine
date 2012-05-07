#! /bin/bash

# Binary of the Texturepacker:
TEXTUREPACKER_BINARY=/usr/local/bin/TexturePacker

# Shared TexturePack-Output-Definitions:
TEXTUREPACK_OUTPUT_DIRECTORY="../assets/gfx/texturepacks/"
TEXTUREPACK_OUTPUT_JAVAIDS_PACKAGE="org.website.project.texturepacks"
TEXTUREPACK_OUTPUT_JAVAIDS_DIRECTORY="../src/org/website/project/texturepacks/"

# Shared TexturePack-Input-Definitions:
TEXTUREPACK_INPUT_BASEDIRECTORY="../ext/gfx/texturepacks/"

# Individual TexturePack-Definitions (separated by ';'):
# Fragment #1: The input for the TexturePack, relative to ${TEXTUREPACK_INPUT_BASEDIRECTORY}
# Fragment #2: The name of the generated TexturePack, relative to ${TEXTUREPACK_OUTPUT_DIRECTORY}
# Fragment #3: The filename of the generated java interface that contains the IDs of the textureregions in the generated TexturePack.
TEXTUREPACK_INPUT_DATA=(
 "texturepackerexample/;texturepackerexample;TexturePackerExampleTexturePack.java"
)

###########################
# Clean old TexturePacks: #
###########################
echo 
echo "#########################################"
echo -n "# Cleaning old TexturePacks..."
# TODO rm -f ${TEXTUREPACK_OUTPUT_JAVAIDS_DIRECTORY}*.java
rm -f ${TEXTUREPACK_OUTPUT_DIRECTORY}*.pvr.ccz
rm -f ${TEXTUREPACK_OUTPUT_DIRECTORY}*.xml
echo " done."
echo "#########################################"
echo

##############################
# Generate new TexturePacks: #
##############################
echo "#########################################"
echo "# Generating ${#TEXTUREPACK_INPUT_DATA[@]} TexturePacks..."
echo "#########################################"
echo 

# Loop over all entries ${TEXTUREPACK_INPUT_DATA} to generate one TexturePack for each entry:
i=0
for TEXTUREPACK_INPUT_DATA_ENTRY in "${TEXTUREPACK_INPUT_DATA[@]}"
do
	echo "#########################################"
	i=`expr $i + 1`
	echo "# Generating TexturePack $i of ${#TEXTUREPACK_INPUT_DATA[@]} ..."
	echo "#"
	
	# Split fragments of ${TEXTUREPACK_INPUT_DATA_ENTRY} into ${TEXTUREPACK_INPUT_DATA_ENTRY_FRAGMENT}
	IFS=\; read -a TEXTUREPACK_INPUT_DATA_ENTRY_FRAGMENT <<< "$TEXTUREPACK_INPUT_DATA_ENTRY"
	
	# Generate TEXTUREPACKs
	${TEXTUREPACKER_BINARY} ${TEXTUREPACK_INPUT_BASEDIRECTORY}${TEXTUREPACK_INPUT_DATA_ENTRY_FRAGMENT[0]}*.png \
		--format andengine \
		--opt RGBA4444 \
		--border-padding 0 \
		--shape-padding 1 \
		--disable-rotation \
		--no-trim \
		--max-width 1024 \
		--max-height 1024 \
		--andengine-wraps clamp \
		--andengine-wrapt clamp \
		--andengine-minfilter linear \
		--andengine-magfilter linear \
		--data ${TEXTUREPACK_OUTPUT_DIRECTORY}${TEXTUREPACK_INPUT_DATA_ENTRY_FRAGMENT[1]}.xml \
		--sheet ${TEXTUREPACK_OUTPUT_DIRECTORY}${TEXTUREPACK_INPUT_DATA_ENTRY_FRAGMENT[1]}.pvr.ccz \
		--andengine-java ${TEXTUREPACK_OUTPUT_JAVAIDS_DIRECTORY}${TEXTUREPACK_INPUT_DATA_ENTRY_FRAGMENT[2]} \
		--andengine-packagename ${TEXTUREPACK_OUTPUT_JAVAIDS_PACKAGE}
	echo "#"
	echo "# done."
	echo "#########################################"
	echo
done
echo "#########################################"
echo "# done."
echo "#########################################"
echo 

exit 0