echo " Creating different dimensions (dips) of "$1" ..."

if [! -d "mipmap-xxxhdpi" ]; then
	mkdir mipmap-xxxhdpi
fi
if [! -d "mipmap-xxhdpi" ]; then
	mkdir mipmap-xxhdpi
fi
if [! -d "mipmap-xhdpi" ]; then
	mkdir mipmap-xhdpi
fi
if [! -d "mipmap-hdpi" ]; then
	mkdir mipmap-hdpi
fi
if [! -d "mipmap-mhdpi" ]; then
	mkdir mipmap-mhdpi
fi

convert $1 -resize 192x192 mipmap-xxxhdpi/ic_launcher.png
convert $1 -resize 144x144 mipmap-xxhdpi/ic_launcher.png
convert $1 -resize 96x96 mipmap-xhdpi/ic_launcher.png
convert $1 -resize 72x72 mipmap-hdpi/ic_launcher.png
convert $1 -resize 48x48 mipmap-mdpi/ic_launcher.png
