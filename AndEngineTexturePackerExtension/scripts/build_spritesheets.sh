#! /bin/bash

# Binary of the Texturepacker:
TEXTUREPACKER_BINARY=/usr/local/bin/TexturePacker

# Shared Spritesheet-Output-Definitions:
SPRITESHEET_OUTPUT_DIRECTORY="../assets/gfx/spritesheets/"
SPRITESHEET_OUTPUT_JAVAIDS_PACKAGE="org.website.project.spritesheets"
SPRITESHEET_OUTPUT_JAVAIDS_DIRECTORY="../src/org/website/project/spritesheets/"

# Shared Spritesheet-Input-Definitions:
SPRITESHEET_INPUT_BASEDIRECTORY="../ext/gfx/spritesheets/"

# Individual Spritesheet-Definitions (separated by ';'):
# Fragment #1: The input for the spritesheet, relative to ${SPRITESHEET_INPUT_BASEDIRECTORY}
# Fragment #2: The name of the generated spritesheet, relative to ${SPRITESHEET_OUTPUT_DIRECTORY}
# Fragment #3: The filename of the generated java interface that contains the IDs of the textureregions in the generated spritesheet.
SPRITESHEET_INPUT_DATA=(
 "texturepackerexample/;texturepackerexample;TexturePackerExampleSpritesheet.java"
)

###########################
# Clean old Spritesheets: #
###########################
echo 
echo "#########################################"
echo -n "# Cleaning old spritesheets..."
# TODO rm -f ${SPRITESHEET_OUTPUT_JAVAIDS_DIRECTORY}*.java
rm -f ${SPRITESHEET_OUTPUT_DIRECTORY}*.pvr.ccz
rm -f ${SPRITESHEET_OUTPUT_DIRECTORY}*.xml
echo " done."
echo "#########################################"
echo

##############################
# Generate new Spritesheets: #
##############################
echo "#########################################"
echo "# Generating ${#SPRITESHEET_INPUT_DATA[@]} spritesheets..."
echo "#########################################"
echo 

# Loop over all entries ${SPRITESHEET_INPUT_DATA} to generate one spritesheet for each entry:
i=0
for SPRITESHEET_INPUT_DATA_ENTRY in "${SPRITESHEET_INPUT_DATA[@]}"
do
	echo "#########################################"
	i=`expr $i + 1`
	echo "# Generating spritesheet $i of ${#SPRITESHEET_INPUT_DATA[@]} ..."
	echo "#"
	
	# Split fragments of ${SPRITESHEET_INPUT_DATA_ENTRY} into ${SPRITESHEET_INPUT_DATA_ENTRY_FRAGMENT}
	IFS=\; read -a SPRITESHEET_INPUT_DATA_ENTRY_FRAGMENT <<< "$SPRITESHEET_INPUT_DATA_ENTRY"
	
	# Generate spritesheets
	${TEXTUREPACKER_BINARY} ${SPRITESHEET_INPUT_BASEDIRECTORY}${SPRITESHEET_INPUT_DATA_ENTRY_FRAGMENT[0]}*.png \
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
		--data ${SPRITESHEET_OUTPUT_DIRECTORY}${SPRITESHEET_INPUT_DATA_ENTRY_FRAGMENT[1]}.xml \
		--sheet ${SPRITESHEET_OUTPUT_DIRECTORY}${SPRITESHEET_INPUT_DATA_ENTRY_FRAGMENT[1]}.pvr.ccz \
		--andengine-java ${SPRITESHEET_OUTPUT_JAVAIDS_DIRECTORY}${SPRITESHEET_INPUT_DATA_ENTRY_FRAGMENT[2]} \
		--andengine-packagename ${SPRITESHEET_OUTPUT_JAVAIDS_PACKAGE}
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