# 'texture' - Folder

This folder is a special folder, designated for processing with the libGDX TexturePacker. All Assets in this folder will
be packed onto Sprite-Sheets.
Read more about the TexturePacker here: https://libgdx.com/wiki/tools/texture-packer

Usage of this TexturePacker is automated in the build process via this special pipeline.
While in the regular TexturePacker the groups are defined by the folder structure, we found this system too limiting an
unorganized.
Instead membership to groups is assigned via `.group` files, containing exactly one word. Each `.group` file defines the
group of all files in its current directory as well as any subdirectory, unless overwritten by another group file in a
deeper directory.
Additionally `.exclude` files still apply normally.

## Texture-lookup Preprocessing

Another special format recognized by the pipeline are `.cbase` and `.ubase` files. These files contain the path (
starting in the assets/res folder) to the compressed and uncompressed base skins respectively.
Any directory that contains a `.cbase` file will be recognized as texture-lookup-compatible sprite:

Each image inside will be converted in a way that allows for an arbitrary skins to be applied at runtime. This is
achieved by converting all of its pixels in position on the base skins. For this each pixel on the sprite is mapped to
the position of equal color on the compressed base skin. The position is then saved in the rg channel of the resulting
sprite. The a channel is copied unaltered. The b channel is used to save lighting information: For each sprite a light
file (titled `[full name of the sprite].light`) may exist. If so the grayscale values in this light file will be saved
to the b channel: Here `#000000` denotes maximum darkness `#FFFFFF` maximum brightness and `#7f7f7f` no change a.e. the
original color described by the skin. The a channel is ignored for `.light` files and no file is interpreted
as `#7f7f7f`.

Any directory that contains both `.cbase` and `.ubase` files will be recognized as texture-lookup-compatible skin. They
need to match the format of the uncompressed base skin and will be compressed according to the difference between
compressed and uncompressed base skin. This will render them suitable for application at runtime.

Afterwards the resulting sprites are processed as a regular images by the TexturePacker (.group files etc. apply
normally)

The texture directory can therefore only process image files in addition to the aforementioned special control files.
These files will be removed automatically during build and do not require manual exclusion.
