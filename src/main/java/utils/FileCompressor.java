package utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;


/**
 * Created on : Dec 30, 2018
 * Author     : AndroidWave
 * Website    : https://androidwave.com/
 */
public class FileCompressor {
    //max width and height values of the compressed image is taken as 612x816
    private int maxWidth = 100;
    private int maxHeight = 100;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int quality = 40;
    private String destinationDirectoryPath;

    public FileCompressor(Context context) {
        destinationDirectoryPath = context.getCacheDir().getPath() + File.separator + "images";
    }

    public FileCompressor setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public FileCompressor setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public FileCompressor setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
        return this;
    }

    public FileCompressor setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public FileCompressor setDestinationDirectoryPath(String destinationDirectoryPath) {
        this.destinationDirectoryPath = destinationDirectoryPath;
        return this;
    }


    public File compressToFile(File imageFile, String type) throws IOException {
        if(type.equals("S")){
            maxHeight =  100;
            maxWidth = 100;


        }
        return ImageUtilities.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality,
                destinationDirectoryPath + File.separator + imageFile.getName());
    }

    public Bitmap compressToBitmap(File imageFile) throws IOException {
        return ImageUtilities.decodeSampledBitmapFromFile(imageFile, maxWidth, maxHeight);
    }



}