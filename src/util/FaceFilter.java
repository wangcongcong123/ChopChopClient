package util;

/**
 * this util class is used to filter the users' face
 *  the types of faces are restricted to three formats, .jpg, .png, and .gif
 */
import java.io.File;
import java.io.FileFilter;

public class FaceFilter implements FileFilter {

	public boolean accept(File file) {
		// if the face file meet one of the three formats, the method return
		// true, otherwise return false
		String name = file.getName();
		if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif"))
			return true;
		else
			return false;

	}

}
