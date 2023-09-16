# Asset-Directory

## Overview
This directory is used as a VSC for all assets utilizing git. All assets can be organized freely within this directory.
The assets will be imported automatically into the core project using a gradle-integrated asset pipeline.
Assets and/or subdirectories that are supposed to be excluded from pipeline-processing are listed in .exclude files within each directory.

## Asset-Pipeline
The Asset-Pipeline is automatically deployed whenever necessary using gradle. It preprocesses all assets 
(for example by reading special config files like .exclude), 
before deploying the respective asset processing tools from libGDX.
More detailed information are listed below and in the pipeline module.
Currently, it only utilizes the TexturePacker, but more tools will be incorporated as needed.

### TexturePacker
The TexturePacker processes sprites and animations in the "texture" subdirectory. These are combined into Sprite-Sheets of size 1024 x 1024 px.
For that purpose texture-assets are organized into groups, where alle elements of the same group are combined on a separate set of pages. 
(Separate sets of pages can be loaded separately, while a single set of pages can only be loaded all at once).

The default group is 'misc', 
but additional groups can be declared using .group files, which state the name of all files in the current directory, as well as all
subdirectories unless overwritten by another .group file in a deeper directory.

The TexturePacker can process sprites and animations equally. Animations are recognized by having multiple sprites 
(as frames), that carry the same name except for ending on "_[Frame Number]". No two sprites and or animations of the 
same group may have the same name. For compatibility reasons however, it is advised that this applies to the entire project.