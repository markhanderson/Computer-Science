/* 
 * File: ImageShopAlgorithms.java
 * Name: Julianne Crawford
 * Section Leader: Peter Maldonado
 * --------------------------------
 * Purpose: The ImageShopAlgorithms program gives the user the ability to manipulate
 * a source image in several ways. Namely, the user can invert the colors of an image, 
 * remove a green background from an image (to simulate a green screen), rotate an 
 * image right or left, flip an image over it's central vertical axis, shift the image
 * right and/or left, blur the image, and enhance the image by increasing its grayscale 
 * contrast. 
 */

import java.util.*;
import acm.graphics.*;
import java.awt.*;

public class ImageShopAlgorithms implements ImageShopAlgorithmsInterface {
	
	private static final int COLOR_INVERSE = 255; 
	private static final int TRANSPARENT_ALPHA = 0; 
	private static final int LUMINOSITY_RANGE = 255; 
	
	/* Method: flipHorizontal
	 * The flipHorizontal method flips the source across its vertical center line
	 * Parameters: 
	 * source - source image uploaded into the GUI from the ImageShopProgram 
	 * Output: 
	 * FlippedImage - source image flipped across its vertical center line 
	 */
	public GImage flipHorizontal(GImage source) {
		int[][] pixels = source.getPixelArray(); 
		int numRows = numRows(pixels); 
		int numCols = numCols(pixels); 
		int[][] pixelsFlipped = new int[numRows][numCols]; 
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				int rNew = r;
				int cNew = (numCols-1)-c; 
				pixelsFlipped[rNew][cNew] = pixels[r][c]; 
			}
		}
		GImage FlippedImage = new GImage(pixelsFlipped); 
		return FlippedImage; 	}
	
	/* Method: rotateLeft
	 * The rotateLeft method rotates the source image 90-degrees counter-clockwise
	 * Parameters: 
	 * source - source image uploaded into the GUI from the ImageShopProgram 
	 * Output: 
	 * rotatedLeftImage - source image rotated 90-degrees counter-clockwise 
	 */
	public GImage rotateLeft(GImage source) {
		int[][] pixels = source.getPixelArray(); 
		int numRows = numRows(pixels); 
		int numCols = numCols(pixels); 
		int[][] pixelsRotatedLeft = new int[numCols][numRows]; 
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				int rNew = (numCols-1) - c;
				int cNew = r; 
				pixelsRotatedLeft[rNew][cNew] = pixels[r][c]; 
			}
		}
		GImage rotatedLeftImage = new GImage(pixelsRotatedLeft); 
		return rotatedLeftImage; 
	}
	
	/* Method: rotateRight
	 * The rotateRight method rotates the source image 90-degrees clockwise
	 * Parameters: 
	 * source - source image uploaded into the GUI from the ImageShopProgram 
	 * Output: 
	 * rotatedRightImage - source image rotated 90-degrees clockwise 
	 */
	public GImage rotateRight(GImage source) {
		int[][] pixels = source.getPixelArray(); 
		int numRows = numRows(pixels); 
		int numCols = numCols(pixels); 
		int[][] pixelsRotatedRight = new int[numCols][numRows]; 
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				int rNew = c;
				int cNew = (numRows-1)-r; 
				pixelsRotatedRight[rNew][cNew] = pixels[r][c]; 
			}
		}
		GImage rotatedRightImage = new GImage(pixelsRotatedRight); 
		return rotatedRightImage; 
	}

	/* Method: greenScreen
	 * The greenScreen method converts green pixels in the source image
	 * to transparent pixels. 
	 * Parameters: 
	 * source - source image uploaded into the GUI from the ImageShopProgram 
	 * Outputs: 
	 * greenScreenImage - source image with transparent pixels in place of green pixels
	 */
	public GImage greenScreen(GImage source) {
		int[][] pixels = source.getPixelArray(); 
		int numRows = numRows(pixels); 
		int numCols = numCols(pixels); 
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				int pixel = pixels[r][c]; 
				int red = GImage.getRed(pixel); 
				int green = GImage.getGreen(pixel); 
				int blue = GImage.getBlue(pixel); 
				int rgbMax = Math.max(red,blue); 
				if(green >= 2*rgbMax) {
					pixels[r][c] = GImage.createRGBPixel(red,green,blue, TRANSPARENT_ALPHA); 
				} else {
					pixels[r][c] = GImage.createRGBPixel(red,green,blue); 
				}
			}
		}
		GImage greenScreenImage = new GImage(pixels); 
		return greenScreenImage; 
	}

	/* Method: equalize
	 * The equalize method enhances the source image by spreading out the intensities 
	 * of its grayscale values to increase its effective contrast. 
	 * Parameters: 
	 * source - source image uploaded into the GUI from the ImageShopProgram 
	 * Output: 
	 * equalizedImage - equalized source image 
	 */
	public GImage equalize(GImage source) {
		int[] luminosityArray = luminosityHistogram(source); 
		int[] cumulativeLuminosityArray = cumulativeLuminosityHistogram(luminosityArray); 
		int[][] increasedContrastPixels = increaseContrast(cumulativeLuminosityArray, source); 
		GImage equalizedImage = new GImage(increasedContrastPixels);
		return equalizedImage;
	}
	
	/* Method: luminosityHistogram
	 * The luminosityHistogram method creates an array to store the luminosity 
	 * values of the source image 
	 * Parameters: 
	 * source - source image uploaded into the GUI from the ImageShopProgram
	 * Output: 
	 * luminosityArray - array containing all of the image's luminosity values, 
	 * sorted based on their value. 
	 */
	private int[] luminosityHistogram(GImage source) {
		int[][] pixels = source.getPixelArray();
		int[] luminosityArray = new int[LUMINOSITY_RANGE+1]; 
		int numRows = numRows(pixels); 
		int numCols = numCols(pixels); 
		
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				int pixel = pixels[r][c]; 
				int red = GImage.getRed(pixel); 
				int green = GImage.getGreen(pixel); 
				int blue = GImage.getBlue(pixel); 
				int luminosity = computeLuminosity(red,green,blue); 
				luminosityArray[luminosity]++; 
			}
		}
		return luminosityArray; 
	}
	
	/* Method: cumulativeLuminosityHistogram
	 * The cumulativeLuminosityHistogram creates a cumulativeLuminosityArray 
	 * representing the number of pixels in the image with a certain luminosity 
	 * or less
	 * Parameters: 
	 * luminosityArray - array containing all of the source image's luminosity values, 
	 * sorted based on their value. 
	 * Output: 
	 * cumulativeLuminosityArray - array containing the number of pixels int the image
	 * with a certain luminosity or less
	 */
	private int[] cumulativeLuminosityHistogram(int[] luminosityArray) {
		int[] cumulativeLuminosityArray = new int[LUMINOSITY_RANGE+1];
		cumulativeLuminosityArray[0] = luminosityArray[0]; 
		for (int i = 0; i < cumulativeLuminosityArray.length-1; i++) {
			cumulativeLuminosityArray[i+1] = luminosityArray[i+1]+cumulativeLuminosityArray[i]; 
		}
		return cumulativeLuminosityArray; 
	}

	/* Method: increaseContrast
	 * The increaseContrast method spreads the luminosity values across as much of  
	 * the luminosity value range as possible. By doing so, the contrast of the image
	 * is effectively increased. 
	 * Parameters: 
	 * cumulativeLuminosityArray - array containing the number of pixels int the image
	 * with a certain luminosity or less
	 * source - source image uploaded into the GUI from the ImageShopProgram
	 * Output: 
	 * increasedContrastPixels - a matrix containing the "blurred" pixels of the source image
	 */
	private int[][] increaseContrast(int[] cumulativeLuminosityArray, GImage source) {
		int[][] pixels = source.getPixelArray();
		int numRows = numRows(pixels); 
		int numCols = numCols(pixels); 
		int[][] increasedContrastPixels = new int[numRows][numCols]; 
		// modify each pixel to increase its contrast 
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				int pixel = pixels[r][c]; 
				int red = GImage.getRed(pixel); 
				int green = GImage.getGreen(pixel); 
				int blue = GImage.getBlue(pixel); 
				int luminosity = computeLuminosity(red,green,blue);
				int numerator = cumulativeLuminosityArray[luminosity]; 
				int denominator = numCols*numRows; 
				int newRgb = Math.round(255*numerator/denominator);
				increasedContrastPixels[r][c] = GImage.createRGBPixel(newRgb, newRgb, newRgb); 
			}
		}
		return increasedContrastPixels;
	}
	
	/* Method:negative 
	 * The negative method inverts the colors in the source image
	 * Parameters: 
	 * source - source image uploaded into the GUI from the ImageShopProgram 
	 * Output: 
	 * negativeImage - source Image with inverted rgb pixels
	 */
	public GImage negative(GImage source) {
		int[][] pixels = source.getPixelArray();
		int numRows = numRows(pixels); 
		int numCols = numCols(pixels); 
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				int pixel = pixels[r][c];
				int red = COLOR_INVERSE - GImage.getRed(pixel); 
				int green = COLOR_INVERSE - GImage.getGreen(pixel); 
				int blue = COLOR_INVERSE - GImage.getBlue(pixel); 
				pixels[r][c] = GImage.createRGBPixel(red, green, blue);  
			}
		}
		GImage negativeImage = new GImage(pixels); 
		return negativeImage;
	}

	/* Method: translate
	 * The translate method shifts the pixel coordinates of the source image 
	 * by dx pixels in the y-dimension and dy pixels in the x-dimension
	 * Parameters: 
	 * source - source image uploaded into the GUI from the ImageShopProgram 
	 * dx - offset (in pixels) in the y-dimension 
	 * dy - offset (in pixels) in the x-dimension 
	 * Output: 
	 * translatedImage - source Image shifted by dx pixels in the y-dimension and dy 
	 * pixels in the x-dimension
	 */
	public GImage translate(GImage source, int dx, int dy) {
		int[][] pixels = source.getPixelArray(); 
		int numRows = numRows(pixels); 
		int numCols = numCols(pixels); 
		int[][]pixelsTranslated = new int[numRows][numCols]; 
		int rNew = 0; 
		int cNew = 0;
		int wrapAmount = 0; 
		if(dx > pixels[0].length) {
			dx %= pixels[0].length;
		} else if(dy > pixels.length) {
			dy %= pixels.length; 
		}
		for(int r = 0; r < numRows; r++) {
			if(r + dy > pixels.length-1) {
				wrapAmount = (r+dy)-(pixels.length-1); 
				rNew = wrapAmount - 1; 
			} else if(r + dy < 0) {
				wrapAmount = Math.abs(r+dy); 
				rNew = (pixels.length-1)-(wrapAmount-1); 
			} else {
				rNew = r + dy; 
			}
			for(int c = 0; c < numCols; c++) {
				if(c + dx > pixels.length-1) {
					wrapAmount = (c+dx)-(pixels.length-1); 
					cNew = wrapAmount - 1; 
				} else if(c + dx < 0) {
					wrapAmount = Math.abs(c+dx); 
					cNew = (pixels.length-1)-(wrapAmount-1); 
				} else {
					cNew = c + dx; 
				}
				pixelsTranslated[rNew][cNew] = pixels[r][c]; 
			}
		}
		GImage translatedImage = new GImage(pixelsTranslated); 
		return translatedImage; 
	}

	/* Method: blur
	 * The blur method blurs the source image by averaging the rgb values of
	 * the source image's pixel values with the values of their immediate neighbors
	 * Parameters: 
	 * source - source image uploaded into the GUI from the ImageShopProgram
	 * Output: 
	 * blurredImage - blurred source Image 
	 * 
	 */
	public GImage blur(GImage source) {
		int[][] pixels = source.getPixelArray(); 
		int numRows = numRows(pixels); 
		int numCols = numCols(pixels); 
		int[][]pixelsBlurred = new int[numRows][numCols]; 
		for (int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				// top edge row pixels
				if (r == 0) {
					// top left corner pixel  
					if(c == 0) {
						int rLowRange = r; 
						int rHighRange = r+1; 
						int cLowRange = c; 
						int cHighRange = c+1;
						pixelsBlurred = createBlurredPixel(pixels,pixelsBlurred,rLowRange,rHighRange,cLowRange,cHighRange,r,c); 
					} // top right corner pixel 
					else if(c == numCols-1) {
						int rLowRange = r; 
						int rHighRange = r+1; 
						int cLowRange = c-1; 
						int cHighRange = c;
						pixelsBlurred = createBlurredPixel(pixels,pixelsBlurred,rLowRange,rHighRange,cLowRange,cHighRange,r,c); 
					} else {
						int rLowRange = r; 
						int rHighRange = r+1; 
						int cLowRange = c-1; 
						int cHighRange = c+1;
						pixelsBlurred = createBlurredPixel(pixels,pixelsBlurred,rLowRange,rHighRange,cLowRange,cHighRange,r,c); 
					}
				} // left edge column pixels 
				else if(c == 0) {
					// bottom left corner pixel 
					if(r == numRows-1) {
						int rLowRange = r-1; 
						int rHighRange = r; 
						int cLowRange = c; 
						int cHighRange = c+1;
						pixelsBlurred = createBlurredPixel(pixels,pixelsBlurred,rLowRange,rHighRange,cLowRange,cHighRange,r,c); 
					} else {
						int rLowRange = r-1; 
						int rHighRange = r+1; 
						int cLowRange = c; 
						int cHighRange = c+1;
						pixelsBlurred = createBlurredPixel(pixels,pixelsBlurred,rLowRange,rHighRange,cLowRange,cHighRange,r,c); 
					}
				} // right edge column pixels  
				else if(c == numCols - 1) {
					// bottom right corner pixel 
					if(r == numRows-1) {
						int rLowRange = r-1; 
						int rHighRange = r; 
						int cLowRange = c-1; 
						int cHighRange = c;
						pixelsBlurred = createBlurredPixel(pixels,pixelsBlurred,rLowRange,rHighRange,cLowRange,cHighRange,r,c); 
					} else {
						int rLowRange = r-1; 
						int rHighRange = r+1; 
						int cLowRange = c-1; 
						int cHighRange = c;
						pixelsBlurred = createBlurredPixel(pixels,pixelsBlurred,rLowRange,rHighRange,cLowRange,cHighRange,r,c); 
					}
				} // bottom edge row pixels 
				else if(r == numRows-1) {
					int rLowRange = r-1; 
					int rHighRange = r; 
					int cLowRange = c-1; 
					int cHighRange = c+1;
					pixelsBlurred = createBlurredPixel(pixels,pixelsBlurred,rLowRange,rHighRange,cLowRange,cHighRange,r,c); 
				} // inner pixels with neighbors on all sides 
				else {
					int rLowRange = r-1; 
					int rHighRange = r+1; 
					int cLowRange = c-1; 
					int cHighRange = c+1;
					pixelsBlurred = createBlurredPixel(pixels,pixelsBlurred,rLowRange,rHighRange,cLowRange,cHighRange,r,c); 
				}
			}
		}
		GImage blurredImage = new GImage(pixelsBlurred); 
		return blurredImage;
	}
	
	/* Method: createBlurredPixel
	 * The createBlurredPixel method takes in a pixel from the original source
	 * image and creates a new "blurred" pixel by averaging the pixel's rgb values
	 * with its immediate neighbor's rgb values. 
	 * Parameters: 
	 * pixels - matrix of pixels of the original source image
	 * pixelsBlurred - matrix storing the blurred pixels
	 * rLowRange - the lower neighboring row bound
	 * rHighRange - the upper neighboring row bound
	 * cLowRange - the lower neighboring column bound
	 * cHighRange - the upper neighboring column bound
	 * r - pixel row 
	 * c - pixel column 
	 * Output: 
	 * pixelsBlurred - matrix storing the blurred pixels, including the newly blurred pixel
	 */
	private int[][] createBlurredPixel(int[][] pixels,int[][] pixelsBlurred,int rLowRange,int rHighRange,int cLowRange,int cHighRange,int r,int c){
		int red = 0; 
		int green = 0; 
		int blue = 0; 
		int numNeighbors = 0; 
		// summing the red, green and blue values of a pixel and its neighbors 
		for(int i = rLowRange; i < rHighRange+1; i++) {
			for(int j = cLowRange; j < cHighRange+1; j++) {
				int pixel = pixels[i][j];
				red += GImage.getRed(pixel); 
				green += GImage.getGreen(pixel); 
				blue += GImage.getBlue(pixel); 
				numNeighbors ++; 
			}
		}
		int redAvg = red/numNeighbors; 
		int greenAvg = green/numNeighbors; 
		int blueAvg = blue/numNeighbors; 
		pixelsBlurred[r][c] = GImage.createRGBPixel(redAvg, greenAvg, blueAvg); 
		return pixelsBlurred; 
	}
	
	/* Method: numRows
	 * the numRows method returns the number of rows in an input matrix 
	 * Parameters: 
	 * matrix - a numRows x numCols matrix 
	 * Output: 
	 * int - the number of rows in the matrix 
	 */
	private int numRows(int[][] matrix) {
		return matrix.length; 
	}
	
	/* Method: numCols
	 * the numCols method returns the number of columns in an input matrix 
	 * Parameters: 
	 * matrix - a numRows x numCols matrix 
	 * Output: 
	 * int - the number of columns in the matrix 
	 */
	private int numCols(int[][] matrix) {
		return matrix[0].length; 
	}
}
